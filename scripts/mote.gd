class_name Mote

extends CharacterBody3D

enum State {
	WANDERING,
	SEEKING_FOOD,
	SEEKING_WATER
}

var state: State = State.WANDERING

var target_position: Vector3
var last_tick_position: Vector3
var rotation_speed_min: float = 1.5
var rotation_speed_max: float = 5.0
var stuck: bool = false

var move_r: int = 20

var sight_r: int = 10
var speed: int = 5
var death_chance: float = 0.05 # between 0-1

var food: float = 100
var water: float = 100

func _ready() -> void:
	target_position = global_position
	SimManager.register_mote(self)

func _physics_process(delta: float) -> void:
	_update_curr_state()
	_movement_logic(delta)

func tick() -> void:
	if randf_range(0, 1) <= death_chance:
		die()
	
	if _at_position_buffer(global_position, last_tick_position, 0.05):
		print("stuck")
		stuck = true
	else: stuck = false
	last_tick_position = global_position

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
	#velocity = dir * speed

	move_and_slide()

func _update_curr_state() -> void:
	pass

func _wander() -> bool:
	if _at_position_buffer(global_position, target_position, 0.05) or stuck: # Pick a new target pos
		target_position.x = clamp(randi_range(int(global_position.x) - move_r, int(global_position.x) + move_r), SimManager.left_bound, SimManager.right_bound)
		target_position.z = clamp(randi_range(int(global_position.z) - move_r, int(global_position.z) + move_r), SimManager.bottom_bound, SimManager.top_bound)
		return true
	return false

func die() -> void:
	SimManager.unregsiter_mote(self)
	queue_free()

func _seek_resource() -> void:
	pass

func _at_position_buffer(pos_1: Vector3, pos_2: Vector3, buffer: float) -> bool:
	# Currently only deal with x and z
	if abs(pos_1.x - pos_2.x) < buffer and abs(pos_1.z - pos_2.z) < buffer:
		return true
	return false
