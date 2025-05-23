package predictor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.lang.runtime.TemplateRuntime;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import common.ProgressBarUtil;
import common.ScreenshotTaker;
import neuralNetwork.ActivationFunctionType;
import neuralNetwork.Input;
import neuralNetwork.InputType;
import neuralNetwork.NeuralNetwork;
import neuralNetwork.NeuralNetworkBuilder;
import neuralNetwork.NeuralNetworkException;
import neuralNetwork.NeuralNetworkSettings;

public class Frame extends JFrame {

	public final static int PADDING = 50, 
			WIDTH = Panel.DIMENSION + 17 + PADDING*2 + Panel.OFFSET_X*2, //
			HEIGHT = Panel.DIMENSION + 40 + PADDING*2 + Panel.OFFSET_Y*2, 
			GRANULARITY=10, EVALUATION_INTERVAL = 100, EPOCHS = 10000;

	private Panel mainPanel;
	private JPanel spacerN,spacerS,spacerW,spacerE;
	private static String FILE_SAVE = "save", FILE_EXTENSION = ".dat"; 
	
	private InputType inputType;
	private VisualizationType visualizationType;
	NeuralNetwork nn;
	int IN = 1, OUT = 1;

	public Frame(ArrayList<DataPoint> dataValues) {
		super("Temperature Prediction");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = (int) screenSize.getWidth();
		int h = (int) screenSize.getHeight();

		setBounds((w-WIDTH) / 2, (h-HEIGHT) / 2, WIDTH, HEIGHT);

		setLayout(new BorderLayout());

		spacerN = new JPanel();
		spacerS = new JPanel();
		spacerW = new JPanel();
		spacerE = new JPanel();
		spacerN.setPreferredSize(new Dimension(PADDING, PADDING));
		spacerS.setPreferredSize(new Dimension(PADDING, PADDING));
		spacerW.setPreferredSize(new Dimension(PADDING, PADDING));
		spacerE.setPreferredSize(new Dimension(PADDING, PADDING));

		add(spacerN, BorderLayout.NORTH);
		add(spacerS, BorderLayout.SOUTH);
		add(spacerW, BorderLayout.WEST);
		add(spacerE, BorderLayout.EAST);

		mainPanel = new Panel();
		mainPanel.setDataValues(dataValues);
		inputType = dataValues.get(0).getInputType();
		visualizationType = dataValues.get(0).getVisualizationType();
		add(mainPanel, BorderLayout.CENTER);

		setVisible(true);

		//nn = NeuralNetwork.load(FILE_SAVE + FILE_EXTENSION);

		if(nn == null) {
			NeuralNetworkSettings.setUseDropout(true);
			NeuralNetworkSettings.setDropoutRate(0.1f);
			NeuralNetworkSettings.setUseInertia(true);

			nn = NeuralNetworkBuilder.Builder()
					.input(IN)
					.hidden(2, ActivationFunctionType.GELU)
					.hidden(2, ActivationFunctionType.GELU)
					.output(OUT, ActivationFunctionType.SIGMOID)
					.build();

			train();

			feedForwardAndPaint();

			//nn.save(FILE_SAVE + FILE_EXTENSION);

		}

	}

	@Override
	public void repaint() {
		super.repaint();
	}

	public void train() {
		//wait for jframe to open in order to take first screenshot
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long startTime = System.currentTimeMillis();

		for(int k=0;k<EPOCHS;k++) {
			Input[] in = new Input[IN];
			Float[] out = new Float[OUT];

			for(int j=0;j<mainPanel.getDataValues().size();j++) {
				in[0] = new Input(mainPanel.getDataValues().get(j).getTime(), InputType.CLASSIFICATION);
				out[0] = Input.normalize(mainPanel.getDataValues().get(j).getValue(), inputType);

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
	}

	public void feedForwardAndPaint() {
		ArrayList<DataPoint> predictedDataValues = new ArrayList<DataPoint>();
		Float[] predicted = null;
		
		for(int time=0;time<DataPoint.MAX_TIME;time+=GRANULARITY) {
			Input inTime = new Input(time, InputType.CLASSIFICATION);

			try {
				predicted = nn.feedForward(inTime);
			} catch (NeuralNetworkException e) {
				e.printStackTrace();
			}

			predictedDataValues.add(new DataPoint(inputType, visualizationType, time, Input.deNormalize(predicted[0], inputType)));
		}
		
		Float[] actual = new Float[OUT];
		float mae = 0, mse = 0;
		int count=0;
		for(int j=0;j<mainPanel.getDataValues().size();j++) {
			for(int i=0;i<predictedDataValues.size();i++) {
				if(mainPanel.getDataValues().get(j).getTime() == predictedDataValues.get(i).getTime()) {
					actual[0] = Input.normalize(mainPanel.getDataValues().get(j).getValue(), inputType);
					predicted[0] = Input.normalize(predictedDataValues.get(i).getValue(), inputType);
					mae += NeuralNetwork.calculateMAE(actual,  predicted);
					mse += NeuralNetwork.calculateMSE(actual,  predicted);
					count++;
				}
			}
		}
		
		mae /= count;
		mse /= count;

		mainPanel.setPredictedTemps(predictedDataValues);
		mainPanel.setMae(mae);
		mainPanel.setMse(mse);

		takeScreenshot();
		
		repaint();
	}

	private void takeScreenshot() {
		
		
//		//wait for the repaint to apply
//
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		Point locationOnScreen = mainPanel.getLocationOnScreen();
//		Rectangle r = new Rectangle(locationOnScreen, mainPanel.getSize());
//		ScreenshotTaker.take(r);
	}

	

}
