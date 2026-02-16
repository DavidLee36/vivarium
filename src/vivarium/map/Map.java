package vivarium.map;

import java.util.Collection;
import java.util.HashMap;

public class Map {
	private int radius;
	private HashMap<String, Hex> hexes;

	public Map(int radius) {
		this.radius = radius;
		this.hexes = new HashMap<>();
		generate();
	}

	private void generate() {
		for(int q = -radius; q <= radius; q++) {
			for(int r = -radius; r <= radius; r++) {
				if(Math.abs(q + r) <= radius) {
					String key = q + "," + r;
					hexes.put(key, new Hex(q, r));
				}
			}
		}
	}

	/**
	 * Print the distance from each hex to origin
	 */
	public void printDistances() {
		for (Hex hex : getHexes()) {
			System.out.println("Distance from " + hex.getQ() + ", " + hex.getR() + ": " + hex.distanceTo(getHex(0, 0)));
		}
	}

	public Collection<Hex> getHexes() {
		return hexes.values();
	}

	public Hex getHex(int q, int r) {
		return hexes.get(q + "," + r);
	}
}
