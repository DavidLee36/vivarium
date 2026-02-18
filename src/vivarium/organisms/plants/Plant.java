package vivarium.organisms.plants;

import vivarium.map.Hex;
import vivarium.organisms.Organism;

public class Plant extends Organism {
	public Plant(Hex hex, float maxHealth) {
		super(hex, maxHealth);
	}

	@Override
	public float heal(float health) {
		currHealth += health;
		if (currHealth > maxHealth) currHealth = maxHealth;
		return currHealth;
	}

	@Override
	public float hurt(float damage) {
		currHealth -= damage;
		if (currHealth <= 0f) {
			die();
			currHealth = 0;
		}
		return currHealth;
	}

	@Override
	public final void die() {
		//TODO: remove plant from the plant array list in map
		super.die();
	}
}
