package neuralNetwork;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class NeuralNetwork implements Serializable {

	private ArrayList<Neurone> inputLayer, outputLayer;
	private ArrayList<ArrayList<Neurone>> layers;

	/**
	 * @param input: input size
	 * @param hidden: array containing each hidden layer size
	 * @param output: output size
	 * @throws Exception 
	 */
	protected NeuralNetwork(int input, ArrayList<Layer> hidden, Layer output) throws NeuralNetworkException {

		if(input < 1) {
			throw new NeuralNetworkException("Wrong input number");
		}
		if(output.getSize() < 1) {
			throw new NeuralNetworkException("Wrong output number");
		}
		for(Layer h : hidden) {
			if(h.getSize() < 1) {
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
		Neurone bias = new NeuroneBias(hidden.get(0).getActivationFunctionType());
		inputLayer.add(bias);

		layers.add(inputLayer);

		//Create hidden layers
		for(int i=0;i<hidden.size();i++) {
			layers.add(addLayer(hidden.get(i).getSize(), hidden.get(i).getActivationFunctionType()));
		}

		//Create outputLayers
		for (int i=0;i<output.getSize();i++) {
			NeuroneOutput n = new NeuroneOutput(output.getActivationFunctionType());
			outputLayer.add(n);
		}
		layers.add(outputLayer);

		//Connect layers
		for(int i=0;i<layers.size()-1;i++) {
			for(int j=0;j<layers.get(i).size();j++) {

				for (Neurone n : layers.get(i+1)) {
					if(!(n instanceof NeuroneBias)) {
						Arco a = new Arco(layers.get(i).get(j), n);

						layers.get(i).get(j).addNext(a);
						n.addPrevious(a);
					}
				}

			}
		}

	}

	private ArrayList<Neurone> addLayer(int layerSize, ActivationFunctionType hiddenActivationFunction) {
		ArrayList<Neurone> hiddenLayer = new ArrayList<Neurone>();
		//Create hidden layer
		for (int i=0;i<layerSize;i++) {
			Neurone n =  new Neurone(hiddenActivationFunction);
			hiddenLayer.add(n);
		}
		//Add Neurone Bias
		Neurone bias = new NeuroneBias(hiddenActivationFunction);
		hiddenLayer.add(bias);

		return hiddenLayer;
	}

	public Float[] evaluate(ArrayList<Input> inputValues) throws NeuralNetworkException {
		return evaluate((Input[]) inputValues.toArray());
	}

	public Float[] evaluate(Input... inputValues) throws NeuralNetworkException {
		if(inputValues.length!=inputLayer.size()-1) {
			throw new NeuralNetworkException("Input length mismatch");
		}
		for(int i=0;i<inputLayer.size()-1;i++) {
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

		for (int i=0;i<outputLayer.size();i++) {
			float actualOutput = outputLayer.get(i).getLastOutput();
			float error = actualOutput - expectedOutputValues[i];
			float gradient = NeuralNetworkSettings.derivate(outputLayer.get(i).getActivationFunctionType(), outputLayer.get(i).getLastOutputX());

			outputLayer.get(i).setError(error * gradient);

			for (Arco a : outputLayer.get(i).getPrevious()) {
				float input = a.getFrom().getLastOutput();
				float weightChange = - NeuralNetworkSettings.getLearningRate() * 
						outputLayer.get(i).getError() * input;
				if(NeuralNetworkSettings.getUseInertia()) {
					weightChange += NeuralNetworkSettings.getAlpha() * a.getPreviousWeightChange();
				}
				a.updateWeight(weightChange);
			}
		}

		//Backpropagate through hidden layers
		for (int i = layers.size() - 2; i > 0; i--) {
			ArrayList<Neurone> currentLayer = layers.get(i);

			for (Neurone neuron : currentLayer) {
				//Calculate the error signal for this neuron
				float errorSum = 0.0f;
				for (Arco a : neuron.getNext()) {
					errorSum += a.getWeight() * a.getTo().getError();
				}

				float gradient = NeuralNetworkSettings.derivate(neuron.getActivationFunctionType(), neuron.getLastOutputX());

				neuron.setError(errorSum * gradient);

				// Update weights for this hidden neuron
				for (Arco a : neuron.getPrevious()) {
					float input = a.getFrom().getLastOutput();
					float weightChange = - NeuralNetworkSettings.getLearningRate() * 
							neuron.getError() * input;
					if(NeuralNetworkSettings.getUseInertia()) {
						weightChange += NeuralNetworkSettings.getAlpha() * a.getPreviousWeightChange();
					}

					a.updateWeight(weightChange);

				}
			}
		}

	}

	public static NeuralNetwork load(String f) {
		NeuralNetwork nn = null;
		try {
			FileInputStream fin = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fin);
			nn = (NeuralNetwork) ois.readObject();
			ois.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nn;
	}

	public void save(String f) {
		try {
			FileOutputStream fout = new FileOutputStream(f, true);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(this);
			oos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}