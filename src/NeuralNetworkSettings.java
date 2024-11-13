
public class NeuralNetworkSettings {

	public final static float LEARNING_RATE = 0.2f;
	public final static ActivationFunction 
	ACTIVATION_FUNCTION = ActivationFunction.HYPERBOLIC_TANGENT;
	
	public static Float activationFuncion(float x) {
		switch(ACTIVATION_FUNCTION) {
		case HYPERBOLIC_TANGENT:
			return hyperbolicTangentActivationFunction(x);
		case LINEAR:
			return linearActivationFunction(x);
		case SIGMOID:
			return sigmoidActivationFunction(x);
		}
		
		return null;
	}
	public static Float derivate(float x) {
		switch(ACTIVATION_FUNCTION) {
		case HYPERBOLIC_TANGENT:
			return hyperbolicTangentDerivate(x);
		case LINEAR:
			return linearDerivate(x);
		case SIGMOID:
			return sigmoidDerivate(x);
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
	private static Float hyperbolicTangentDerivate(float hyperbolicTangentOutput) {
		float x = (float) (0.5 * Math.log((1 + hyperbolicTangentOutput) / (1 - hyperbolicTangentOutput))); //get x
		
		return (float) Math.pow(1/Math.cosh(x),2);
	}
	
}
