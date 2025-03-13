package neuralNetwork;

import java.io.Serializable;

public class NeuroneBias extends Neurone implements Serializable  {

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
