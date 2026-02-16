package vivarium.utils;

import java.io.FileWriter;
import vivarium.World;

public class Logger {
	private static final String PATH = "C:/Users/david/Desktop/Projects/Vivarium/logs/";
	private static String defaultLog = "log.txt";


	public static void clear() {
		clear(defaultLog);
	}

	public static void clear(String fileName) {
		try {
			FileWriter writer = new FileWriter(PATH + fileName);
			writer.close();
		} catch (Exception e) {
			System.out.println("Error clearing file: " + e);
		}
	}

	public static void log(String msg, boolean showTick) {
		try {
			FileWriter writer = new FileWriter(PATH + defaultLog, true);
			if (showTick) {
				writer.write(World.getTickCount() + ": ");
			}
			writer.write(msg + "\n");
			writer.close();
		} catch (Exception e) {
			System.out.println("Error writing to log: " + e);
		}
	}

	public static void logLine() {
		log("===================================================================", false);
	}

	public static void setDefaultLogFile(String fileName) {
		defaultLog = fileName + ".txt";
	}

	public static String getLogName() { return defaultLog; }
}
