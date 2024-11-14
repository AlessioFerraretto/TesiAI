package neuralNetwork;
public class NeuroneBias extends Neurone {

	public NeuroneBias(ActivationFunctionType activationFunctionType) {
		super(activationFunctionType);
	}

	protected Float evaluate() {
		return 1f;
	}
	
	public Float getLastOutput() {
		return 1f;
	}
	
}
