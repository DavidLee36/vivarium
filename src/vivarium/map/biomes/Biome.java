package vivarium.map.biomes;

import java.util.Random;
import vivarium.utils.*;

import com.badlogic.gdx.graphics.Color;

public abstract class Biome {
	private float minTemp;
	private float maxTemp;
	private float currTemp;
	private float minMoisture;
	private float maxMoisture;
	private float currMoisture;
	private Color color;

	protected Biome(float minTemp, float maxTemp, float minMoisture, float maxMoisture, Color color) {
		this.minTemp = minTemp;
		this.maxTemp = maxTemp;
		this.minMoisture = minMoisture;
		this.maxMoisture = maxMoisture;
		this.color = color;

		Random rng = new Random();
		this.currTemp = Helpers.roundFloat(rng.nextFloat(minTemp, maxTemp));
		this.currMoisture = Helpers.roundFloat(rng.nextFloat(minMoisture, maxMoisture));
	}

	public Color getColor() { return color; }
}