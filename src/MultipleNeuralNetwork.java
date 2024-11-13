//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MultipleNeuralNetwork {
//
//	private ArrayList<NeuralNetwork> nn;
//	private ArrayList<Double[]> lastResults;
//	private int inputs, outputs, quantity, numberOfModels;
//	private double mutationProbability = EvolutionaryConfiguration.MUTATION_PROBABILITY, mutationQuantity = EvolutionaryConfiguration.MUTATION_QUANTITY;
//	private static int generation = 1;
//
//	/**
//	 * @param inputs: number of inputs
//	 * @param outputs: number of outputs
//	 * @param quantity: quantity of neural networks
//	 * @param nModels: number of models to evolve
//	 */
//	public MultipleNeuralNetwork(int inputs, int outputs, int quantity, int nModels) {
//		if(inputs<1) {
//			inputs = 1;
//		}
//		if(outputs<1) {
//			outputs = 1;
//		}
//		if(quantity<1) {
//			quantity = 1;
//		}
//		if(nModels<=0) {
//			nModels = 1;
//		}
//		nn = new ArrayList<>();
//
//		this.inputs = inputs;
//		this.outputs = outputs;
//		this.quantity = quantity;
//		this.numberOfModels = nModels;
//
//		for(int i=0;i<quantity;i++) {
//			nn.add(new NeuralNetwork(this.inputs, this.outputs));
//		}
//	}
//
//	public ArrayList<Double[]> evaluate(Input... arrayInputs) throws Exception {
//		lastResults = new ArrayList<>();
//
//		for(NeuralNetwork n : nn) {
//			lastResults.add(n.evaluate(arrayInputs));
//		}
//
//		return lastResults;
//	}
//
//	public ArrayList<Double[]> evaluate(ArrayList<ArrayList<Input>> arrayInputs) throws Exception {
//		lastResults = new ArrayList<>();
//
//		for(int i=0;i<nn.size();i++) {
//			lastResults.add(nn.get(i).evaluate(arrayInputs.get(i)));
//		}
//
//		return lastResults;
//	}
//
//	public void score(ScoreInterface scoreInterface) {
//		List<Integer> bestScores = scoreInterface.getBestScoreIndexArray();
//
//		NeuralNetwork[] bests = new NeuralNetwork[numberOfModels];
//		for(int i=0;i<numberOfModels;i++) {
//			bests[i] = nn.get(bestScores.get(i));
//		}
//
//		generation++;
//		regenerate(bests);		
//	}
//
//	private void regenerate(NeuralNetwork... models) {
//		nn = new ArrayList<>();
//		//Mantengo i modelli migliori
//		for(NeuralNetwork m : models) {
//			nn.add(m);
//		}
//
//		for(int i=0;i<quantity-numberOfModels;i++) {
//			nn.add(new NeuralNetwork(inputs, outputs, models[numberOfModels * ((int) (RandomSingleton.random()))], mutationProbability, mutationQuantity));
//		}
//
//	}
//
//	public int getGeneration() {
//		return generation;
//	}
//
//}
