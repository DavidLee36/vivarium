package vivarium.organisms.animals;

import java.util.ArrayList;

import vivarium.map.Hex;
import vivarium.organisms.Organism;
import vivarium.utils.Logger;

public abstract class Animal extends Organism {
	protected boolean warmBlooded;
	protected ArrayList<Organism> prey = new ArrayList<>();
	protected ArrayList<Organism> killedPrey = new ArrayList<>();
	protected float stamina = 100f;
	protected float hunger = 95f;
	protected float baseDamage = 30f;

	public Animal(Hex hex, boolean warmBlooded) {
		super(hex);
		this.warmBlooded = warmBlooded;
	}

	@Override
	public float heal(float health) {
		this.health += health;
		return this.health;
	}

	@Override
	public float hurt(float damage) {
		this.health -= damage;
		if (this.health <= 0f) {
			kill();
			this.health = 0;
		}
		return this.health;
	}

	@Override
	public final void kill() {
		//TODO: remove animal from the animal array list in map
		super.kill();
	}

	/**
	 * Eat prey, if the prey is a plant, deal damage. If the prey is an animal, kill it
	 * @param prey prey to eat
	 * @return true if the prey is dead else false
	 */
	public final boolean eat(Organism prey) {
		if (prey.isPlant(prey)) {
			if(prey.hurt(baseDamage) <= 0) {
				killedPrey.add(prey);
				return true;
			}
			return false;
		}else {
			prey.hurt(prey.getHealth());
			killedPrey.add(prey);
			return true;
		}
	}

	public void log() {
		Logger.log("Animal: " + this.getClass().getSimpleName());
		Logger.log("Spawned on tick: " + spawnTick);
		Logger.log("Health: " + health);
		Logger.log("Hunger: " + hunger);
	}

	//#region Getters and Setters
	
	public ArrayList<Organism> getPrey() {
		return prey;
	}

	public float getStamina() {
		return stamina;
	}

	public float getHunger() {
		return hunger;
	}

	//#endregion
}