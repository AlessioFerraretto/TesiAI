package predictor;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import neuralNetwork.Input;
import neuralNetwork.InputType;

public class Panel extends JPanel  {

	public static final int DIMENSION = 700, SIZE = 6, PREDICTED_SIZE = 6, OFFSET_X = 60, OFFSET_Y = 60;

	private ArrayList<DataPoint> dataValues;
	private ArrayList<DataPoint> predictedTemps;
	private float mae, mse;

	public Panel() {
		super();

		dataValues = new ArrayList<DataPoint>();
		predictedTemps = new ArrayList<DataPoint>();
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setFont(new Font("Arial", Font.PLAIN, 25));

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, DIMENSION+OFFSET_X*2, DIMENSION+OFFSET_Y*2);

		g.setColor(Color.BLACK);

		int deltaHours = 24;
		VisualizationType visualizationType = dataValues.get(0).getVisualizationType();
		int deltaValue = (int) ((visualizationType.getMaxValue()-visualizationType.getMinValue()) /visualizationType.getUnit());
		int spazioYPerOre = 30;
		int spazioXPerGradi = -40;

		g.setColor(Color.black);
		for(int h=0;h<=deltaHours;h++) {
			g.drawLine(h*DIMENSION/deltaHours +OFFSET_X, 0+OFFSET_Y, h*DIMENSION/deltaHours+OFFSET_X, DIMENSION+OFFSET_Y);
			
			if(h%6==0) {
			g.drawString(h+"", h*DIMENSION/deltaHours+OFFSET_X, Panel.DIMENSION+OFFSET_Y+spazioYPerOre);
			}
		}

		for(int t=0;t<=deltaValue;t++) {
			g.drawLine(0+OFFSET_X, t*DIMENSION/deltaValue+OFFSET_Y, DIMENSION+OFFSET_X, t*DIMENSION/deltaValue+OFFSET_Y);

			if(t%5==0) {
				g.drawString((int) (visualizationType.getMaxValue()-t*visualizationType.getUnit())+visualizationType.getSymbol(), 0+OFFSET_X+spazioXPerGradi, t*DIMENSION/deltaValue+OFFSET_Y);
			}
		}

		g.setColor(Color.blue);
		for(DataPoint p : dataValues) {
			int x = p.getTime() * Panel.DIMENSION / DataPoint.MAX_TIME;
			int y = (int) ( Panel.DIMENSION - (((p.getValue() - p.getVisualizationType().getMinValue()) / p.getVisualizationType().getDelta()) * Panel.DIMENSION));
			g.fillRect(x-SIZE/2 +OFFSET_X, y-SIZE/2 +OFFSET_Y, SIZE, SIZE);
		}

		g.setColor(Color.red);
		for(DataPoint p : predictedTemps) {
			int x = p.getTime() * Panel.DIMENSION / DataPoint.MAX_TIME;
			int y = (int) ( Panel.DIMENSION - (((p.getValue() - p.getVisualizationType().getMinValue()) / p.getVisualizationType().getDelta()) * Panel.DIMENSION));
			g.fillOval(x-PREDICTED_SIZE/2 +OFFSET_X, y-PREDICTED_SIZE/2 +OFFSET_Y, PREDICTED_SIZE, PREDICTED_SIZE);
		}

		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.BOLD, 50));
		g.drawString("MAE: " + String.format("%.4f", mae) + " MSE: " + String.format("%.4f", mse), OFFSET_X + 20, OFFSET_Y + 20);

	}

	public ArrayList<DataPoint> getDataValues() {
		return dataValues;
	}

	public void setPredictedTemps(ArrayList<DataPoint> predictedTemps) {
		this.predictedTemps = predictedTemps;
	}

	public void setDataValues(ArrayList<DataPoint> dataValues) {
		this.dataValues = dataValues;
	}

	public void setMae(float mae) {
		this.mae = mae;
	}
	public void setMse(float mse) {
		this.mse = mse;
	}


}
