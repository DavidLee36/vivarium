extends Node3D

# Total terrain span in world units (a square of world_size x world_size).
@export var world_size: float = 154.0
# Number of vertices per side of the grid. More = smoother, fewer = chunkier.
@export var resolution: int = 80

# --- Noise / terrain shape ---
# Vertical scale of the terrain. Peaks reach ~+amplitude, valleys ~-amplitude.
@export var amplitude: float = 10.0
# Different seeds produce different worlds. Same seed = same world every run.
@export var noise_seed: int = 0
# Roughly 1 / feature_size. Lower = bigger smoother continents, higher = noisier.
@export var noise_frequency: float = 0.02
# Number of noise layers stacked together (fractal). More = more detail.
@export var noise_octaves: int = 4

# The MeshInstance3D child that will display the generated mesh.
@onready var terrain: MeshInstance3D = $Terrain

# preload runs at script-compile time (cheaper than load() for things we always need).
const TERRAIN_MATERIAL := preload("res://materials/green_ground.tres")

func _ready() -> void:
	_build_terrain()

# Builds a heightmap mesh with flat (faceted) shading and assigns it to $Terrain.
func _build_terrain() -> void:
	# step = distance between neighboring vertices.
	# resolution-1 because N vertices have N-1 gaps between them.
	var step: float = world_size / float(resolution - 1)
	# half is used to center the terrain on origin instead of starting at the corner.
	var half: float = world_size * 0.5

	# Set up the noise generator. Same seed always produces the same world.
	var noise := FastNoiseLite.new()
	noise.seed = noise_seed
	noise.frequency = noise_frequency
	# FBM = fractional Brownian motion: stack multiple octaves for natural-looking detail.
	noise.fractal_type = FastNoiseLite.FRACTAL_FBM
	noise.fractal_octaves = noise_octaves

	# --- Pre-compute every grid point's world position ---
	# Stored in a flat array so we can look up quad corners by (i, j) below
	# without re-sampling noise four times per quad.
	var positions := PackedVector3Array()
	positions.resize(resolution * resolution)
	for j in range(resolution):
		for i in range(resolution):
			var x := i * step - half
			var z := j * step - half
			# Sample noise (~ -1..1) and scale to get this vertex's height.
			var y := noise.get_noise_2d(x, z) * amplitude
			positions[j * resolution + i] = Vector3(x, y, z)

	# --- Build triangles with non-shared vertices for flat shading ---
	# Each triangle gets its own 3 vertices, all carrying that triangle's face
	# normal. Since the 3 normals agree, the GPU has nothing to interpolate
	# and the triangle shades as a single flat tone — the low-poly look.
	# No index buffer: vertices are read in groups of 3 as consecutive triangles.
	var quad_count := (resolution - 1) * (resolution - 1)
	var vertices := PackedVector3Array()  # 3 verts per triangle, 2 triangles per quad
	var normals := PackedVector3Array()
	vertices.resize(quad_count * 6)
	normals.resize(quad_count * 6)

	var v := 0
	for j in range(resolution - 1):
		for i in range(resolution - 1):
			# Look up this quad's 4 corner positions.
			var a := positions[j * resolution + i]             # bottom-left
			var b := positions[j * resolution + (i + 1)]       # bottom-right
			var c := positions[(j + 1) * resolution + i]       # top-left
			var d := positions[(j + 1) * resolution + (i + 1)] # top-right

			# Face normal = cross product of two edges of the triangle.
			# This particular cross-order matches our winding so normals point
			# upward (out of the visible surface), not into the ground.
			var n1 := (c - a).cross(b - a).normalized()
			vertices[v] = a; normals[v] = n1; v += 1
			vertices[v] = b; normals[v] = n1; v += 1
			vertices[v] = c; normals[v] = n1; v += 1

			var n2 := (c - b).cross(d - b).normalized()
			vertices[v] = b; normals[v] = n2; v += 1
			vertices[v] = d; normals[v] = n2; v += 1
			vertices[v] = c; normals[v] = n2; v += 1

	# --- Pack and build the mesh (no ARRAY_INDEX this time) ---
	var arrays := []
	arrays.resize(Mesh.ARRAY_MAX)
	arrays[Mesh.ARRAY_VERTEX] = vertices
	arrays[Mesh.ARRAY_NORMAL] = normals

	var mesh := ArrayMesh.new()
	mesh.add_surface_from_arrays(Mesh.PRIMITIVE_TRIANGLES, arrays)
	mesh.surface_set_material(0, TERRAIN_MATERIAL)
	terrain.mesh = mesh
