package vivarium.map.biomes;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;

import vivarium.organisms.animals.Animal;
import vivarium.organisms.animals.mammals.*;
import vivarium.organisms.plants.Plant;

public class Desert extends Biome {
	public Desert() {
		super(90f, 0.1f, new Color(0.82f, 0.70f, 0.44f, 1));
	}

	@Override
	protected HashMap<Class<? extends Animal>, Float> buildAnimalSpawnRates() {
		HashMap<Class<? extends Animal>, Float> animalSpawns = new HashMap<>();

		animalSpawns.put(Rabbit.class, 1f);

		return animalSpawns;
	}

	@Override
	protected HashMap<Class<? extends Plant>, Float> buildPlantSpawnRates() {
		return new HashMap<Class<? extends Plant>, Float> ();
	}
}
