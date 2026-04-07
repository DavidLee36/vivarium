extends Node3D

@export var default_speed: float = 15
@export var sensitivity: float = 0.3

var _mouse_delta := Vector2.ZERO
@onready var _pitch: float = rad_to_deg(rotation.x)

func _input(event: InputEvent) -> void:
	if event is InputEventMouseMotion:
		_mouse_delta = event.relative

func _physics_process(delta: float) -> void:
	_handle_input()
	_movement_logic(delta)
	_mouse_delta = Vector2.ZERO

func _handle_input() -> void:
	if Input.is_action_pressed("speed_time"):
		Engine.time_scale = 10.0
	else:
		Engine.time_scale = 1.
	if Input.is_action_just_pressed("end_sim"):
		SimManager.end_simulation()

func _movement_logic(delta: float) -> void:
	# Speed
	var speed = default_speed * 4 if Input.is_action_pressed("shift") else default_speed
	
	# WASD
	var input_dir := Input.get_vector("move_left", "move_right", "move_forward", "move_back")
	var move := (transform.basis * Vector3(input_dir.x, 0, input_dir.y)).normalized()
	position += move * speed * delta
	if position.y <= 0: position.y = 0.1
	
	# Vertical
	if Input.is_action_pressed("space_bar"):
		position.y += speed * delta
	elif Input.is_action_pressed("ctrl"):
		position.y -= speed * delta
	
	# PANNING
	if Input.is_action_pressed("right_click"):
		Input.mouse_mode = Input.MOUSE_MODE_CAPTURED
		rotate_y(deg_to_rad(-_mouse_delta.x * sensitivity))
		_pitch = clamp(_pitch - _mouse_delta.y * sensitivity, -90, 90)
		rotation.x = deg_to_rad(_pitch)
	else:
		Input.mouse_mode = Input.MOUSE_MODE_VISIBLE

	if position.y <= 0.1: position.y = 0.1
