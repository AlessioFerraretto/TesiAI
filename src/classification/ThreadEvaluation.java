package classification;

import java.awt.Color;
import java.util.ArrayList;

import common.Point;
import neuralNetwork.Input;
import neuralNetwork.InputType;
import neuralNetwork.NeuralNetwork;
import neuralNetwork.NeuralNetworkException;

public class ThreadEvaluation extends Thread {

	private int y;
	private ArrayList<Point> predictedPoints;
	private NeuralNetwork nn;

	public ThreadEvaluation(int y, ArrayList<Point> predictedPoints, NeuralNetwork nn) {
		super();
		this.y = y;
		this.predictedPoints = predictedPoints;
		this.nn = nn;
	}

	public void run() {
		Input inX;
		Float[] predicted = null;
		Color color;

		try {
			Input inY = new Input(y, InputType.CLASSIFICATION);
			for(int x=0;x<Panel.DIMENSION;x+=Frame.GRANULARITY) {
				inX = new Input(x, InputType.CLASSIFICATION);

				predicted = nn.feedForward(inX, inY);

				int r = Math.min((int) (255*predicted[0]),255);
				int g = Math.min((int) (255*predicted[1]),255);
				int b = Math.min((int) (255*predicted[2]),255);
				color = new Color(r,g,b,255);

				synchronized (predictedPoints) {
					predictedPoints.add(new Point(color,x,y));
				}
			}
			
		} catch (NeuralNetworkException e) {
			e.printStackTrace();
		}
	}
	
	
}
