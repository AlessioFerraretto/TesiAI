
import java.util.ArrayList;

public class Neurone {

	protected ArrayList<Arco> previous, next;

	private float lastOutput;

	public Neurone() {
		previous = new ArrayList<>();
//		next = new ArrayList<>();
	}

	public ArrayList<Arco> getPrevious() {
		return previous;
	}

//	public ArrayList<Arco> getNext() {
//		return next;
//	}

//	public void setNext(ArrayList<Neurone> ne) {
//		next = new ArrayList<>();
//		for (Neurone n : ne) {
//			if(!(n instanceof NeuroneBias)) {
//				Arco a = new Arco(this, n);
//				next.add(a);
//			}
//		}		
//	}

	public void setPrevious(ArrayList<Neurone> pr) {
		previous = new ArrayList<>();
		for (Neurone n : pr) {
			Arco a = new Arco(n, this);
			previous.add(a);
		}

	}

	protected Float evaluate() {
		float f = 0;
		for(int i=0;i<previous.size();i++) {
			f += previous.get(i).getWeight()*previous.get(i).getFrom().evaluate();
		}
		
		lastOutput = NeuralNetworkSettings.activationFuncion(f);
		return lastOutput;
	}


	public void backpropagation(float targetOutput) {
		for(Arco p : previous) {
			p.backpropagation(targetOutput);
		}
	}

	public Float getLastOutput() {
		return lastOutput;
	}

}
