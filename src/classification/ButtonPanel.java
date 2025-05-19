package classification;

import java.awt.Dimension;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.swing.JPanel;


public class ButtonPanel extends JPanel {

	private Button run, saveInputs;
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
		add(saveInputs);

	}

}
