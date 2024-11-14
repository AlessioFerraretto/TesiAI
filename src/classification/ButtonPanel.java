package classification;

import java.awt.Dimension;

import javax.swing.JPanel;

public class ButtonPanel extends JPanel {

	private Button run;
	private RunListener frame;
	public ButtonPanel(RunListener frame) {
		super();
		this.frame = frame;
		
		setMaximumSize(new Dimension(1000,200));
		run = new Button("Run");
		run.addActionListener(l->frame.run());
		add(run);

	}

}
