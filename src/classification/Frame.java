package classification;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import common.Point;
import common.RandomSingleton;
import neuralNetwork.ActivationFunctionType;
import neuralNetwork.Input;
import neuralNetwork.InputType;
import neuralNetwork.NeuralNetwork;
import neuralNetwork.NeuralNetworkBuilder;
import neuralNetwork.NeuralNetworkException;
import neuralNetwork.NeuralNetworkSettings;


public class Frame extends JFrame implements RepaintListener, TrainListener, EvaluateInterface {


	private static final int EPOCHS = 1000;
	public static int WIDTH = Panel.DIMENSION + 150, HEIGHT = Panel.DIMENSION + 40, GRANULARITY=1;
	private int x, y;
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
		x = (w-WIDTH) / 2;
		y = (h-HEIGHT) / 2;

		setBounds(x, y, WIDTH, HEIGHT);

		setLayout(new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));		

		mainPanel = new Panel(this);
		add(mainPanel);

		buttonPanel = new ButtonPanel(this);
		add(buttonPanel);

		setVisible(true);

		NeuralNetworkSettings.setUseInertia(true);
		NeuralNetworkSettings.setUseDropout(false);
		NeuralNetworkSettings.setDropoutRate(0.01f);

		nn = NeuralNetworkBuilder.Builder()
				.input(IN)
				.hidden(5, ActivationFunctionType.GELU)
				.hidden(5, ActivationFunctionType.GELU)
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


		TrainingThread tt = new TrainingThread(this, mainPanel.getPoints(), EPOCHS, nn);
		tt.start();
		
		evaluate();

			

	}

	@Override
	public void evaluate() {
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
		System.out.println("done");
		mainPanel.setPredictedPoints(determinedPoints);

		mainPanel.setEditable(true);
		repaint();
		System.out.println("repainted");	
		
		}catch(Exception e) {
			e.printStackTrace();
		}

	}

}
