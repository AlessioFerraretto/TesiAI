package neuralNetwork;

import java.io.Serializable;

import common.NumberController;
import common.RandomSingleton;

public class Arco implements Serializable {

	public static int N = 0;
	private int id;
	
	private Neurone from, to;
	private float weight;

	public Arco(Neurone from, Neurone to, float weight) {
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

	public Arco(Neurone from, Neurone to) {
		this(from, to, RandomSingleton.randFloat(-1,1));
	}

	public Neurone getFrom() {
		return from;
	}

	public float getWeight() {
		return weight;
	}

	public void updateWeight(float weightChange) {
		setWeight(weight+weightChange);
	}

	public Neurone getTo() {
		return to;
	}

	public String toString() {
		return "Arco id: "+id+ " from " + from.getId() + " to " + to.getId();
	}
}
