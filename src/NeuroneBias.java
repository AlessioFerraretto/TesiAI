public class NeuroneBias extends Neurone {

	public NeuroneBias() {
		super();
	}

	protected Float evaluate() {
		return 1f;
	}
	
	public Float getLastOutput() {
		return 1f;
	}
	
}
