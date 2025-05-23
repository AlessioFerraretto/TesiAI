package predictor;

import neuralNetwork.InputType;

public class DataPoint {

	public static final int MAX_TIME = 60 * 24; // in minutes

	private InputType inputType;
	private VisualizationType visualizationType;
	private int time; //in minutes
	private float value;
	
	public DataPoint(InputType inputType, VisualizationType visualizationType, int time, float value) {
		this.inputType = inputType;
		this.visualizationType = visualizationType;
		this.time = time;
		this.value = value;
	}

	public DataPoint(InputType inputType, VisualizationType visualizationType, String datetime, float value) {
		String date = datetime.split("T")[0];
		String time = datetime.split("T")[1];
		
		int year = Integer.parseInt(date.split("-")[0]);
		int month = Integer.parseInt(date.split("-")[1]);
		int day = Integer.parseInt(date.split("-")[2]);
		
		int hour = Integer.parseInt(time.split(":")[0]);
		int minute = Integer.parseInt(time.split(":")[1]);
		int second = Integer.parseInt(time.split(":")[2].split("\\.")[0]); // '.' is a regular expression, '\\.' is the escape
		
		this.inputType = inputType;
		this.visualizationType = visualizationType;
		this.time = generateTimeFromDateTime(year, month, day, hour, minute, second);
		this.value = value;
	}

	private int generateTimeFromDateTime(int year, int month, int day, int hour, int minute, int second) {
		//does not consider year, month, day in this implementation
		return hour*60 + minute;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public InputType getInputType() {
		return inputType;
	}

	public VisualizationType getVisualizationType() {
		return visualizationType;
	}
	
	public Float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

}
