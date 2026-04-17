extends Control

@onready var tick_label: Label = $SimDataContainer/TickLabel
@onready var mote_count_label: Label = $SimDataContainer/MoteCountLabel
@onready var average_label: Label = $SimDataContainer/AverageLabel

@onready var mote_data_container: ColorRect = $MoteDataContainer
@onready var mote_id_label: Label = $MoteDataContainer/MoteDataList/MoteID
@onready var mote_age_label: Label = $MoteDataContainer/MoteDataList/MoteAge
@onready var mote_state_label: Label = $MoteDataContainer/MoteDataList/MoteState
@onready var mote_food_label: Label = $MoteDataContainer/MoteDataList/MoteFood
@onready var mote_water_label: Label = $MoteDataContainer/MoteDataList/MoteThirst
@onready var close_button: Button = $MoteDataContainer/CloseButton

var curr_mote: Mote :
	set(value):
		if curr_mote: curr_mote.indicator.visible = false
		if value: value.indicator.visible = true
		curr_mote = value

func _ready() -> void:
	SimManager.mote_change_signal.connect(_update_mote_count_labels)
	SimManager.tick_signal.connect(_tick)
	SimManager.mote_click_signal.connect(_mote_clicked)

	mote_data_container.visible = false

func _tick(tick: int) -> void:
	tick_label.text = "Tick: " + str(tick)
	if curr_mote: _update_mote_data_labels()
	if not curr_mote and mote_data_container.visible == true: # Mote that is currently being viewed has died
		mote_age_label.text = "Age: Dead"

## Run whenever the amount of motes in the world changes, this can happen more than once per tick
func _update_mote_count_labels() -> void:
	mote_count_label.text = "Current: " + str(SimManager.get_current_mote_count())
	average_label.text = "Average: " + str(SimManager.get_mote_count_average())

func _update_mote_data_labels() -> void:
	mote_id_label.text = "ID: " + str(curr_mote.id)
	mote_age_label.text = "Age: " + str(SimManager.curr_tick - curr_mote.id)
	mote_state_label.text = "State: WANDERING" # TODO: Make this shit accurate
	mote_food_label.text = "Food: " + str(curr_mote.food)
	mote_water_label.text = "Water: " + str(curr_mote.water)

func _mote_clicked(mote: Mote) -> void:
	curr_mote = mote
	mote_data_container.visible = true
	_update_mote_data_labels()

func _on_close_button_pressed() -> void:
	curr_mote = null
	mote_data_container.visible = false
