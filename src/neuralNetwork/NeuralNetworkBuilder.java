package neuralNetwork;

import java.util.ArrayList;

public class NeuralNetworkBuilder {

	private int in;
	private ArrayList<Layer> hidden;
	private Layer out;
	
	private NeuralNetworkBuilder() {
		hidden = new ArrayList<Layer>();
	}
	
	public static NeuralNetworkBuilder Builder() {
		return new NeuralNetworkBuilder();
	}
	
	public NeuralNetworkBuilder input(int in) {
		this.in = in;
		return this;
	}
	
	public NeuralNetworkBuilder output(int out, ActivationFunctionType type) {
		this.out = new Layer(out, type);
		return this;
	}
	
	public NeuralNetworkBuilder hidden(int size, ActivationFunctionType type) {
		hidden.add(new Layer(size, type));
		return this;
	}
	
	public NeuralNetwork build() {
		NeuralNetwork nn = null;
		try {
			nn = new NeuralNetwork(in, hidden, out);
		} catch (NeuralNetworkException e) {
			e.printStackTrace();
		}
		
		return nn;
	}
	
}
