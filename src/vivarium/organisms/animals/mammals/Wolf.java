package vivarium.organisms.animals.mammals;

import vivarium.map.Hex;

public class Wolf extends Mammal {

	public Wolf(Hex hex) {
		super(hex);
		setHealth(75f, 120f);
	}
	
}
