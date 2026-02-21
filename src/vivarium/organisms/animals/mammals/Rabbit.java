package vivarium.organisms.animals.mammals;

import vivarium.map.Hex;

public class Rabbit extends Mammal {
	public Rabbit(Hex hex) {
		super(hex);
		setHealth(30f, 50f);
	}
}
