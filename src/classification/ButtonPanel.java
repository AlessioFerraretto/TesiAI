package classification;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JPanel;

import common.ScreenshotTaker;

public class ButtonPanel extends JPanel {

	private Button run, saveImage, saveInputs;
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
		saveImage = new Button("img");
		saveImage.addActionListener(e -> {
			ScreenshotTaker.take();
		});
		
		saveInputs = new Button("in");
		saveInputs.addActionListener(e -> {
			
			 ObjectOutputStream oos;
			try {
				oos = new ObjectOutputStream(new FileOutputStream("inputs.dat"));
				oos.writeObject(frame.getInputs());
				oos.close();

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			 
		});
		
		add(run);
		add(saveImage);
		add(saveInputs);

	}

}
