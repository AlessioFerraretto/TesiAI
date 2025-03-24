package neuralNetwork;

import java.io.Serializable;

import common.NumberController;

public class NeuronInput extends Neuron implements Serializable {

	protected float value;

	public NeuronInput() {
		super(null);
	}

	public Float getValue() {
		value = NumberController.check(value, 3);

		return value;
	}

	public void setValue(float v) {
		value = v;
	}

	protected Float evaluate() {		
		return getValue();
	}

	public Float getLastOutput() {
		return value;
	}
	
}
