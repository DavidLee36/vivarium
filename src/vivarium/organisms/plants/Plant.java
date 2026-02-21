package vivarium.organisms.plants;

import vivarium.map.Hex;
import vivarium.organisms.Organism;

public class Plant extends Organism {
	public Plant(Hex hex) {
		super(hex);
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
		//TODO: remove plant from the plant array list in map
		super.kill();
	}
}
