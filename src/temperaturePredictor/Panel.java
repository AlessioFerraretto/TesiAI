package temperaturePredictor;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Panel extends JPanel  {

	public static final int DIMENSION = 700, SIZE = 6, PREDICTED_SIZE = 6, OFFSET_X = 20, OFFSET_Y = 20;

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
		g.fillRect(0, 0, DIMENSION+OFFSET_X*2, DIMENSION+OFFSET_Y*2);

		g.setColor(Color.BLACK);

		int deltaHours = 24;
		int deltaDegrees = Temperature.MAX_TEMP-Temperature.MIN_TEMP;
		int spazioYPerOre = 12;
		int spazioXPerGradi = -15;

		g.setColor(Color.black);
		for(int h=0;h<=deltaHours;h++) {
			g.drawLine(h*DIMENSION/deltaHours +OFFSET_X, 0+OFFSET_Y, h*DIMENSION/deltaHours+OFFSET_X, DIMENSION+OFFSET_Y);
			g.drawString(h+"", h*DIMENSION/deltaHours+OFFSET_X, Panel.DIMENSION+OFFSET_Y+spazioYPerOre);
		}

		for(int t=0;t<=deltaDegrees;t++) {
			g.drawLine(0+OFFSET_X, t*DIMENSION/deltaDegrees+OFFSET_Y, DIMENSION+OFFSET_X, t*DIMENSION/deltaDegrees+OFFSET_Y);
			g.drawString(Temperature.MAX_TEMP-t+"", 0+OFFSET_X+spazioXPerGradi, t*DIMENSION/deltaDegrees+OFFSET_Y);
		}

		g.setColor(Color.blue);
		for(Temperature p : temps) {
			int x = p.getTime() * Panel.DIMENSION / Temperature.MAX_TIME;
			int y = (int) ((Temperature.MAX_TEMP - p.getValue()) * Panel.DIMENSION/deltaDegrees);
			g.fillRect(x-SIZE/2 +OFFSET_X, y-SIZE/2 +OFFSET_Y, SIZE, SIZE);
		}

		g.setColor(Color.red);
		for(Temperature p : predictedTemps) {
			int x = p.getTime() * Panel.DIMENSION / Temperature.MAX_TIME;
			int y = (int) ((Temperature.MAX_TEMP - p.getValue()) * Panel.DIMENSION/deltaDegrees);
			g.fillOval(x-PREDICTED_SIZE/2 +OFFSET_X, y-PREDICTED_SIZE/2 +OFFSET_Y, PREDICTED_SIZE, PREDICTED_SIZE);
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
