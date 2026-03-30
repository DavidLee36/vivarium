extends Node3D

var scene_tree

var left_bound: float = -75
var right_bound: float = 75
var top_bound: float = 75
var bottom_bound: float = -75

var TICK_TIME: float = 1 # In seconds
var tick_timer: Timer
var curr_tick: int = 0

var mote_scene = load("res://scenes/mote.tscn")
var motes: Array = []
var mote_spawn_chance: float = 1

func _ready() -> void:
	scene_tree = get_tree()
	_timer_setup()

func _tick() -> void:
	curr_tick += 1
	_spawn_random()
	for mote in motes:
		mote.tick()

func register_mote(mote: Mote) -> void:
	motes.append(mote)

func _spawn_random() -> void:
	var spawn_point = Vector3(randf_range(left_bound, right_bound), 1, randf_range(bottom_bound, top_bound))
	var new_mote = mote_scene.instantiate()
	new_mote.position = spawn_point
	scene_tree.get_root().get_node("World/Motes").add_child(new_mote)

func _timer_setup() -> void:
	tick_timer = Timer.new()
	add_child(tick_timer)
	tick_timer.wait_time = TICK_TIME
	tick_timer.one_shot = false
	tick_timer.timeout.connect(_tick)
	tick_timer.start()
