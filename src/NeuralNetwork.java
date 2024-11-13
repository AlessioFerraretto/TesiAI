
import java.util.ArrayList;
import java.util.Arrays;

public class NeuralNetwork {

	private static ArrayList<Neurone> inputLayer, outputLayer;
	private static ArrayList<ArrayList<Neurone>> layers;

	/**
	 * @param input: input size
	 * @param hidden: array containing each hidden layer size
	 * @param output: output size
	 * @throws Exception 
	 */
	public NeuralNetwork(int input, int[] hidden, int output) throws NeuralNetworkException {
		
		if(input < 1) {
			throw new NeuralNetworkException("Wrong input number");
		}
		if(output < 1) {
			throw new NeuralNetworkException("Wrong output number");
		}
		for(int h : hidden) {
			if(h < 1) {
				throw new NeuralNetworkException("Wrong hidden number");
			}
		}
		
		inputLayer = new ArrayList<>();
		outputLayer = new ArrayList<>();
		layers = new ArrayList<ArrayList<Neurone>>();

		//Create inputLayers
		for (int i=0;i<input;i++) {
			Neurone n = new NeuroneInput();
			inputLayer.add(n);
		}
		layers.add(inputLayer);

		//Create hidden layers
		for(int i=0;i<hidden.length;i++) {
			layers.add(addLayer(hidden[i]));
		}

		//Create outputLayers
		for (int i=0;i<output;i++) {
			NeuroneOutput n = new NeuroneOutput();
			n.setPrevious(layers.get(layers.size()-1));
			outputLayer.add(n);
		}
		layers.add(outputLayer);
		
		//Fixes first row of hiddenLayer
//		for (Neurone a : inputLayer) {
//			a.setNext(layers.get(1));
//		}	

		//Fixes last row of hiddenLayer
//		for(Neurone a : layers.get(layers.size()-1)) {
//			a.setNext(outputLayer);
//		}
				

	}

	private ArrayList<Neurone> addLayer(int layerSize) {
		ArrayList<Neurone> hiddenLayer = new ArrayList<Neurone>();
		//Create hidden layer
		for (int i=0;i<layerSize;i++) {
			Neurone n =  new Neurone();
			n.setPrevious(layers.get(layers.size()-1));
			hiddenLayer.add(n);
		}

		//Add Neurone Bias
		Neurone bias = new NeuroneBias();
		hiddenLayer.add(bias);

//		for(Neurone a : layers.get(layers.size()-1)) {
//			a.setNext(hiddenLayer);
//		}

		return hiddenLayer;
	}

	public Float[] evaluate(ArrayList<Input> inputValues) throws NeuralNetworkException {
		return evaluate((Input[]) inputValues.toArray());
	}

	public Float[] evaluate(Input... inputValues) throws NeuralNetworkException {
		if(inputValues.length!=inputLayer.size()) {
			throw new NeuralNetworkException("Input length mismatch");
		}
		for(int i=0;i<inputLayer.size();i++) {
			((NeuroneInput) inputLayer.get(i)).setValue(inputValues[i].getValue());
		}

		Float[] result = new Float[outputLayer.size()];
		for(int i=0;i<outputLayer.size();i++) {
			result[i] = outputLayer.get(i).evaluate();
		}

		return result;
	}
	
	public void test(ArrayList<Input> inputValues, ArrayList<Float> expectedOutputValues) throws NeuralNetworkException {
		test((Input[]) inputValues.toArray(),(Float[]) expectedOutputValues.toArray());
	}

	public void test(Input[] inputValues, Float[] expectedOutputValues) throws NeuralNetworkException {
		evaluate(inputValues);
		
		for(int i=0;i<outputLayer.size();i++) {
			outputLayer.get(i).backpropagation(expectedOutputValues[i]);
		}
		
	}


}
