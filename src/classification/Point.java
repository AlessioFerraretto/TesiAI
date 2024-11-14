package classification;

import java.awt.Color;

public class Point {

	private Color type;
	private int x,y;

	public Point(int x, int y, Color type) {
		setX(x);
		setY(y);
		setType(type);
	}
	
	public Color getType() {
		return type;
	}
	public void setType(Color type) {
		this.type = type;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}



}
