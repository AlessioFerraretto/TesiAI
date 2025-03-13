package temperaturePredictor;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Panel extends JPanel  {

	public static final int DIMENSION = 700, SIZE = 8, PREDICTED_SIZE = 6;
	
	private ArrayList<Temperature> temps;
	private ArrayList<Temperature> predictedTemps;

	public Panel() {
		super();
		
		temps = new ArrayList<Temperature>();
		predictedTemps = new ArrayList<Temperature>();
	}

	public void paint(Graphics g) {
		super.paint(g);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, DIMENSION, DIMENSION);
		
		g.setColor(Color.BLACK);

		int deltaHours = 24;
		int deltaDegrees = Temperature.MAX_TEMP-Temperature.MIN_TEMP;
		
		for(int h=0;h<=deltaHours;h++) {
			g.drawLine(h*DIMENSION/deltaHours, 0, h*DIMENSION/deltaHours, DIMENSION);
		}
		
		for(int t=0;t<=deltaDegrees;t++) {
			g.drawLine(0, t*DIMENSION/deltaDegrees, DIMENSION, t*DIMENSION/deltaDegrees);
		}
		
		g.setColor(Color.blue);
		for(Temperature p : temps) {
			int x = p.getTime() * Panel.DIMENSION / Temperature.MAX_TIME;
			int y = (int) ((Temperature.MAX_TEMP - p.getValue()) * Panel.DIMENSION/deltaDegrees);
			g.fillRect(x-SIZE/2, y-SIZE/2, SIZE, SIZE);
		}
	
		g.setColor(Color.red);
		for(Temperature p : predictedTemps) {
			int x = p.getTime() * Panel.DIMENSION / Temperature.MAX_TIME;
			int y = (int) ((Temperature.MAX_TEMP - p.getValue()) * Panel.DIMENSION/deltaDegrees);
			g.fillOval(x-PREDICTED_SIZE/2, y-PREDICTED_SIZE/2, PREDICTED_SIZE, PREDICTED_SIZE);
		}
	}

	public ArrayList<Temperature> getTemps() {
		return temps;
	}
	
	public void setPredictedTemps(ArrayList<Temperature> predictedTemps) {
		this.predictedTemps = predictedTemps;
	}

	public void setTemps(ArrayList<Temperature> temps) {
		this.temps = temps;
	}
	

}
