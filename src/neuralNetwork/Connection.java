package neuralNetwork;

import java.io.Serializable;

import common.NumberController;
import common.RandomSingleton;

public class Connection implements Serializable {

	public static int N = 0;
	private int id;
	
	private Neuron from, to;
	private float weight, previousWeightChange;

	public Connection(Neuron from, Neuron to, float weight) {
		this.from = from;
		this.to = to;
		setWeight(weight);
		
		id = N;
		N++;
	}

	private void setWeight(float w) {
		weight = w;
		weight = NumberController.check(weight, 1);
	}

	public Connection(Neuron from, Neuron to) {
		this(from, to, RandomSingleton.randFloat(-1,1));
	}

	public Neuron getFrom() {
		return from;
	}

	public float getWeight() {
		return weight;
	}

	public void updateWeight(float weightChange) {
		previousWeightChange = weightChange;
		setWeight(weight+weightChange);
	}

	public Neuron getTo() {
		return to;
	}

	public String toString() {
		return "Arco id: "+id+ " from " + from.getId() + " to " + to.getId();
	}

	public Float getPreviousWeightChange() {
		return previousWeightChange;
	}
}
