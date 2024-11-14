package neuralNetwork;

public class NeuralNetworkSettings {

	public final static float DEFAULT_LEARNING_RATE = 0.01f;

	public static Float activationFuncion(ActivationFunctionType type, float x) {
		switch(type) {
		case HYPERBOLIC_TANGENT:
			return hyperbolicTangentActivationFunction(x);
		case LINEAR:
			return linearActivationFunction(x);
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

	public static Float derivate(ActivationFunctionType type,float x) {
		float eps= 1e-4f;

		boolean calculateWithLimit = false;

		switch(type) {
		case HYPERBOLIC_TANGENT:
			if(calculateWithLimit)
				return (hyperbolicTangentActivationFunction(hyperbolicTangentInverse(x)+eps) -hyperbolicTangentActivationFunction(hyperbolicTangentInverse(x)-eps))/2;
			else 
				return hyperbolicTangentDerivate(x);
		case LINEAR:
			if(calculateWithLimit)
				return (linearActivationFunction(x+eps)-linearActivationFunction(x-eps))/2;
			else
				return linearDerivate(x);
		case BIPOLAR_STEP:
			if(calculateWithLimit)
				return (bipolarStepActivationFunction(x+eps)-bipolarStepActivationFunction(x-eps))/2;
			else
				return bipolarStepDerivate(x);
		case SIGMOID:
			if(calculateWithLimit)
				return (sigmoidActivationFunction(x+eps)-sigmoidActivationFunction(x-eps))/2;
			else
				return sigmoidDerivate(x);
		case GELU:
			if(calculateWithLimit)
				return (geluActivationFunction(x+eps)-geluActivationFunction(x-eps))/2;
			else
				return geluDerivate(x);
		case RELU:
			if(calculateWithLimit)
				return (reluActivationFunction(x+eps)-reluActivationFunction(x-eps))/2;
			else
				return reluDerivate(x);
		}

		return null;
	}

	//Linear
	private static Float linearActivationFunction(float x) {
		if(x>1) {
			x=1;
		}
		if(x<-1) {
			x=-1;
		}
		return x;	
	}

	private static Float linearDerivate(float x) {
		return 1f;
	}

	//BipolarStep
	private static Float bipolarStepActivationFunction(float x) {
		if(x>0) {
			x=1;
		}
		if(x<0) {
			x=-1;
		}
		return x;
	}
	private static Float bipolarStepDerivate(float x) {
		return 0.01f; //should be for x!=0 and infinite for x=0
	}


	//Sigmoid
	private static Float sigmoidActivationFunction(float x) {
		return (float) (1/(1+Math.exp(-x)));
	}
	private static Float sigmoidDerivate(float  sigmoidOutput) {
		return sigmoidOutput * (1 - sigmoidOutput);
	}

	//Hyperbolic Tangent
	private static Float hyperbolicTangentActivationFunction(float x) {
		return (float) Math.tanh(x);
	}

	private static float hyperbolicTangentInverse(float hyperbolicTangentOutput) {
		return (float) (0.5 * Math.log((1 + hyperbolicTangentOutput) / (1 - hyperbolicTangentOutput))); //get x

	}

	private static Float hyperbolicTangentDerivate(float hyperbolicTangentOutput) {
		float x = hyperbolicTangentInverse(hyperbolicTangentOutput);
		return (float) Math.pow(1/Math.cosh(x),2);
	}

	//GELU
	public static float geluActivationFunction(float x) {
		final float term = (float) (0.044715 * Math.pow(x, 3));
		return (float) (0.5 * x * (1 + Math.tanh(Math.sqrt(2 / Math.PI) * (x + term))));
	}

	public static float geluDerivate(float x) {
		final double sqrt2OverPi = Math.sqrt(2 / Math.PI);

		float term1 = (float) (0.5 * Math.tanh(sqrt2OverPi * (x + 0.044715 * Math.pow(x, 3))) + 0.5);
		float term2 = (float) (sqrt2OverPi * x * (1 + 0.134145 * Math.pow(x, 2)));
		float sech2 = (float) (1 - Math.pow(Math.tanh(sqrt2OverPi * (x + 0.044715 * Math.pow(x, 3))), 2));

		float result = term1 + term2 * sech2 * 0.5f;
		
		if(Float.isNaN(result)) {
			System.out.println(x + " gelu");
			return 0;
		}
		
		return term1 + term2 * sech2 * 0.5f;
	}
	
	//RELU
	public static float reluActivationFunction(float x) {
		return Math.max(0,x);
	}

	public static float reluDerivate(float x) {
		return x > 0 ? 1f : 0;
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
	//	public float getLearningRate() {
	//		return learningRate;
	//	}
	//
	//	public Float getActivationFuncion(float x) {
	//		return activationFuncion(activationFunctionType, x);
	//	}
	//	public Float getDerivate(float x) {
	//		return derivate(activationFunctionType, x);
	//	}
}
