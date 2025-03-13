package neuralNetwork;

import common.RandomSingleton;

public class Input {

	private InputType type;
	private Float value;

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

	//normalizes a value in the range [-1, 1]
	public float normalize() {
		return (value - type.getMinValue() - (type.getMaxValue()-type.getMinValue())/2) / ((type.getMaxValue()-type.getMinValue())/2);
	}

	//normalizes a value in the range [0, 1]
	public float normalizePositive() {
		return (value - type.getMinValue()) / (type.getMaxValue()-type.getMinValue());
	}

//	public static float deNormalize(Float v, InputType type) {
//		return v*(type.getMaxValue()-type.getMinValue())/2;
//	}
}
