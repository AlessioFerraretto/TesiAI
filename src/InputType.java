public enum InputType {

	TEST(-100,100);
	
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
