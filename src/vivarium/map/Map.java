package vivarium.map;

import java.util.Collection;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector3;

import vivarium.Vivarium;
import vivarium.World;

public class Map {
	private int radius;
	private HashMap<HexCoordinate, Hex> hexes;

	public Map(int radius) {
		this.radius = radius;
		this.hexes = new HashMap<>();
		generate();
	}

	private void generate() {
		for (int q = -radius; q <= radius; q++) {
			for (int r = -radius; r <= radius; r++) {
				if (Math.abs(q + r) <= radius) {
					Hex hex = new Hex(q, r);
					hexes.put(hex.getHexCoordinate(), hex);
				}
			}
		}
	}

	/**
	 * Print the distance from each hex to origin
	 */
	public void printDistances() {
		for (Hex hex : getHexes()) {
			System.out.println("Distance from " + hex.getQ() + ", " + hex.getR() + ": " + hex.distanceTo(getHexAt(0, 0)));
		}
	}

	public Collection<Hex> getHexes() {
		return hexes.values();
	}

	public Hex getHexAt(int q, int r) {
		return hexes.get(new HexCoordinate(q, r));
	}

	public Hex getHexAt(Vector3 worldCoord) {
		float hexSize = Vivarium.getHexSize();
		float fq = (2f / 3f) * (worldCoord.x / hexSize);
		float fr = (-1f / 3f) * (worldCoord.x / hexSize) + ((float) Math.sqrt(3) / 3f) * (worldCoord.y / hexSize);
		float fs = -fq - fr;

		int rq = Math.round(fq);
		int rr = Math.round(fr);
		int rs = Math.round(fs);

		float dq = Math.abs(rq - fq);
		float dr = Math.abs(rr - fr);
		float ds = Math.abs(rs - fs);

		if (dq > dr && dq > ds) {
			rq = -rr - rs;
		} else if (dr > ds) {
			rr = -rq - rs;
		} else {
			rs = -rq - rr;
		}

		return getHexAt(rq, rr);
	}
}
