package vivarium.organisms.animals;

import vivarium.map.Hex;
import vivarium.organisms.Organism;

public abstract class Animal extends Organism {
	public Animal(Hex hex, float maxHealth) {
		super(hex, maxHealth);
	}

	@Override
	public final void die() {
		//TODO: remove animal from the animal array list in map
		super.die();
	}
}