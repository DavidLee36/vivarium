package vivarium.organisms;

import vivarium.map.Hex;
import vivarium.organisms.animals.Animal;
import vivarium.organisms.plants.Plant;

public abstract class Organism {
	protected Hex hex;
	private boolean alive = true;
	protected float currHealth;

	protected Organism(Hex hex, float maxHealth) {
		this.hex = hex;
		this.currHealth = maxHealth;
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

	//#region

	public final float getHealth() { return currHealth; }

	//#endregion
}
