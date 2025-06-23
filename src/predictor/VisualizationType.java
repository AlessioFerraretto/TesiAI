package predictor;

import classification.Panel;

public enum VisualizationType {

	CLASSIFICATION(0,Panel.DIMENSION,1,""),
	TEMPERATURE(20,40,1,"°"),
	CO2(0,1500,100,""),
	UMIDITY(0,100,10,"%"),
	TVOC(0,2500,500,"");
	
	
	private float minValue, maxValue, unit; //unit: amount that defines how many units are in a line of the graph
	private String symbol;
	
	VisualizationType(float minValue, float maxValue, float unit, String symbol) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.unit = unit;
		this.symbol = symbol;
	}
	
	public float getMinValue() {
		return minValue;
	}
	
	public float getMaxValue() {
		return maxValue;
	}

	public float getUnit() {
		return unit;
	}
	
	public String getSymbol() {
		return symbol;
	}

	public float getDelta() {
		return getMaxValue() - getMinValue();
	}
}
