package common;

public class NumberController {

	public static final int MAX = 25, MIN = -MAX;
	
	public static float check(float number, int errorNumber) {
		if(number > MAX) {
			return MAX;
		}

		if(number < MIN) {
			return MIN;
		}
		
		if(Float.isNaN(number)) {
			System.out.println("errore " + errorNumber);
			System.exit(0);
		}
		
		return number;
	}

}
