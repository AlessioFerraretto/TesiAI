import java.util.Random;

public class RandomSingleton {

	private final static long SEED = 1;
	private RandomSingleton() {}
	
	private static Random r;
	
	static {
		 r = new Random();
		 r.setSeed(SEED);
	}
	
	public static double random() {
		return r.nextDouble();
	}
	
	public static double randDouble(float from, float to) {
		return r.nextDouble() * (to - from) + from;
	}

	public static float randFloat(float from, float to) {
		return r.nextFloat() * (to - from) + from;

	}

}
