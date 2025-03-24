package neuralNetwork;

import java.io.Serializable;

public class NeuronBias extends Neuron implements Serializable  {

	public NeuronBias(ActivationFunctionType activationFunctionType) {
		super(activationFunctionType);
	}

	protected Float evaluate() {
		return 1f;
	}
	
	public Float getLastOutput() {
		return 1f;
	}
	
}
