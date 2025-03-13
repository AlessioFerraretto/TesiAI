package temperaturePredictor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import common.RandomSingleton;

public class myMain {

	static final String FILE_NAME = "Qualita_Aria-t3.csv", SEPARATOR = ",";
	
	public static void main(String[] args) {

//		Frame f = new Frame(readFile(FILE_NAME));
		Frame f = new Frame(generateRandom());

	}
	
	public static ArrayList<Temperature> readFile(String fileName) {
		File f = new File(fileName);
		ArrayList<Temperature> results = new ArrayList<>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String line;
			line = reader.readLine(); //remove first line 
			
			while((line = reader.readLine()) != null) {
				String[] values = line.split(SEPARATOR);
				String time = values[0];
				float value = Float.parseFloat(values[1]);
				results.add(new Temperature(time,value));
			}
			
			reader.close();

		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	public static ArrayList<Temperature> generateRandom() {
		ArrayList<Temperature> results = new ArrayList<>();
		results.add(new Temperature(0, RandomSingleton.randFloat(20, 30)));

		for(int i=1;i<24*60;i++) {
			results.add(new Temperature(i, 
					results.get(i-1).getValue() 
					+ RandomSingleton.randFloat(0, 0.4f)
					+ RandomSingleton.randFloat(0, 0.1f)
					- RandomSingleton.randFloat(0, 0.4f) 
					- RandomSingleton.randFloat(0, 0.1f) ));
		}
		
		return results;
	}
}
