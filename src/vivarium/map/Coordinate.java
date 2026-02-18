package vivarium.map;

public class Coordinate {
	protected int q;
	protected int r;
	protected int s;

	protected Coordinate(int q, int r) {
		this.q = q;
        this.r = r;
		this.s = -q-r;
	}

	protected Coordinate(int q, int r, int s) {
		this.q = q;
        this.r = r;
		this.s = s;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Coordinate)) return false;
		Coordinate other = (Coordinate) obj;
		return q == other.q && r == other.r && s == other.s;
	}

	@Override
	public int hashCode() {
		return 31 * q + r;
	}
}
