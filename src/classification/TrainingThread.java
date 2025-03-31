package classification;

import java.awt.Color;
import java.util.ArrayList;

import common.Point;
import neuralNetwork.Input;
import neuralNetwork.InputType;
import neuralNetwork.NeuralNetwork;

public class TrainingThread extends Thread {

	private ArrayList<Point> points;
	private int EPOCHS;
	private NeuralNetwork nn;
	private EvaluateInterface e;

	public TrainingThread(EvaluateInterface e, ArrayList<Point> points, int EPOCHS, NeuralNetwork nn) {
		this.e = e;
		this.points = points;
		this.EPOCHS = EPOCHS;
		this.nn = nn;
	}

	public void run() {
		int N = EPOCHS;
		long startTime = System.currentTimeMillis();

		try {

			//testing 
			for(int k=0;k<N;k++) {
				Input[] in = new Input[nn.getIn()];
				Float[] out = new Float[nn.getOut()];

				for(int j=0;j<points.size();j++) {
					in[0] = new Input(points.get(j).getInput(0), InputType.CLASSIFICATION);
					in[1] = new Input(points.get(j).getInput(1), InputType.CLASSIFICATION);

					out[0] = points.get(j).getType().equals(Color.RED) ? 1f : 0;
					out[1] = points.get(j).getType().equals(Color.GREEN) ? 1f : 0;
					out[2] = points.get(j).getType().equals(Color.BLUE) ? 1f : 0;

					nn.train(in, out);
				}

				if(k%50 == 0) {
					printProgressBar(startTime, k, N);
					e.evaluate();
					//					evaluate();
				}
			}
			System.out.println();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}



	private void printProgressBar(long startTime, int i, long total) {
		clearLine();
		printPB((int) ((i * 100.0) / total), ((System.currentTimeMillis() - startTime) * total) / (i + 1) - (System.currentTimeMillis() - startTime));

	}

	private void printPB(int percent, long remainingTimeMillis) {
		int barLength = 50;
		int filledLength = (percent * barLength) / 100;

		StringBuilder bar = new StringBuilder();
		bar.append("[");
		for (int j = 0; j < barLength; j++) {
			if (j < filledLength) {
				bar.append("=");
			} else {
				bar.append(" ");
			}
		}
		bar.append("] ");

		System.out.print("\r" + bar + percent + "% - Remaining time: " + remainingTimeMillis / 1000 + "s");
	}

	private static void clearLine() {
		System.out.print("\033[F");  // Move cursor up one line
		System.out.print("\033[2K"); // Clear the entire line
	}
}
