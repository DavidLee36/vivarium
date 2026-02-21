package vivarium.map;

import java.util.ArrayList;

import vivarium.map.biomes.*;
import vivarium.organisms.animals.Animal;
import vivarium.utils.Logger;

public class Hex {
    private HexCoordinate coordinate;
	private Biome biome;
	private ArrayList<Animal> animals = new ArrayList<>();

    protected Hex(int q, int r) {
        coordinate = new HexCoordinate(q, r);
		biome = BiomeType.random().create();
    }

	public int distanceTo(Hex hex) {
		int qChange = Math.abs(this.getQ() - hex.getQ());
		int rChange = Math.abs(this.getR() - hex.getR());
		int sChange = Math.abs(this.getS() - hex.getS());

		return (qChange + rChange + sChange) / 2;
	}

	public Animal spawnRandomAnimal() {
		Class<? extends Animal> animalClass = biome.getRandomAnimalClass();
		try {
			Animal animal = animalClass.getDeclaredConstructor(Hex.class).newInstance(this);
			this.animals.add(animal);
			return animal;
		} catch (Exception e) {
			System.out.println("Error spawning new animal:");
			e.printStackTrace();
			return null;
		}
	}

	public void log() {
		Logger.logLine();
		Logger.log("Hex: " + coordinate.toString());
		biome.log();
		Logger.log("");
		Logger.log("Animal count: " + animals.size());
		for (Animal animal : animals) {
			animal.log();
		}
		Logger.logLine();
	}

	//#region Getters and Setters

	public Biome getBiome() { return biome; }
	public HexCoordinate getHexCoordinate() { return coordinate; }

    public int getQ() { return coordinate.q; }
    public int getR() { return coordinate.r; }
	public int getS() { return coordinate.s; }

    public float getCenterX(float size) {
        return size * 1.5f * getQ();
    }

    public float getCenterY(float size) {
        return size * (float) Math.sqrt(3) * (getR() + getQ() / 2.0f);
    }

	//#endregion
}

