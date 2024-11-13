
public class Arco {

	private Neurone from, to;
	float weight;

	public Arco(Neurone from, Neurone to, float weight) {
		this.from = from;
		this.to = to;
		this.weight = weight;
	}

	public Arco(Neurone from, Neurone to) {
		this(from, to, RandomSingleton.randFloat(-1,1));
	}

	public Neurone getFrom() {
		return from;
	}

	public float getWeight() {
		if(Float.isNaN(weight)) {
			System.out.println("a");
		}
		return weight;
	}

	public void backpropagation(float targetOutput) {
		float idk = from.getLastOutput();
		float actualOutput = to.getLastOutput();
		float deviation = targetOutput-actualOutput; //deviation from expected output
		float derivate = NeuralNetworkSettings.derivate(actualOutput);
		float weightChange = NeuralNetworkSettings.LEARNING_RATE * deviation *	
				idk * derivate;							


		weight += weightChange;

		if(from!=null) {
			from.backpropagation(targetOutput);
		}
	}

}
