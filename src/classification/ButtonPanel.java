package classification;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import common.ScreenshotTaker;

public class ButtonPanel extends JPanel {

	private Button run, saveImage;
	private TrainListener frame;
	public ButtonPanel(TrainListener frame) {
		super();
		this.frame = frame;

		setMaximumSize(new Dimension(1000,200));
		run = new Button("Train");
		run.addActionListener(e-> {
			new Thread() {
				@Override
				public void run() {
					frame.train();
				}
			}.start();

		});
		saveImage = new Button("Save image");
		saveImage.addActionListener(e -> {
			ScreenshotTaker.take();
		});
		add(run);
		add(saveImage);

	}

}
