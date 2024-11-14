package classification;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import neuralNetwork.ActivationFunctionType;
import neuralNetwork.Input;
import neuralNetwork.InputType;
import neuralNetwork.NeuralNetwork;
import neuralNetwork.NeuralNetworkBuilder;
import neuralNetwork.NeuralNetworkException;


public class Frame extends JFrame implements RepaintListener, RunListener {

	public static int WIDTH = Panel.DIMENSION + 150, HEIGHT = Panel.DIMENSION + 40;
	private int x, y;
	private Panel mainPanel;
	private ButtonPanel buttonPanel;

	NeuralNetwork nn;
	int IN = 2, OUT = 2;

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


		nn = NeuralNetworkBuilder.Builder()
				.input(IN)
				.hidden(2, ActivationFunctionType.HYPERBOLIC_TANGENT)
				.hidden(2, ActivationFunctionType.HYPERBOLIC_TANGENT)
				.output(OUT, ActivationFunctionType.SIGMOID)
				.build();
	}

	@Override
	public void repaint() {
		super.repaint();
	}

	@Override
	public void run() {
		mainPanel.setEditable(false);
		int N = 10000;
		long startTime = System.currentTimeMillis();

		try {

			//testing 
			for(int k=0;k<N;k++) {
				Input[] in = new Input[IN];
				Float[] out = new Float[OUT];

				for(int j=0;j<mainPanel.getPoints().size();j++) {
					in[0] = new Input(mainPanel.getPoints().get(j).getX(), InputType.CLASSIFICATION);
					in[1] = new Input(mainPanel.getPoints().get(j).getY(), InputType.CLASSIFICATION);

					out[0] = mainPanel.getPoints().get(j).getType().equals(Color.RED) ? 1f : 0;
					out[1] = mainPanel.getPoints().get(j).getType().equals(Color.BLUE) ? 1f : 0;

					nn.test(in, out);
				}

				printProgressBar(startTime, k, N);
			}

			//evaluating
			ArrayList<Point> determinedPoints = new ArrayList();
			for(int x=0;x<Panel.DIMENSION;x++) {
				for(int y=0;y<Panel.DIMENSION;y++) {
					Input inX = new Input(x, InputType.CLASSIFICATION);
					Input inY = new Input(y, InputType.CLASSIFICATION);

					Float[] out = nn.evaluate(inX, inY);

					Color color;
					if(out[0]>out[1]) {
						int transp = Math.min((int) ((out[0]-out[1])*100),255);
						color = new Color(255,120,120,Math.abs(transp));
					} else {
						int transp = Math.min((int) ((out[1]-out[0])*100),255);
						color = new Color(120,120,255,Math.abs(transp));
					}
					determinedPoints.add(new Point(x,y,color));
				}
			}

			mainPanel.setDeterminedPoints(determinedPoints);

			mainPanel.setEditable(true);
			repaint();

		} catch (NeuralNetworkException e) {
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
