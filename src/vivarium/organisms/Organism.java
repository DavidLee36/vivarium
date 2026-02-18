package vivarium.organisms;

import vivarium.map.Hex;

public abstract class Organism {
	protected Hex hex;
	private boolean alive = true;
	protected float maxHealth;
	protected float currHealth;

	protected Organism(Hex hex, float maxHealth) {
		this.hex = hex;
		this.maxHealth = maxHealth;
		this.currHealth = maxHealth;
	}

	public void die() {
		this.alive= false;
	}
}
