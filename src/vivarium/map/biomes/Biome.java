package vivarium.map.biomes;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import vivarium.organisms.animals.Animal;
import vivarium.organisms.plants.Plant;
import vivarium.utils.*;

import com.badlogic.gdx.graphics.Color;

public abstract class Biome {
	protected float temp;
	protected float moisture;
	protected Color color;
	protected HashMap<Class<? extends Animal>, Float> animalSpawnRates;
	protected HashMap<Class<? extends Plant>, Float> plantSpawnRates;

	protected Biome(float temp, float moisture, Color color) {
		this.temp = temp;
		this.moisture = moisture;
		this.color = color;
		this.animalSpawnRates = buildAnimalSpawnRates();
	}

	protected abstract HashMap<Class<? extends Animal>, Float> buildAnimalSpawnRates();
	protected abstract HashMap<Class<? extends Plant>, Float> buildPlantSpawnRates();

	public Class<? extends Animal> getRandomAnimalClass() {
		Random rng = new Random();
		float rFloat = rng.nextFloat(1f);
		for (Entry<Class<? extends Animal>, Float> entry : animalSpawnRates.entrySet()) {
			rFloat = rFloat - entry.getValue();
			if (rFloat <= 0f) return entry.getKey();
		}
		return null;
	}

	// public Plant SpawnRandomPlant() {

	// }

	public void log() {
		Logger.log("Biome: " + this.getClass().getSimpleName());
		Logger.log("Temp: " + this.temp);
		Logger.log("Moisture: " + this.moisture);
	}

	public Color getColor() { return color; }
}