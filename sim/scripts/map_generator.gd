extends Node3D

var grid_map : GridMap

@export var map_size : int = 256
@export var cell_size: int = 2
@export var map_seed : int = 32
@export var frequency: float = 0.03

var tiles = [
	["abyss", 0.28, "#001a3d"],
	["deep_water", 0.40, "#012d73"],
	["water", 0.46, "#1a64db"],
	["beach", 0.50, "#f5be5f"],
	["dry_grass", 0.55, "#b8d147"],
	["light_grass", 0.66, "#4ec23a"],
	["forest", 0.76, "#1f6b2e"],
	["hills", 0.86, "#6b5d3e"],
	["mountain", 0.94, "#8a8a8a"],
	["snow", 1.0, "#ffffff"]
]

func _ready() -> void:
	grid_map = $GridMap
	grid_map.cell_size = Vector3i(cell_size, cell_size, cell_size)
	grid_map.mesh_library = generate_mesh_library()
	generate_map()

func generate_mesh_library() -> MeshLibrary:
	var mesh_library: MeshLibrary = MeshLibrary.new()
	for i in range(tiles.size()):
		var mesh = BoxMesh.new()
		mesh.size = Vector3(cell_size, cell_size, cell_size)
		var material = StandardMaterial3D.new()
		material.albedo_color = tiles[i][2]
		mesh.surface_set_material(0, material)
		mesh_library.create_item(i)
		mesh_library.set_item_mesh(i, mesh)
	return mesh_library

func generate_map() -> void:
	var noise = FastNoiseLite.new()
	noise.seed = map_seed
	noise.noise_type = FastNoiseLite.TYPE_PERLIN
	noise.frequency = frequency

	for x in range(map_size):
		for z in range(map_size):
			var noise_value = noise.get_noise_2d(x, z)
			noise_value = (noise_value + 1) / 2 # convert noise from -1 to 1 to 0 to 1
			var tile_idx: int
			for i in range(tiles.size()):
				if noise_value <= tiles[i][1]:
					tile_idx = i
					break
			grid_map.set_cell_item(Vector3i(x, -1, z), tile_idx, 0)
