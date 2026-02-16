package vivarium.utils;

public class Helpers {

	/**
	 * Round to the nearest hundredth
	 * @param num number to round
	 * @return num rounded to the nearest hundredth
	 */
	public static float roundFloat(float num) {
		return Math.round(num * 100) / 100;
	}
}
