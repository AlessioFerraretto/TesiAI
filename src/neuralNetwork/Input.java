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
			return normalize(value, type);
		}
		return value;
	}

	//normalizes a value in the range [-1, 1]
	public static float normalize(float value, InputType type) {
		float delta = type.getMaxValue()-type.getMinValue();
		return 2*(value - type.getMinValue() - delta/2) / delta;
	}


	public static float deNormalize(float value, InputType type) {
		float delta = type.getMaxValue()-type.getMinValue();
		return value * delta / 2 + type.getMinValue() + delta/2;
	}
	
	//normalizes a value in the range [0, 1]
	public static float normalizePositive(float value, InputType type) {
		return (value - type.getMinValue()) / (type.getMaxValue()-type.getMinValue());
	}
	
	public static float deNormalizePositive(Float value, InputType type) {
		float delta = type.getMaxValue()-type.getMinValue();
		return value * delta + type.getMinValue();
	}
}
