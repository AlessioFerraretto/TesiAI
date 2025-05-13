package common;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import classification.Frame;
import classification.Panel;

public class ScreenshotTaker {

	private static int i=0;
	
	public static void take() {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = (int) screenSize.getWidth();
		int h = (int) screenSize.getHeight();

		take((w-Panel.DIMENSION) / 2 - 141, (h-Panel.DIMENSION) / 2 + 11, Panel.DIMENSION, Panel.DIMENSION);
		
	}
	public static void take(int x, int y, int w, int h) {
		
		Robot r;
		try {
			r = new Robot();
			BufferedImage img = r.createScreenCapture(new Rectangle(x,y,w,h));
			
			i++;
			File f = new File("images/image"+i+".png");
			ImageIO.write(img, "png", f);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
