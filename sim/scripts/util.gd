class_name Util

static var base_path: String = "C:/Users/david/Documents/vivarium/"

static func write_to_json_file(path: String, content: String):
	var idx: int = 0
	while(FileAccess.file_exists(base_path + path + str(idx) + ".json")):
		idx += 1
	var file = FileAccess.open(base_path + path + str(idx) + ".json", FileAccess.WRITE)
	file.store_string(content)
	file.close()
	print("Wrote to file")
