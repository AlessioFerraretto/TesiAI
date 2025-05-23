package predictor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import common.RandomSingleton;
import neuralNetwork.InputType;

public class myMain {

	private static final String FILE_NAME = "Air Quality-co.csv", SEPARATOR = ",";
	
	public static void main(String[] args) {

		Frame f = new Frame(readFile(FILE_NAME));
//		Frame f = new Frame(generateRandom(InputType.TEMPERATURA, VisualizationType.TEMPERATURA));

	}
	
	public static ArrayList<DataPoint> readFile(String fileName) {
		File f = new File(fileName);
		ArrayList<DataPoint> results = new ArrayList<>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String line;
			line = reader.readLine(); //remove first line 
			int i=0;
			
			while((line = reader.readLine()) != null) {
				String[] values = line.split(SEPARATOR);
				String time = values[0];
				float value = Float.parseFloat(values[1]);
				if(i%6 == 0)
					results.add(new DataPoint(InputType.CO2, VisualizationType.CO2, time, value));
				i++;
			}
			
			reader.close();

		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	public static ArrayList<DataPoint> generateRandom(InputType inputType, VisualizationType visualizationType) {
		ArrayList<DataPoint> results = new ArrayList<>();
		results.add(new DataPoint(inputType, visualizationType, 0, RandomSingleton.randFloat(20, 30)));

		for(int i=1;i<24*60;i++) {
			results.add(new DataPoint(inputType, visualizationType, i, 
					results.get(i-1).getValue() 
					+ RandomSingleton.randFloat(0, 0.4f)
					+ RandomSingleton.randFloat(0, 0.1f)
					- RandomSingleton.randFloat(0, 0.4f) 
					- RandomSingleton.randFloat(0, 0.1f) ));
			
		}
		
		return results;
	}
}
