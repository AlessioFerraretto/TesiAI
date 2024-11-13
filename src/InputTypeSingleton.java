//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.Random;
//
//public class InputTypeSingleton {
//
//	private static ArrayList<InputType> INPUT_TYPES = new ArrayList<InputType>();
//	private InputTypeSingleton() {}
//
//	private static Random r;
//
//	public static InputType createType(String type, float minValue, float maxValue) {
//		InputType t = new InputType(type, minValue, maxValue);
//		if(!INPUT_TYPES.contains(t)) {
//			INPUT_TYPES.add(t);
//			return t;
//		}
//		return getType(type);
//	}
//	
//	public static InputType getType(String type) {
//		INPUT_TYPES.
//		return t;
//	}
//
//}
//
//class InputType {
//	private String name;
//	private float minValue, maxValue;
//
//	protected InputType(String name, float minValue, float maxValue) {
//		this.name = name;	
//		this.minValue = minValue;	
//		this.maxValue = maxValue;	
//	}
//
//	public boolean equals(Object other) {
//		return getName().equals(((InputType) other).getName());
//	}
//
//	public int hashCode() {
//		return getName().hashCode();
//	}
//
//	private String getName() {
//		return name;
//	}
//
//}