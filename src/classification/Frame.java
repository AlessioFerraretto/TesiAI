package classification;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import common.Point;
import common.ScreenshotTaker;
import neuralNetwork.ActivationFunctionType;
import neuralNetwork.Input;
import neuralNetwork.InputType;
import neuralNetwork.NeuralNetwork;
import neuralNetwork.NeuralNetworkBuilder;
import neuralNetwork.NeuralNetworkException;
import neuralNetwork.NeuralNetworkSettings;


public class Frame extends JFrame implements RepaintListener, TrainListener {

	public static final int EPOCHS = 10000, EVALUATION_INTERVAL = 200;
	public static final int WIDTH = Panel.DIMENSION + 300, HEIGHT = Panel.DIMENSION + 60, GRANULARITY=1;
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

		NeuralNetworkSettings.setUseDropout(true);
		NeuralNetworkSettings.setDropoutRate(0.2f);
		NeuralNetworkSettings.setUseInertia(true);

		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("inputsEsempio2.dat"));
			mainPanel.setPoints((ArrayList<Point>) ois.readObject());
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		repaint();

		nn = NeuralNetworkBuilder.Builder()
				.input(IN)
				.hidden(4, ActivationFunctionType.GELU)
				.hidden(4, ActivationFunctionType.GELU)
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
		mainPanel.setEditable(false);
		java.awt.Point locationOnScreen = mainPanel.getLocationOnScreen();
		Rectangle r = new Rectangle(locationOnScreen, new Dimension(Panel.DIMENSION,Panel.DIMENSION));
		ScreenshotTaker.take(r);

		ArrayList<Point> points = mainPanel.getPoints();
		long startTime = System.currentTimeMillis();

		for(int k=0;k<EPOCHS;k++) {
			Input[] in = new Input[nn.getIn()];
			Float[] out = new Float[nn.getOut()];
			Float[] predicted = null;

			float mae = 0, mse = 0;
			for(int j=0;j<points.size();j++) {
				in[0] = new Input(points.get(j).getInput(0), InputType.CLASSIFICATION);
				in[1] = new Input(points.get(j).getInput(1), InputType.CLASSIFICATION);

				out[0] = points.get(j).getType().equals(Color.RED) ? 1f : 0;
				out[1] = points.get(j).getType().equals(Color.GREEN) ? 1f : 0;
				out[2] = points.get(j).getType().equals(Color.BLUE) ? 1f : 0;

				try {
					predicted = nn.train(in, out);
				} catch (NeuralNetworkException e) {
					e.printStackTrace();
				}

				mae += NeuralNetwork.calculateMAE(out, predicted);
				mse += NeuralNetwork.calculateMSE(out, predicted);
			}
			mae /= points.size();
			mse /= points.size();

			printProgressBar(startTime, k%EVALUATION_INTERVAL, EVALUATION_INTERVAL);

			if(k%EVALUATION_INTERVAL == 0) {

				if(k!=0) {
					mainPanel.setMae(mae);
					mainPanel.setMse(mse);
					feedForward();

				}
				//					printProgressBar(startTime, k, EPOCHS);

			}
		}

		printProgressBar(startTime, EPOCHS, EPOCHS);
		feedForward();


		mainPanel.setEditable(true);

	}

	public void feedForward() {
		try {
			ArrayList<Point> determinedPoints = new ArrayList<Point>();
			ArrayList<Thread> threads = new ArrayList<Thread>();
			for(int y=0;y<Panel.DIMENSION;y+=GRANULARITY) {
				Thread t = new ThreadEvaluation(y, determinedPoints, nn.deepCopy());
				t.start();
				threads.add(t);
			}
			for(Thread t : threads) {
				t.join();
			}


			mainPanel.setPredictedPoints(determinedPoints);

			repaint();

			Thread.sleep(1000);
			//wait for the repaint to apply
			SwingUtilities.invokeLater(() -> {
				java.awt.Point locationOnScreen = mainPanel.getLocationOnScreen();
				Rectangle r = new Rectangle(locationOnScreen, new Dimension(Panel.DIMENSION,Panel.DIMENSION));
				ScreenshotTaker.take(r);
			});



		}catch(Exception e) {
			e.printStackTrace();
		}

	}

	public ArrayList<Point> getInputs() {
		return mainPanel.getPoints();
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
