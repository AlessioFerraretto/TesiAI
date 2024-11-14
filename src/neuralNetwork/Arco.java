package neuralNetwork;

public class Arco {

	public static int N = 0;
	private int id;
	
	
	private Neurone from, to;
	float weight;

	public Arco(Neurone from, Neurone to, float weight) {
		this.from = from;
		this.to = to;
		this.weight = weight;
		
		id = N;
		N++;
	}

	public Arco(Neurone from, Neurone to) {
		this(from, to, RandomSingleton.randFloat(-1,1));
	}

	public Neurone getFrom() {
		return from;
	}

	public float getWeight() {
		if(Float.isNaN(weight)) {
			System.out.println("m");
		}
		return weight;
	}

	public void updateWeight(float weightChange) {
		weight+=weightChange;
	}

	public Neurone getTo() {
		return to;
	}

	public String toString() {
		return "Arco id: "+id+ " from " + from.getId() + " to " + to.getId();
	}
}
