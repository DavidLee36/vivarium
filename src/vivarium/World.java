package vivarium;

import vivarium.map.Map;
import vivarium.map.biomes.BiomeType;
import vivarium.utils.Logger;

public class World {
	private static final int RUN_TIME = 10000;
	private static final int MAP_RADIUS = 5;

	private Map map;
	private static int currTick = 0;
	private int runTime;

	private int totalAnimals = 0;
	private int totalPlants = 0;

	public World() {
		map = new Map(MAP_RADIUS);

		logWorldCreation();
	}

	/**
	 * Main heart of the simulation, this code runs each tick controlling what happens each tick
	 */
	protected void runTick() {
		currTick++;
		Logger.log("Running", true);
	}

	/**
	 * Log all relevant info upon world creation
	 */
	private void logWorldCreation() {
		Logger.logLine();
		Logger.log("World created with " + map.getHexes().size() + " hexes", false);
		for (BiomeType biome : BiomeType.getAllTypes()) {
			Logger.log(biome.name() + " count: " + biome.getCount(), false);
		}
		Logger.logLine();
	}

	//#region Getters and Setters

	public Map getMap() { return map; }
	public static int getTickCount() { return currTick; }
	public int getTotalAnimals() { return totalAnimals; }
	public int getTotalPlants() { return totalPlants; }

	//#endregion
}
