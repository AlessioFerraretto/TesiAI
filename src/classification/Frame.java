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
import common.ProgressBarUtil;
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

		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("inputsEsempio3.dat"));
			mainPanel.setPoints((ArrayList<Point>) ois.readObject());
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		NeuralNetworkSettings.setUseDropout(true);
		NeuralNetworkSettings.setDropoutRate(0.1f);
		NeuralNetworkSettings.setUseInertia(true);

		nn = NeuralNetworkBuilder.Builder()
				.input(IN)
				.hidden(12, ActivationFunctionType.GELU)
				.hidden(12, ActivationFunctionType.GELU)
				.output(OUT, ActivationFunctionType.SIGMOID)
				.build();
	}

	@Override
	public void repaint() {
		super.repaint();
	}

	@Override
	public void train() {
		mainPanel.setEditable(false);

		takeScreenshot();

		ArrayList<Point> points = mainPanel.getPoints();
		long startTime = System.currentTimeMillis();

		for(int k=0;k<EPOCHS;k++) {
			Input[] in = new Input[nn.getIn()];
			Float[] out = new Float[nn.getOut()];

			for(int j=0;j<points.size();j++) {
				in[0] = new Input(points.get(j).getInput(0), InputType.CLASSIFICATION);
				in[1] = new Input(points.get(j).getInput(1), InputType.CLASSIFICATION);

				out[0] = points.get(j).getType().equals(Color.RED) ? 1f : 0;
				out[1] = points.get(j).getType().equals(Color.GREEN) ? 1f : 0;
				out[2] = points.get(j).getType().equals(Color.BLUE) ? 1f : 0;

				try {
					nn.train(in, out);
				} catch (NeuralNetworkException e) {
					e.printStackTrace();
				}
			}

			if(k%EVALUATION_INTERVAL == 0 && k!=0) {
				ProgressBarUtil.printProgressBar(startTime, k, EPOCHS);
				feedForwardAndPaint();
			}
		}

		ProgressBarUtil.printProgressBar(startTime, EPOCHS, EPOCHS);
		feedForwardAndPaint();

		mainPanel.setEditable(true);

	}

	private void takeScreenshot() {
		java.awt.Point locationOnScreen = mainPanel.getLocationOnScreen();
		Rectangle r = new Rectangle(locationOnScreen, new Dimension(Panel.DIMENSION,Panel.DIMENSION));
		ScreenshotTaker.take(r);		
	}

	public void feedForwardAndPaint() {
		try {
			ArrayList<Point> predictedPoints = new ArrayList<Point>();
			ArrayList<Thread> threads = new ArrayList<Thread>();

			for(int y=0;y<Panel.DIMENSION;y+=GRANULARITY) {
				Thread t = new ThreadEvaluation(y, predictedPoints, nn.deepCopy());
				t.start();
				threads.add(t);
			}
			for(Thread t : threads) {
				t.join();
			}

			ArrayList<Point> points = mainPanel.getPoints();
			Float[] actual = new Float[OUT];
			Float[] predicted = new Float[OUT];
			Float mae = 0f, mse = 0f;
			int count = 0;
			for(int j=0;j<mainPanel.getPoints().size();j++) {
				for(int i=0;i<predictedPoints.size();i++) {
					if(mainPanel.getPoints().get(j).getInput(0) == predictedPoints.get(i).getInput(0) &&
							mainPanel.getPoints().get(j).getInput(1) == predictedPoints.get(i).getInput(1)) {
						
						actual[0] = points.get(j).getType().equals(Color.RED) ? 1f : 0;
						actual[1] = points.get(j).getType().equals(Color.GREEN) ? 1f : 0;
						actual[2] = points.get(j).getType().equals(Color.BLUE) ? 1f : 0;
						
						predicted[0] = predictedPoints.get(j).getType().getRed()/255f;
						predicted[1] = predictedPoints.get(j).getType().getGreen()/255f;
						predicted[2] = predictedPoints.get(j).getType().getBlue()/255f;
						
						mae += NeuralNetwork.calculateMAE(actual, predicted);
						mse += NeuralNetwork.calculateMSE(actual, predicted);
						count ++;
					}
				}
			}
			mae /= count;
			mse /= count;

			mainPanel.setPredictedPoints(predictedPoints);
			mainPanel.setMae(mae);
			mainPanel.setMse(mse);

			repaint();

			Thread.sleep(1000);
			//wait for the repaint to apply
			takeScreenshot();



		}catch(Exception e) {
			e.printStackTrace();
		}

	}

	public ArrayList<Point> getInputs() {
		return mainPanel.getPoints();
	}


}
