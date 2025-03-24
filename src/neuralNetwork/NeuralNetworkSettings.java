package neuralNetwork;

import common.NumberController;

public class NeuralNetworkSettings {

	public final static Float DEFAULT_LEARNING_RATE = 0.01f, EPSILON = 1e-4f;
	public final static boolean DEFAULT_CALCULATE_DERIVATE_NUMERICALY = false, DEFAULT_USE_INTERTIA = true, DEFAULT_USE_DROPOUT = false;
	public static final Float DEFAULT_ALPHA = 0.93f, DEFAULT_DROPOUT_RATE = 0.1f/100f;

	private static boolean calculateDerivateNumerically = DEFAULT_CALCULATE_DERIVATE_NUMERICALY, useInertia = DEFAULT_USE_INTERTIA, dropout = DEFAULT_USE_DROPOUT;
	private static Float learningRate = DEFAULT_LEARNING_RATE, alpha = DEFAULT_ALPHA, dropoutRate = DEFAULT_DROPOUT_RATE;

	public static Float activationFunction(ActivationFunctionType type, float x) {
		switch(type) {
		case TANH:
			return hyperbolicTangentActivationFunction(x);
		case BIPOLAR_STEP:
			return bipolarStepActivationFunction(x);
		case SIGMOID:
			return sigmoidActivationFunction(x);
		case GELU:
			return geluActivationFunction(x);
		case RELU:
			return reluActivationFunction(x);
		}

		return null;
	}

	public static Float derivate(ActivationFunctionType type, Float x) {

		switch(type) {
		case TANH:
			if(getCalculateDerivateNumerically())
				return (hyperbolicTangentActivationFunction(x+EPSILON)-hyperbolicTangentActivationFunction(x))/EPSILON;
			else 
				return hyperbolicTangentDerivate(x);
		case BIPOLAR_STEP:
			if(getCalculateDerivateNumerically())
				return (bipolarStepActivationFunction(x+EPSILON)-bipolarStepActivationFunction(x))/EPSILON;
			else
				return bipolarStepDerivate(x);
		case SIGMOID:
			if(getCalculateDerivateNumerically())
				return (sigmoidActivationFunction(x+EPSILON)-sigmoidActivationFunction(x))/EPSILON;
			else
				return sigmoidDerivate(x);
		case GELU:
			if(getCalculateDerivateNumerically())
				return (geluActivationFunction(x+EPSILON)-geluActivationFunction(x))/EPSILON;
			else
				return geluDerivate(x);
		case RELU:
			if(getCalculateDerivateNumerically())
				return (reluActivationFunction(x+EPSILON)-reluActivationFunction(x))/EPSILON;
			else
				return reluDerivate(x);
		}

		return null;
	}

	//BipolarStep
	private static Float bipolarStepActivationFunction(float x) {
		return x > 0 ? 1f : -1f;
	}

	private static Float bipolarStepDerivate(float x) {
		if(x > -EPSILON && x < EPSILON) {
			return 0.01f;
		}

		return 0f;
	}


	//Sigmoid
	private static Float sigmoidActivationFunction(float x) {
		return (float) (1/(1+Math.exp(-x)));
	}
	private static Float sigmoidDerivate(float x) {
		Float y = sigmoidActivationFunction(x);
		return y * (1 - y);
	}

	//Hyperbolic Tangent
	private static Float hyperbolicTangentActivationFunction(float x) {
		return (float) Math.tanh(x);
	}

	private static float hyperbolicTangentInverse(float hyperbolicTangentOutput) {
		return (float) (0.5 * Math.log((1 + hyperbolicTangentOutput) / (1 - hyperbolicTangentOutput))); //get x

	}

	private static Float hyperbolicTangentDerivate(float x) {
		return NumberController.check((float) Math.pow(1/Math.cosh(x),2), 9);
	}

	//GELU
	public static float geluActivationFunction(float x) {
		final float term = (float) (0.044715 * Math.pow(x, 3));
		float r = (float) (0.5 * x * (1 + Math.tanh(Math.sqrt(2 / Math.PI) * (x + term))));

		r = NumberController.check(r, 0);

		return r;
	}

	public static float geluDerivate(float x) {
		final double sqrt2OverPi = Math.sqrt(2 / Math.PI);

		float tanhTerm = (float) Math.tanh(sqrt2OverPi * (x + 0.044715 * Math.pow(x, 3)));

		float term1 = (float) (0.5 * tanhTerm + 0.5);
		float term2 = (float) (sqrt2OverPi * x * (1 + 0.134145 * Math.pow(x, 2)));
		float sech2 = (float) (1 - Math.pow(tanhTerm, 2)); // Use the previously computed tanh term

		float result = term1 + term2 * sech2 * 0.5f;

		result = NumberController.check(result, 99);
		return result;
	}

	//RELU
	public static float reluActivationFunction(float x) {
		return Math.max(0,x);
	}

	public static float reluDerivate(float x) {
		return x > 0 ? 1f : 0;
	}

	public static void setLearningRate(Float f) {
		learningRate = f;
	}

	public static Float getLearningRate() {
		return learningRate;
	}

	public static void setCalculateDerivateNumerically(boolean b) {
		calculateDerivateNumerically = b;
	}

	public static boolean getCalculateDerivateNumerically() {
		return calculateDerivateNumerically;
	}

	public static boolean getUseInertia() {
		return useInertia;
	}

	public static Float getAlpha() {
		return alpha;
	}

	public static void setUseInertia(boolean b) {
		useInertia = b;
	}

	public static void setAlpha(float f) {
		alpha = f;
	}

	public static boolean getUseDropout() {
		return dropout;
	}

	public static void setUseDropout(boolean b) {
		dropout = b;
	}

	public static Float getDropoutRate() {
		return dropoutRate;
	}


	public static void setDropoutRate(Float f) {
		dropoutRate = f;
	}

	//if a project contains multiple NeuralNetwork, each one with its own Settings, use this.
	//	private float learningRate;
	//	private ActivationFunctionType activationFunctionType;
	//
	//	public NeuralNetworkSettings() {
	//		this(DEFAULT_LEARNING_RATE, DEFAULT_ACTIVATION_FUNCTION);
	//	}
	//
	//	public NeuralNetworkSettings(float learningRate, ActivationFunctionType activationFunctionType) {
	//		this.learningRate = learningRate;
	//		this.activationFunctionType = activationFunctionType;
	//	}
	//
	//	public Float getActivationFuncion(float x) {
	//		return activationFuncion(activationFunctionType, x);
	//	}
	//	public Float getDerivate(float x) {
	//		return derivate(activationFunctionType, x);
	//	}
}
