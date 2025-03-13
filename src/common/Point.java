package common;

import java.awt.Color;

/**
 * 
 */
/**
 * 
 */
public class Point {

	private Color type;
	private int[] input;

	public Point(Color type, int... input) {
		setInput(input);
		setType(type);
	}
	
	public void setInput(int[] input) {
		this.input = input;
	}

	public int[] getInput() {
		return input;
	}
	
	public Color getType() {
		return type;
	}
	public void setType(Color type) {
		this.type = type;
	}

	
	/**
	 * @param dimensions, the number of dimensions of the input (x1, x2, ..., xn)
	 * Generates a point with n dimensions.
	 * Each dimension will have a random generated value from 0 to 1000.
	 * 
	 * @return
	 */
	public static Point generateRandomPoint(int dimensions) {
		int[] input = new int[dimensions];
		for(int i=0;i<dimensions;i++) {
			input[i] = RandomSingleton.randInt(0, 1000);
		}
		
		Color c = RandomSingleton.randBool() ? Color.red : Color.blue;
		return new Point(c, input);	
	}

	public float getInput(int i) {
		return input[i];
	}


}
