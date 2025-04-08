package temperaturePredictor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

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
			GRANULARITY=10, EVALUATION_INTERVAL = 1000, EPOCHS = 10000;
	
	private Panel mainPanel;
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

		JPanel spacer1 = new JPanel();
		JPanel spacer2 = new JPanel();
		JPanel spacer3 = new JPanel();
		JPanel spacer4 = new JPanel();
		spacer1.setPreferredSize(new Dimension(PADDING, PADDING));
		spacer2.setPreferredSize(new Dimension(PADDING, PADDING));
		spacer3.setPreferredSize(new Dimension(PADDING, PADDING));
		spacer4.setPreferredSize(new Dimension(PADDING, PADDING));

		add(spacer1, BorderLayout.NORTH);
		add(spacer2, BorderLayout.SOUTH);
		add(spacer3, BorderLayout.WEST);
		add(spacer4, BorderLayout.EAST);

		mainPanel = new Panel();
		mainPanel.setTemps(temps);
		add(mainPanel, BorderLayout.CENTER);

		setVisible(true);

//		nn = NeuralNetwork.load(FILE_SAVE + FILE_EXTENSION);

		if(nn == null) {
			nn = NeuralNetworkBuilder.Builder()
					.input(IN)
					.hidden(2, ActivationFunctionType.GELU)
					.hidden(2, ActivationFunctionType.GELU)
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
		int N = EPOCHS;
		long startTime = System.currentTimeMillis();

		//training 
		for(int k=0;k<N;k++) {
			Input[] in = new Input[IN];
			Float[] out = new Float[OUT];
			Float[] predicted = null;

			for(int j=0;j<mainPanel.getTemps().size();j++) {
				in[0] = new Input(mainPanel.getTemps().get(j).getTime(), InputType.CLASSIFICATION);
				out[0] = Input.normalize(mainPanel.getTemps().get(j).getValue(), InputType.TEMPERATURA);
				
				try {
					predicted = nn.train(in, out);
				} catch (NeuralNetworkException e) {
					e.printStackTrace();
				}
			}

			if(k%EVALUATION_INTERVAL == 0) {
				printProgressBar(startTime, k, N);
				
				feedForwardAndPaint();
				
				float mae = NeuralNetwork.calculateMAE(out, predicted);
				float mse = NeuralNetwork.calculateMSE(out, predicted);
				System.out.print(String.format("\nMAE: %.8f\tMSE: %.8f", mae, mse));

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
