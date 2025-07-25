package neuralNetwork;

import classification.Panel;
import predictor.DataPoint;

public enum InputType {

	CLASSIFICATION(0,Panel.DIMENSION),
	TEMPERATURE(-100,100),
	CO2(-3000,3000),
	UMIDITY(-200,200),
	TVOC(-5000,5000), 
	COLOR(-255*3,255*3);
	
	private float minValue, maxValue; //unit: amount that defines how many units are in a line of the graph
	
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

	public float getDelta() {
		return getMaxValue() - getMinValue();
	}
}
