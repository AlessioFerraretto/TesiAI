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
class TestNonRegressione3 {

	private static final int TEST_EPOCHS = 15000, TEST_SEED = 1;
	private static final Float TEST_LEARNING_RATE = 0.02f, TEST_ALPHA = 0.9f;
	private static final boolean TEST_CALCULATE_DERIVATE_NUMERICALLY = true, TEST_USE_INERTIA = false;;

	@BeforeAll
	static void setup() {
		RandomSingleton.setSeed(TEST_SEED);
		NeuralNetworkSettings.setLearningRate(TEST_LEARNING_RATE);
		NeuralNetworkSettings.setCalculateDerivateNumerically(TEST_CALCULATE_DERIVATE_NUMERICALLY);
		NeuralNetworkSettings.setUseInertia(TEST_USE_INERTIA);
		NeuralNetworkSettings.setAlpha(TEST_ALPHA);
		
		//Fixme
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

		TestNonRegressione1.trainNeuralNetwork(nn, IN, TEST_EPOCHS, 0.997042f, 0.003229596f);
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

		TestNonRegressione1.trainNeuralNetwork(nn, IN, TEST_EPOCHS, 1f, -1f);

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

		TestNonRegressione1.trainNeuralNetwork(nn, IN, TEST_EPOCHS, -4.6248664E-15f, -0f);
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

		TestNonRegressione1.trainNeuralNetwork(nn, IN, TEST_EPOCHS, 0.8407494f, 0.8385899f);
	}


	@Test
	@Order(5)
	void testLinear() {
		int IN = 2, OUT = 2;

		NeuralNetwork nn = NeuralNetworkBuilder.Builder()
				.input(IN)
				.hidden(3, ActivationFunctionType.LINEAR)
				.hidden(3, ActivationFunctionType.LINEAR)
				.output(OUT, ActivationFunctionType.LINEAR)
				.build();

		TestNonRegressione1.trainNeuralNetwork(nn, IN, TEST_EPOCHS, -1f, 1f);
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

		TestNonRegressione1.trainNeuralNetwork(nn, IN, EPOCHS, 0f, 0f);
	}



}
