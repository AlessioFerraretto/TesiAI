package neuralNetwork;

import classification.Panel;
import temperaturePredictor.Temperature;

public enum InputType {

	CLASSIFICATION(0,Panel.DIMENSION),
	TEMPERATURA(0,50),
	UMIDITA(0,100);
	
	private float minValue, maxValue;
	
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
