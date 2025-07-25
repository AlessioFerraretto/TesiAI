package imageclassifier;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import common.Point;
import common.ProgressBarUtil;
import neuralNetwork.ActivationFunctionType;
import neuralNetwork.Input;
import neuralNetwork.InputType;
import neuralNetwork.NeuralNetwork;
import neuralNetwork.NeuralNetworkBuilder;
import neuralNetwork.NeuralNetworkException;
import neuralNetwork.NeuralNetworkSettings;

public class Classifier implements Serializable {

	private NeuralNetwork nn;
	private ArrayList<String> images;
	private int IN, OUT = 2;

	public Classifier(Integer in) {
		this.IN = in;
		images = new ArrayList<String>();

		NeuralNetworkSettings.setUseDropout(false);
		NeuralNetworkSettings.setDropoutRate(0.1f);
		NeuralNetworkSettings.setUseInertia(true);

		nn = NeuralNetworkBuilder.Builder()
				.input(IN)
				.hidden(125, ActivationFunctionType.SIGMOID)
				.hidden(30, ActivationFunctionType.SIGMOID)
				.hidden(5, ActivationFunctionType.SIGMOID)
				.output(OUT, ActivationFunctionType.SIGMOID)
				.build();
	}


	public void train(String name, BufferedImage image, ClassifierType type) {
		if(images.contains(name)) {
			return;
		}
		images.add(name);

		Input[] in = new Input[nn.getIn()];
		Float[] out = new Float[nn.getOut()];

		for(int i=0;i<image.getWidth();i++) {
			for(int j=0;j<image.getHeight();j++) {
				int rgb = image.getRGB(i, j);

				int red   = (rgb >> 16) & 0xFF;
				int green = (rgb >> 8)  & 0xFF;
				int blue  = rgb & 0xFF;

				int index = (i*image.getHeight() + j) *3;

				in[index] = new Input(red, InputType.COLOR);
				in[index + 1] = new Input(green, InputType.COLOR);
				in[index + 2] = new Input(blue, InputType.COLOR);
			}
		}

		out[0] = type.ordinal() == 0 ? 1f : 0f;
		out[1] = type.ordinal() == 1 ? 1f : 0f;

		System.out.println(type.ordinal());

		try {
			nn.train(in, out);
		} catch (NeuralNetworkException e) {
			e.printStackTrace();
		}
	}

	public void evaluate(BufferedImage image) {

		Input[] in = new Input[nn.getIn()];

		for(int i=0;i<image.getWidth();i++) {
			for(int j=0;j<image.getHeight();j++) {
				int rgb = image.getRGB(i, j);

				int red   = (rgb >> 16) & 0xFF;
				int green = (rgb >> 8)  & 0xFF;
				int blue  = rgb & 0xFF;

				int index = (i*image.getHeight() + j) *3;

				in[index] = new Input(red, InputType.COLOR);
				in[index + 1] = new Input(green, InputType.COLOR);
				in[index + 2] = new Input(blue, InputType.COLOR);
			}
		}

		try {
			Float[] outputs = nn.feedForward(in);
			System.out.print("GATTO: " + outputs[0] + " ");
			System.out.print("CANE: " + outputs[1] + " ");

			System.out.println();
		} catch (NeuralNetworkException e) {
			e.printStackTrace();
		}
	}


	public void save() {
		try {
			FileOutputStream fout = new FileOutputStream("imageClassifier.dat", true);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(this);
			oos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
