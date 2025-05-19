package temperaturePredictor;

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
			GRANULARITY=1, EVALUATION_INTERVAL = 1000, EPOCHS = 1000;

	private Panel mainPanel;
	private JPanel spacerN,spacerS,spacerW,spacerE;
	private static String FILE_SAVE = "save", FILE_EXTENSION = ".dat"; 

	NeuralNetwork nn;
	int IN = 1, OUT = 1;

	public Frame(ArrayList<Temperature> temps) {
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
		mainPanel.setTemps(temps);
		add(mainPanel, BorderLayout.CENTER);

		setVisible(true);

		//		nn = NeuralNetwork.load(FILE_SAVE + FILE_EXTENSION);

		if(nn == null) {
			NeuralNetworkSettings.setUseDropout(true);
			NeuralNetworkSettings.setDropoutRate(0.1f);
			NeuralNetworkSettings.setUseInertia(true);

			nn = NeuralNetworkBuilder.Builder()
					.input(IN)
					.hidden(6, ActivationFunctionType.RELU)
					.hidden(6, ActivationFunctionType.RELU)
					.output(OUT, ActivationFunctionType.TANH)
					.build();

			train();

			feedForwardAndPaint();

			//			nn.save(FILE_SAVE + FILE_EXTENSION);

		}

	}

	@Override
	public void repaint() {
		super.repaint();
	}

	public void train() {
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long startTime = System.currentTimeMillis();

		//training 
		for(int k=0;k<EPOCHS;k++) {
			Input[] in = new Input[IN];
			Float[] out = new Float[OUT];
			Float[] predicted = null;

			float mae = 0, mse = 0;
			for(int j=0;j<mainPanel.getTemps().size();j++) {
				in[0] = new Input(mainPanel.getTemps().get(j).getTime(), InputType.CLASSIFICATION);
				out[0] = Input.normalize(mainPanel.getTemps().get(j).getValue(), InputType.TEMPERATURA);

				try {
					predicted = nn.train(in, out);
				} catch (NeuralNetworkException e) {
					e.printStackTrace();
				}

				mae += NeuralNetwork.calculateMAE(out, predicted);
				mse += NeuralNetwork.calculateMSE(out, predicted);
			}
			mae /= mainPanel.getTemps().size();
			mse /= mainPanel.getTemps().size();

			if(k%EVALUATION_INTERVAL == 0) {
				if(k!=0) {
					printProgressBar(startTime, k, EPOCHS);
					mainPanel.setMae(mae);
					mainPanel.setMse(mse);
					feedForwardAndPaint();
				}
			}
		}
	}

	public void feedForwardAndPaint() {
		ArrayList<Temperature> predictedTemps = new ArrayList<Temperature>();
		for(int time=0;time<Temperature.MAX_TIME;time+=GRANULARITY) {
			Input inTime = new Input(time, InputType.CLASSIFICATION);

			Float[] out = null;
			try {
				out = nn.feedForward(inTime);
			} catch (NeuralNetworkException e) {
				e.printStackTrace();
			}

			predictedTemps.add(new Temperature(time, Input.deNormalize(out[0], InputType.TEMPERATURA)));
		}
		mainPanel.setPredictedTemps(predictedTemps);

		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//wait for the repaint to apply
		SwingUtilities.invokeLater(() -> {
			Point locationOnScreen = mainPanel.getLocationOnScreen();
			Rectangle r = new Rectangle(locationOnScreen, mainPanel.getSize());
			ScreenshotTaker.take(r);

		});
		repaint();
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
