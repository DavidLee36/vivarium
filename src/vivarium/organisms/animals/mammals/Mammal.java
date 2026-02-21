package vivarium.organisms.animals.mammals;

import vivarium.map.Hex;
import vivarium.organisms.animals.Animal;

public abstract class Mammal extends Animal {
	
	public Mammal(Hex hex) {
		super(hex, true);
	}
}
