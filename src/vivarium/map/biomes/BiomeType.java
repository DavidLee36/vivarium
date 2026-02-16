package vivarium.map.biomes;

import java.util.Random;

public enum BiomeType {
	FOREST,
	DESERT;

	private int count = 0;

	private static final Random rng = new Random();

	/**
	 * @return a new Biome of this BiomeType
	 */
	public Biome create() {
		return switch (this) {
			case FOREST -> {
				count++;
				yield new Forest();
			} case DESERT -> {
				count++;
				yield new Desert();
			}
		};
	}

	/**
	 * @return a random biome type
	 */
	public static BiomeType random() {
		BiomeType[] types = values();
		return types[rng.nextInt(types.length)];
	}

	public static BiomeType[] getAllTypes() {
		return values();
	}

	/**
	 * @return total count of this biome
	 */
	public int getCount() {
		return count;
	}
}
