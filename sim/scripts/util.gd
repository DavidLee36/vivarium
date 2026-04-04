class_name Util

static var base_path: String = "C:/Users/david/Documents/vivarium/"

static func write_to_file(path: String, content: String):
	var file = FileAccess.open(base_path + path, FileAccess.WRITE)
	file.store_string(content)
	file.close()
