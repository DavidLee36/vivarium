package vivarium.organisms.plants;

import vivarium.map.Hex;
import vivarium.organisms.Organism;

public class Plant extends Organism {
	public Plant(Hex hex, float maxHealth) {
		super(hex, maxHealth);
	}

	@Override
	public final void die() {
		//TODO: remove plant from the plant array list in map
		super.die();
	}
}
