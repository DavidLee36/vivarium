package vivarium.map.biomes;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;

import vivarium.organisms.animals.Animal;
import vivarium.organisms.animals.mammals.*;
import vivarium.organisms.plants.Plant;

public class Forest extends Biome {
	public Forest() {
		super(65f, 0.2f, new Color(0.18f, 0.42f, 0.14f, 1));
	}

	@Override
	protected HashMap<Class<? extends Animal>, Float> buildAnimalSpawnRates() {
		HashMap<Class<? extends Animal>, Float> animalSpawns = new HashMap<>();

		animalSpawns.put(Wolf.class, 0.75f);
		animalSpawns.put(Rabbit.class, 0.25f);

		return animalSpawns;
	}

	@Override
	protected HashMap<Class<? extends Plant>, Float> buildPlantSpawnRates() {
		return new HashMap<Class<? extends Plant>, Float> ();
	}
}
