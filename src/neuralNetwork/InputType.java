package neuralNetwork;

import classification.Panel;

public enum InputType {

	TEST(100),
	CLASSIFICATION(0,Panel.DIMENSION),
	XOR(0,1);
	
	private float minValue, maxValue;
	InputType(float maxValue) {
		this.minValue = -maxValue;
		this.maxValue = maxValue;
	}
	InputType(float minValue, float maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	public float getMinValue() {
		return minValue;
	}
	
	public float getMaxValue() {
		return maxValue;
	}
}
