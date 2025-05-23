package predictor;

import neuralNetwork.InputType;

public class Co2 extends DataPoint {

	public Co2(int time, float value) {
		super(InputType.CO2, VisualizationType.CO2, time, value);
	}

}
