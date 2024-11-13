public class NeuroneInput extends Neurone {

	protected float value;

	public NeuroneInput() {
		super();
	}

	public Float getValue() {
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
