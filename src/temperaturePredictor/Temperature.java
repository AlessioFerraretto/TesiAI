package temperaturePredictor;

public class Temperature {

	public static final int MAX_TIME = 60 * 24, MAX_TEMP = 40, MIN_TEMP = 20; // in minutes

	private int time; //minutes
	private float value;
	
	public Temperature(int time, float value) {
		this.time = time;
		this.value = value;
	}

	public Temperature(String datetime, float value) {
		String date = datetime.split("T")[0];
		String time = datetime.split("T")[1];
		
		int year = Integer.parseInt(date.split("-")[0]);
		int month = Integer.parseInt(date.split("-")[1]);
		int day = Integer.parseInt(date.split("-")[2]);
		
		int hour = Integer.parseInt(time.split(":")[0]);
		int minute = Integer.parseInt(time.split(":")[1]);
		int second = Integer.parseInt(time.split(":")[2].split("\\.")[0]); // '.' is a regular expression, '\\.' is the escape
		
		this.time = generateTimeFromDateTime(year, month, day, hour, minute, second);
		this.value = value;
	}

	private int generateTimeFromDateTime(int year, int month, int day, int hour, int minute, int second) {
		return hour*60 + minute;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}
	
}
