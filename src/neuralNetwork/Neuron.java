package neuralNetwork;

import java.io.Serializable;
import java.util.ArrayList;

import common.NumberController;

public class Neuron implements Serializable {
	public static int N = 0;
	
	private int id;
	protected ActivationFunctionType activationFunctionType;
	protected ArrayList<Connection> previous, next;
	
	private boolean dropout;

	private float lastOutputX, lastOutputY, error;

	public Neuron(ActivationFunctionType activationFunctionType) {
		this.activationFunctionType = activationFunctionType;
		id = N;
		N++;
		previous = new ArrayList<>();
		next = new ArrayList<>();
		dropout = false;
	}

	public ArrayList<Connection> getPrevious() {
		return previous;
	}

	public ArrayList<Connection> getNext() {
		return next;
	}

	protected Float evaluate() {
		if(dropout) {
			return 0f;
		}
		
		float sum = 0;
		for(int i=0;i<previous.size();i++) {
			sum += previous.get(i).getWeight()*previous.get(i).getFrom().evaluate();
		}

		lastOutputX = sum;
		lastOutputY = NeuralNetworkSettings.activationFunction(activationFunctionType, sum);
		
		lastOutputY = NumberController.check(lastOutputY, 4);
		
		if(dropout) 	 {
		    lastOutputY *= 1 / (1 - NeuralNetworkSettings.getDropoutRate());  // Scala l'output per compensare il dropout nel training
		}
		
 		return lastOutputY;
	}

	private Float getLastOutputY() {
		return lastOutputY;
	}

	public Float getLastOutputX() {
		return lastOutputX;
	}
	
	public void setError(float error) {
		this.error = error;
		
		error = NumberController.check(error, 2);
	}

	public float getError() {
		return error;
	}

	public ActivationFunctionType getActivationFunctionType() {
		return activationFunctionType;
	}

	public void addNext(Connection a) {
		next.add(a);
	}

	public void addPrevious(Connection a) {
		previous.add(a);
	}
	
	public String toString() {
		String p ="";
		String n ="";
		for(Connection a : previous) {
			p += a.toString();
		}
		for(Connection a : next) {
			n += a.toString();
		}
		return getClass().getName()+ " id: " + id + "\nPrev: " + p +"\nNext: "+ n;
	}

	public int getId() {
		return id;
	}

	public Float getLastOutput() {
		return getLastOutputY();
	}

	public void setDropout(boolean b) {
		dropout = b;
	}

}
