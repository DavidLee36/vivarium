package vivarium.map;

import vivarium.map.biomes.*;

public class Hex {
    private int q;
    private int r;
	private int s;
	private Biome biome;

    public Hex(int q, int r) {
        this.q = q;
        this.r = r;
		this.s = -q-r;
		biome = BiomeType.random().create();
    }

	public int distanceTo(Hex hex) {
		int qChange = Math.abs(this.q - hex.q);
		int rChange = Math.abs(this.r - hex.r);
		int sChange = Math.abs(this.s - hex.s);

		return (qChange + rChange + sChange) / 2;
	}

    public int getQ() { return q; }
    public int getR() { return r; }
	public int getS() { return s; }

    public float getCenterX(float size) {
        return size * 1.5f * q;
    }

    public float getCenterY(float size) {
        return size * (float) Math.sqrt(3) * (r + q / 2.0f);
    }

	public Biome getBiome() { return biome; }
}

