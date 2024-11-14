import java.awt.Color;
import java.util.ArrayList;

import classification.Panel;
import classification.Point;
import neuralNetwork.ActivationFunctionType;
import neuralNetwork.Input;
import neuralNetwork.InputType;
import neuralNetwork.NeuralNetwork;
import neuralNetwork.NeuralNetworkBuilder;
import neuralNetwork.NeuralNetworkException;
import neuralNetwork.RandomSingleton;

public class test {
	public static void main(String[] args) throws NeuralNetworkException {

		int N = 100000, IN = 2, OUT = 1;
		long startTime = System.currentTimeMillis();

		NeuralNetwork nn = NeuralNetworkBuilder.Builder()
				.input(IN)
				.hidden(3, ActivationFunctionType.RELU)
				.output(OUT, ActivationFunctionType.SIGMOID)
				.build();
		
		//testing 
		for(int k=0;k<N;k++) {
			Input[] in = new Input[IN];
			Float[] out = new Float[OUT];

			int i1 = RandomSingleton.randBool() ? 1 : 0;
			int i2 = RandomSingleton.randBool() ? 1 : 0;
			in[0] = new Input(i1, InputType.XOR);
			in[1] = new Input(i2, InputType.XOR);

			out[0] = (i1==1 ^ i2==1) ? 1f : 0f;

			nn.test(in, out);
//			printProgressBar(startTime, k, N);
		}

		float out1 = nn.evaluate(new Input(1f, InputType.XOR),new Input(1f, InputType.XOR))[0];
		float out2 = nn.evaluate(new Input(0f, InputType.XOR),new Input(1f, InputType.XOR))[0];
		float out3 = nn.evaluate(new Input(1f, InputType.XOR),new Input(0f, InputType.XOR))[0];
		float out4 = nn.evaluate(new Input(0f, InputType.XOR),new Input(0f, InputType.XOR))[0];
		
		System.out.println();
		System.out.println("1XOR1: " + out1);
		System.out.println("0XOR1: " + out2);
		System.out.println("1XOR0: " + out3);
		System.out.println("0XOR0: " + out4);

		if(out1 < 0.1f && out4 < 0.1f && out2 > 0.9f && out3 > 0.9f) {
			System.out.println("ok");
		}
	}

	private static void printProgressBar(long startTime, int i, long total) {
		clearLine();
		printPB((int) ((i * 100.0) / total), ((System.currentTimeMillis() - startTime) * total) / (i + 1) - (System.currentTimeMillis() - startTime));

	}

	private static void printPB(int percent, long remainingTimeMillis) {
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
