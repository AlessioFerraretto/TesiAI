package predictor;

import neuralNetwork.InputType;

public class Temperature extends DataPoint {
	
	public Temperature(int time, float value) {
		super(InputType.TEMPERATURE, VisualizationType.TEMPERATURE, time, value);
	}
	
}
