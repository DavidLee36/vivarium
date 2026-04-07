extends Control

@onready var tick_label: Label = $VBoxContainer/TickLabel
@onready var mote_count_label: Label = $VBoxContainer/MoteCountLabel
@onready var average_label: Label = $VBoxContainer/AverageLabel

func _ready() -> void:
	SimManager.mote_change_signal.connect(_update_motes)
	SimManager.tick_signal.connect(_tick)

func _tick(tick: int) -> void:
	tick_label.text = "Tick: " + str(tick)

func _update_motes() -> void:
	mote_count_label.text = "Current: " + str(SimManager.get_current_mote_count())
	average_label.text = "Average: " + str(SimManager.get_mote_count_average())
