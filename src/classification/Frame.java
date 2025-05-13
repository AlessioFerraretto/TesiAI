package classification;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import common.Point;
import common.ScreenshotTaker;
import neuralNetwork.ActivationFunctionType;
import neuralNetwork.Input;
import neuralNetwork.InputType;
import neuralNetwork.NeuralNetwork;
import neuralNetwork.NeuralNetworkBuilder;
import neuralNetwork.NeuralNetworkSettings;


public class Frame extends JFrame implements RepaintListener, TrainListener {


	public static final int EPOCHS = 200, EVALUATION_INTERVAL = 50;
	public static final int WIDTH = Panel.DIMENSION + 300, HEIGHT = Panel.DIMENSION + 40, GRANULARITY=1;
	private Panel mainPanel;
	private ButtonPanel buttonPanel;

	NeuralNetwork nn;
	int IN = 2, OUT = 3;

	public Frame() {
		super("Classification");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = (int) screenSize.getWidth();
		int h = (int) screenSize.getHeight();

		setBounds((w-WIDTH) / 2, (h-HEIGHT) / 2, WIDTH, HEIGHT);

		setLayout(new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));		

		mainPanel = new Panel(this);
		add(mainPanel);

		buttonPanel = new ButtonPanel(this);
		add(buttonPanel);

		setVisible(true);

		NeuralNetworkSettings.setUseInertia(true);
		//		NeuralNetworkSettings.setUseDropout(true);
		NeuralNetworkSettings.setDropoutRate(0.01f);

		nn = NeuralNetworkBuilder.Builder()
				.input(IN)
				.hidden(12, ActivationFunctionType.GELU)
				.hidden(12, ActivationFunctionType.GELU)
				.output(OUT, ActivationFunctionType.SIGMOID)
				.build();

		//		ArrayList<Point> testPoints = new ArrayList<Point>();
		//				aggiungiCirconferenza(arr, 10, 5, Color.green);
		//				aggiungiCirconferenza(arr, 75, 10, Color.red);
		//				aggiungiCirconferenza(arr, 150, 15, Color.blue);
		//				aggiungiCirconferenza(arr, 220, 20, Color.green);
		//		aggiungiCirconferenza(arr, 300, 30, Color.blue);
		//		mainPanel.setPoints(testPoints);
	}

	private void aggiungiCirconferenza(ArrayList<Point> arr, int r, int N, Color c) {
		for(int i=0;i<N;i++) {
			float ang = (float) (((2*Math.PI) / N) * i);
			arr.add(new Point(c,(int) (Panel.DIMENSION/2+r*Math.cos(ang)),(int) (Panel.DIMENSION/2+r*Math.sin(ang))));	
		}
	}

	@Override
	public void repaint() {
		super.repaint();
	}

	@Override
	public void train() {
		ArrayList<Point> points = mainPanel.getPoints();
		long startTime = System.currentTimeMillis();

		try {
			for(int k=0;k<EPOCHS;k++) {
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

				if(k%EVALUATION_INTERVAL == 0) {
					printProgressBar(startTime, k, EPOCHS);
					feedForward();
					ScreenshotTaker.take();
				}
			}

		} catch(Exception e) {
			e.printStackTrace();
		}

		mainPanel.setEditable(true);


	}

	public void feedForward() {
		try {
			//evaluating
			ArrayList<Point> determinedPoints = new ArrayList<Point>();
			for(int x=0;x<Panel.DIMENSION;x+=GRANULARITY) {
				for(int y=0;y<Panel.DIMENSION;y+=GRANULARITY) {
					Input inX = new Input(x, InputType.CLASSIFICATION);
					Input inY = new Input(y, InputType.CLASSIFICATION);

					Float[] out;

					out = nn.feedForward(inX, inY);


					Color color;
					int r = Math.min((int) (255*out[0]),255);
					int g = Math.min((int) (255*out[1]),255);
					int b = Math.min((int) (255*out[2]),255);
					color = new Color(r,g,b,255);

					determinedPoints.add(new Point(color,x,y));
				}
			}
			mainPanel.setPredictedPoints(determinedPoints);

			repaint();

		}catch(Exception e) {
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
