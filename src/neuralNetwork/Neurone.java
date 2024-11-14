package neuralNetwork;

import java.util.ArrayList;

public class Neurone {
	public static int N = 0;
	
	private int id;
	protected ActivationFunctionType activationFunctionType;
	protected ArrayList<Arco> previous, next;

	private float lastOutput, error;

	public Neurone(ActivationFunctionType activationFunctionType) {
		this.activationFunctionType = activationFunctionType;
		id = N;
		N++;
		previous = new ArrayList<>();
		next = new ArrayList<>();
	}

	public ArrayList<Arco> getPrevious() {
		return previous;
	}

	public ArrayList<Arco> getNext() {
		return next;
	}

	protected Float evaluate() {
		float f = 0;
		for(int i=0;i<previous.size();i++) {
			f += previous.get(i).getWeight()*previous.get(i).getFrom().evaluate();
		}
		
		lastOutput = NeuralNetworkSettings.activationFuncion(activationFunctionType, f);
		if(Float.isNaN(lastOutput)) {
			System.out.println("b");
			System.exit(0);
		}
 		return lastOutput;
	}

	public Float getLastOutput() {
		return lastOutput;
	}

	public void setError(float error) {
		this.error = error;
	}

	public float getError() {
		return error;
	}

	public ActivationFunctionType getActivationFunctionType() {
		return activationFunctionType;
	}

	public void addNext(Arco a) {
		next.add(a);
	}

	public void addPrevious(Arco a) {
		previous.add(a);
	}
	
	public String toString() {
		String p ="";
		String n ="";
		for(Arco a : previous) {
			p += a.toString();
		}
		for(Arco a : next) {
			n += a.toString();
		}
		return getClass().getName()+ " id: " + id + "\nPrev: " + p +"\nNext: "+ n;
	}

	public int getId() {
		return id;
	}


}
