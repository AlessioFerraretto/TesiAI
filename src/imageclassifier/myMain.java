package imageclassifier;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import common.Point;

public class myMain {

	static String pathTrain = "Train/", pathTest = "Test/";;
	static Integer WIDTH = 125, HEIGHT = 125, MAX = 1;

	public static void main(String[] args) {

		Classifier c;

		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("imageClassifier.dat"));
			c = (Classifier) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
			c = new Classifier(WIDTH * HEIGHT * 3);
		}


		for(ClassifierType type : ClassifierType.values()) {
			File folder = new File(pathTrain + type.getPath());
			File[] animals = folder.listFiles();

			for(int i=0; i<animals.length && i < MAX; i++) {
				BufferedImage img = null;
				try {
					img = ImageIO.read(animals[i]);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				BufferedImage resizedImage = resizeImage(img, WIDTH, HEIGHT);


				//				File outputFile = new File("Output25x25/", animal.getName());
				//		        try {
				//					ImageIO.write(resizedImage, "jpg", outputFile);
				//				} catch (Exception e) {
				//					e.printStackTrace();
				//				}

				c.train(animals[i].getName(), resizedImage, type);
			}
		}

		c.save();

		System.out.println("saved");


		for(ClassifierType type : ClassifierType.values()) {
			File folder = new File(pathTrain + type.getPath());
			File[] animals = folder.listFiles();

			for(int i=0; i<animals.length && i < MAX; i++) {
				BufferedImage img = null;
				try {
					img = ImageIO.read(animals[i]);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}					
				BufferedImage resizedImage = resizeImage(img, WIDTH, HEIGHT);
				c.evaluate(resizedImage);

			}
		}
	}

	public static BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
		BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resized.createGraphics();

		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();

		return resized;
	}

}
