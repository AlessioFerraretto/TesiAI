

public class Input {

	private InputType type;
	private float value;
	
	private static final boolean normalize = true;
	
	public Input(InputType type) {
		this(RandomSingleton.randFloat(type.getMinValue(), type.getMaxValue()), type);
	}
	
	public Input(float v, InputType type) {
		value = v;
		this.type = type;
	}

	public float getValue() {
		if(normalize) {
			return normalize();
		}
		
		return value;
	}
	
	public String toString() {
		return getValue()+"";
	}

	public float normalize() {
		return (value - type.getMinValue() - (type.getMaxValue()-type.getMinValue())/2) / ((type.getMaxValue()-type.getMinValue())/2);
	}
	
	public static float deNormalize(Float v, InputType type) {
		return v*(type.getMaxValue()-type.getMinValue())/2;
	}
}
