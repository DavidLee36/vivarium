package vivarium.organisms;

import java.util.Random;

import vivarium.World;
import vivarium.map.Hex;
import vivarium.organisms.animals.Animal;
import vivarium.organisms.plants.Plant;
import vivarium.utils.Helpers;

public abstract class Organism {
	protected Hex hex;
	private boolean alive = true;
	protected float health;
	protected float age = 0f;
	protected int id;
	protected int spawnTick;

	protected static int organismCount = 0;

	protected Organism(Hex hex) {
		this.hex = hex;
		organismCount++;
		this.id = organismCount;
		this.spawnTick = World.getTickCount();
	}

	public abstract float hurt(float damage);

	public abstract float heal(float health);

	public void kill() {
		this.alive= false;
	}

	public final boolean isAnimal(Organism organism) {
		return organism instanceof Animal;
	}

	public final boolean isPlant(Organism organism) {
		return organism instanceof Plant;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Organism)) return false;
		Organism other = (Organism) obj;
		return this.id == other.id;
	}

	/**
	 * Set the current health to a random float between low and high
	 * @param low
	 * @param high
	 * @return health
	 */
	protected float setHealth(float low, float high) {
		Random rng = new Random();
		float rHealth = rng.nextFloat(low, high+0.01f);
		rHealth = Helpers.roundFloat(rHealth);
		this.health = rHealth;
		return health;
	}

	protected void setHealth(float health) {
		this.health = health;
	}

	//#region Getters and Setters

	public final float getHealth() { return health; }

	//#endregion
}
