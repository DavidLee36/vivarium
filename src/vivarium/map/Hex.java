package vivarium.map;

import vivarium.map.biomes.*;

public class Hex {
    private Coordinate coordinate;
	private Biome biome;

    protected Hex(int q, int r) {
        coordinate = new Coordinate(q, r);
		biome = BiomeType.random().create();
    }

	public int distanceTo(Hex hex) {
		int qChange = Math.abs(this.getQ() - hex.getQ());
		int rChange = Math.abs(this.getR() - hex.getR());
		int sChange = Math.abs(this.getS() - hex.getS());

		return (qChange + rChange + sChange) / 2;
	}

    public int getQ() { return coordinate.q; }
    public int getR() { return coordinate.r; }
	public int getS() { return coordinate.s; }

    public float getCenterX(float size) {
        return size * 1.5f * getQ();
    }

    public float getCenterY(float size) {
        return size * (float) Math.sqrt(3) * (getR() + getQ() / 2.0f);
    }

	public Biome getBiome() { return biome; }
	protected Coordinate getCoordinate() { return coordinate; }
}

