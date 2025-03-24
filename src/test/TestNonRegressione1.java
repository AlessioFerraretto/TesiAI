package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import common.Point;
import common.RandomSingleton;
import neuralNetwork.ActivationFunctionType;
import neuralNetwork.Input;
import neuralNetwork.InputType;
import neuralNetwork.NeuralNetwork;
import neuralNetwork.NeuralNetworkBuilder;
import neuralNetwork.NeuralNetworkException;
import neuralNetwork.NeuralNetworkSettings;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RunWith(JUnit4.class)
class TestNonRegressione1 {

	private static final int TEST_EPOCHS = 10000, TEST_SEED = 0;

	private static final Float 
	TEST_LEARNING_RATE = NeuralNetworkSettings.DEFAULT_LEARNING_RATE,
	TEST_ALPHA = NeuralNetworkSettings.DEFAULT_ALPHA,
	TEST_DROPOUT_RATE = NeuralNetworkSettings.DEFAULT_DROPOUT_RATE;


	private static final boolean 
	TEST_CALCULATE_DERIVATE_NUMERICALLY = NeuralNetworkSettings.DEFAULT_CALCULATE_DERIVATE_NUMERICALY,
	TEST_USE_INERTIA = false,
	TEST_USE_DROPOUT = NeuralNetworkSettings.DEFAULT_USE_DROPOUT;

	@BeforeAll
	static void setup() {
		RandomSingleton.setSeed(TEST_SEED);
		NeuralNetworkSettings.setLearningRate(TEST_LEARNING_RATE);
		NeuralNetworkSettings.setCalculateDerivateNumerically(TEST_CALCULATE_DERIVATE_NUMERICALLY);
		NeuralNetworkSettings.setUseInertia(TEST_USE_INERTIA);
		NeuralNetworkSettings.setAlpha(TEST_ALPHA);
		NeuralNetworkSettings.setUseDropout(TEST_USE_DROPOUT);
		NeuralNetworkSettings.setDropoutRate(TEST_DROPOUT_RATE);
	}


	@Test
	@Order(1)
	void testSigmoid() {
		int IN = 2, OUT = 2;

		NeuralNetwork nn = NeuralNetworkBuilder.Builder()
				.input(IN)
				.hidden(3, ActivationFunctionType.SIGMOID)
				.hidden(3, ActivationFunctionType.SIGMOID)
				.output(OUT, ActivationFunctionType.SIGMOID)
				.build();

		trainNeuralNetwork(nn, IN, TEST_EPOCHS, 0.1524454f, 0.8479387f);
	}


	@Test
	@Order(2)
	void testBipolarStep() {
		int IN = 2, OUT = 2;

		NeuralNetwork nn = NeuralNetworkBuilder.Builder()
				.input(IN)
				.hidden(3, ActivationFunctionType.BIPOLAR_STEP)
				.hidden(3, ActivationFunctionType.BIPOLAR_STEP)
				.output(OUT, ActivationFunctionType.BIPOLAR_STEP)
				.build();

		trainNeuralNetwork(nn, IN, TEST_EPOCHS, -1f, 1f);

	}

	@Test
	@Order(3)
	void testGelu() {
		int IN = 2, OUT = 2;

		NeuralNetwork nn = NeuralNetworkBuilder.Builder()
				.input(IN)
				.hidden(3, ActivationFunctionType.GELU)
				.hidden(3, ActivationFunctionType.GELU)
				.output(OUT, ActivationFunctionType.GELU)
				.build();

		trainNeuralNetwork(nn, IN, TEST_EPOCHS, 0.982507f, 0.03504354f);
	}

	@Test
	@Order(4)
	void testHyperbolicTangent() {
		int IN = 2, OUT = 2;

		NeuralNetwork nn = NeuralNetworkBuilder.Builder()
				.input(IN)
				.hidden(3, ActivationFunctionType.TANH)
				.hidden(3, ActivationFunctionType.TANH)
				.output(OUT, ActivationFunctionType.TANH)
				.build();

		trainNeuralNetwork(nn, IN, TEST_EPOCHS, -0.27816874f, 0.97614646f);
	}

	@Test
	@Order(6)
	void testRelu() {
		int IN = 2, OUT = 2, EPOCHS = TEST_EPOCHS;

		NeuralNetwork nn = NeuralNetworkBuilder.Builder()
				.input(IN)
				.hidden(3, ActivationFunctionType.RELU)
				.hidden(3, ActivationFunctionType.RELU)
				.output(OUT, ActivationFunctionType.RELU)
				.build();

		trainNeuralNetwork(nn, IN, EPOCHS, 0.5217011f, 0.47836763f);
	}


	static void trainNeuralNetwork(NeuralNetwork nn, int IN, int EPOCHS, Float... outputs) {
		int OUT = 2;

		ArrayList<Point> points = new ArrayList<>();
		for (int i=0;i<10;i++) {
			points.add(Point.generateRandomPoint(2));
		}

		//testing 
		for(int k=0;k<EPOCHS;k++) {
			Input[] in = new Input[IN];
			Float[] out = new Float[OUT];

			for(int j=0;j<points.size();j++) {
				for (int i=0;i<IN;i++) {
					in[i] = new Input(points.get(j).getInput(i), InputType.CLASSIFICATION);

				}
				out[0] = points.get(j).getType().equals(Color.RED) ? 1f : 0;
				out[1] = points.get(j).getType().equals(Color.BLUE) ? 1f : 0;

				try {
					nn.train(in, out);
				} catch (NeuralNetworkException e) {
					e.printStackTrace();
				}
			}

		}

		Float[] results = null;
		try {
			results = nn.feedForward(new Input[] { new Input(0, InputType.CLASSIFICATION), new Input(0, InputType.CLASSIFICATION) });
		} catch (NeuralNetworkException e) {
			e.printStackTrace();
		}

		for(int i=0;i<OUT;i++) {
			assertEquals(outputs[i], results[i]);
		}
	}


}
