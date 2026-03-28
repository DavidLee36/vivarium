extends CharacterBody3D

enum State {
	WANDERING,
	SEEKING_FOOD,
	SEEKING_WATER
}

var state: State = State.WANDERING
@onready var target_position: Vector3 = global_position
var rotation_speed_min: float = 1.5
var rotation_speed_max: float = 5.0

var move_r: int = 20

var sight_r: int = 10
var speed: int = 3

var food: float = 100
var water: float = 100

func _ready() -> void:
	pass

func _physics_process(delta: float) -> void:
	_update_curr_state()
	_movement_logic(delta)

func _movement_logic(delta: float) -> void:
	if not is_on_floor():
		velocity.y -= 20 * delta
	if state == State.WANDERING:
		_wander()

	var dir = (target_position - global_position).normalized()
	var flat_dir = Vector3(dir.x, 0, dir.z).normalized()
	if flat_dir.length() > 0.1:
		var turn_angle = rad_to_deg((-transform.basis.z).angle_to(flat_dir))
		var target_basis := Basis.looking_at(flat_dir)
		transform.basis = transform.basis.slerp(target_basis, delta * remap(turn_angle, 0, 180, rotation_speed_min, rotation_speed_max))
	velocity = lerp(velocity, dir * speed, delta * 2.0)

	move_and_slide()

func _update_curr_state() -> void:
	pass

func _wander() -> bool:
	if _at_position_buffer(0.05) or (global_position.x == 0 and global_position.z == 0): # Pick a new target pos
		target_position.x = randi_range(int(global_position.x) - move_r, int(global_position.x) + move_r)
		target_position.z = randi_range(int(global_position.z) - move_r, int(global_position.z) + move_r)
		return true
	return false

func _seek_resource() -> void:
	pass

func _at_position_buffer(buffer: float) -> bool:
	# Currently only deal with x and z
	if abs(global_position.x - target_position.x) < buffer and abs(global_position.z - target_position.z) < buffer:
		return true
	return false
