package classification;

import java.awt.Dimension;

import javax.swing.JPanel;

public class ButtonPanel extends JPanel {

	private Button run;
	private TrainListener frame;
	public ButtonPanel(TrainListener frame) {
		super();
		this.frame = frame;
		
		setMaximumSize(new Dimension(1000,200));
		run = new Button("Train");
		run.addActionListener(e->frame.train());
		add(run);

	}

}
