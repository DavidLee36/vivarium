package vivarium.organisms.animals;

import java.util.ArrayList;

import vivarium.map.Hex;
import vivarium.organisms.Organism;

public abstract class Animal extends Organism {
	protected boolean warmBlooded;
	protected ArrayList<Organism> prey;
	protected ArrayList<Organism> killedPrey;
	protected float currStamina;
	protected float currHunger;
	protected float baseDamage;

	public Animal(Hex hex, float health, boolean warmBlooded) {
		super(hex, health);
	}

	@Override
	public float heal(float health) {
		currHealth += health;
		return currHealth;
	}

	@Override
	public float hurt(float damage) {
		currHealth -= damage;
		if (currHealth <= 0f) {
			kill();
			currHealth = 0;
		}
		return currHealth;
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

	//#region Getters and Setters
	
	public ArrayList<Organism> getPrey() {
		return prey;
	}

	public float getStamina() {
		return currStamina;
	}

	public float getHunger() {
		return currHunger;
	}

	//#endregion
}