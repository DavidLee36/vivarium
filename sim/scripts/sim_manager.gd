extends Node3D

var scene_tree

var left_bound: float = -75
var right_bound: float = 75
var top_bound: float = 75
var bottom_bound: float = -75

var TICK_TIME: float = 1 # In seconds
var tick_timer: Timer
var curr_tick: int = 0

var sim_data = { # Data to exported to json for graphing/data collection
	"mote_count": []
}

var mote_scene = load("res://scenes/mote.tscn")
var motes: Array = []
var mote_spawn_chance: float = 1

func _ready() -> void:
	scene_tree = get_tree()
	_timer_setup()

## Code that is run every tick of the simulation
func _tick() -> void:
	_pre_tick()
	_spawn_random()
	for mote in motes:
		mote.tick()
	_post_tick()

## Code that should always run before/start of each tick
func _pre_tick() -> void:
	curr_tick += 1

## Code that should always run after/end of each tick
func _post_tick() -> void:
	sim_data["mote_count"].append(motes.size())
	print(sim_data["mote_count"])

## Registers a mote to be tracked in the sim, should be called upon mote creation
func register_mote(mote: Mote) -> void:
	motes.append(mote)

## Unregister mote from living mote data, should be called whenever a mote dies
func unregsiter_mote(mote: Mote) -> void:
	motes.remove_at(motes.find(mote))

## Spawns a random mote at a random location
func _spawn_random() -> void:
	var spawn_point = Vector3(randf_range(left_bound, right_bound), 1, randf_range(bottom_bound, top_bound))
	var new_mote = mote_scene.instantiate()
	new_mote.position = spawn_point
	scene_tree.get_root().get_node("World/Motes").add_child(new_mote)

## Code that should run at the very end of the simulation
func _end_simulation() -> void:
	var sim_data_str = JSON.stringify(sim_data, "\t")
	Util.write_to_file("sim_runs/run.json", sim_data_str)
	pass

## Create a timer to call tick() every time the timer ends
func _timer_setup() -> void:
	tick_timer = Timer.new()
	add_child(tick_timer)
	tick_timer.wait_time = TICK_TIME
	tick_timer.one_shot = false
	tick_timer.timeout.connect(_tick)
	tick_timer.start()
