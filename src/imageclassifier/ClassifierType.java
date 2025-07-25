package imageclassifier;

public enum ClassifierType {
	
	CAT("Cat/"),
	DOG("Dog/");

	String path;

	private ClassifierType(String path) {
		this.path = path;
	}

	public  String getPath() {
		return path;
	}
}
