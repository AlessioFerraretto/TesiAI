import java.util.ArrayList;

public class myMain {

	static long startTime = System.currentTimeMillis();
	static int N = 10000, IN = 1, OUT = 1;
	static float LEARNING_RATE = 0.2f;

	public static void main(String[] args) throws NeuralNetworkException {
		
		InputType testType = InputType.TEST;
		
		NeuralNetwork nn = new NeuralNetwork(IN, new int[] {2}, OUT);

		for(int k=0;k<N;k++) {
			Input[] in = new Input[IN];
			Float[] out = new Float[OUT];

			in[0] = new Input(testType);
			out[0] = in[0].getValue() * 0.5f;
			
			nn.test(in, out);

			if(k%1000 == 0) {
				printProgressBar(k, N);
			}
		}
		long totalTimeTaken = System.currentTimeMillis() - startTime;
		System.out.println("\nTotal time taken: " + (totalTimeTaken / 1000.0) + " seconds");

		System.out.println("20: " + Input.deNormalize(nn.evaluate(new Input(20,testType))[0], testType));
		System.out.println("10: " + Input.deNormalize(nn.evaluate(new Input(10,testType))[0], testType));
		System.out.println("0: " + Input.deNormalize(nn.evaluate(new Input(0,testType))[0], testType));

	}

	private static void printProgressBar(int i, long total) {
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
