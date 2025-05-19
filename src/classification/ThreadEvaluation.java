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
	private ArrayList<Point> array;
	private NeuralNetwork nn;

	public ThreadEvaluation(int y, ArrayList<Point> array, NeuralNetwork nn) {
		super();
		this.y = y;
		this.array = array;
		this.nn = nn;
	}

	public void run() {
		try {
			Input inY = new Input(y, InputType.CLASSIFICATION);
			for(int x=0;x<Panel.DIMENSION;x+=Frame.GRANULARITY) {
				Input inX = new Input(x, InputType.CLASSIFICATION);

				Float[] out = null;

				out = nn.feedForward(inX, inY);

				Color color;
				int r = Math.min((int) (255*out[0]),255);
				int g = Math.min((int) (255*out[1]),255);
				int b = Math.min((int) (255*out[2]),255);
				color = new Color(r,g,b,255);

				synchronized (array) {
					array.add(new Point(color,x,y));
				}
			}
		} catch (NeuralNetworkException e) {
			e.printStackTrace();
		}
	}
	
	
}
