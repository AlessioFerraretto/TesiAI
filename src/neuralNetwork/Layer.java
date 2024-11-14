package neuralNetwork;

public class Layer {
	
	private int size;
	private ActivationFunctionType activationFunctionType;
	
	public Layer(int size, ActivationFunctionType activationFunctionType) {
		this.size = size;
		this.activationFunctionType = activationFunctionType;
	}
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public ActivationFunctionType getActivationFunctionType() {
		return activationFunctionType;
	}
	public void setType(ActivationFunctionType activationFunctionType) {
		this.activationFunctionType = activationFunctionType;
	}

	
}
