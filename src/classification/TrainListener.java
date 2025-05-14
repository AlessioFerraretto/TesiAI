package classification;

import java.util.ArrayList;

import common.Point;

public interface TrainListener {

	public void train();
	public ArrayList<Point> getInputs();

}
