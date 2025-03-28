package classification;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import common.Point;

public class Panel extends JPanel  {

	public static final int DIMENSION = 700, SIZE = 20, PREDICTED_SIZE = 2;
	private static final long timeBetweenPresses = 100;
	
	private RepaintListener frame;
	private ArrayList<Point> points;
	private ArrayList<Point> predictedPoints;
	private long lastPress;
	private boolean editable;

	public Panel(RepaintListener frame) {
		super();
		this.frame = frame;
		points = new ArrayList<Point>();
		predictedPoints = new ArrayList<Point>();
		editable = true;
		lastPress = 0;

		setFocusable(true);
		requestFocus();
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if(!editable) {
					return;
				}
				if(lastPress + timeBetweenPresses > System.currentTimeMillis()) {
					return;
				}
				lastPress = System.currentTimeMillis();
				int x = (int) e.getX();
				int y = (int) e.getY();

				if(x<SIZE/2 || x>DIMENSION-SIZE/2) {
					return;
				}
				if(y<SIZE/2 || y>DIMENSION-SIZE/2) {
					return;
				}

				Color type;
				
				if (SwingUtilities.isRightMouseButton(e)) {
					type = Color.BLUE;
				} else if(SwingUtilities.isMiddleMouseButton(e)) {
					type = Color.GREEN;
				}
				else {
					type = Color.RED;
				}
				
				
				

				points.add(new Point(type, x, y));
				frame.repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		
		

	}

	public void paint(Graphics g) {
		super.paint(g);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, DIMENSION, DIMENSION);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, DIMENSION, DIMENSION);
		
		for(Point p : predictedPoints) {
			g.setColor(p.getType());
			g.fillRect((int) (p.getInput(0)-PREDICTED_SIZE/2),(int) (p.getInput(1)-PREDICTED_SIZE/2), PREDICTED_SIZE, PREDICTED_SIZE);
		}
		
		for(Point p : points) {
			g.setColor(p.getType());
			g.fillRect((int) (p.getInput(0)-SIZE/2),(int) (p.getInput(1)-SIZE/2), SIZE, SIZE);
		}
	}

	public void setEditable(boolean b) {
		editable = b;
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}
	
	public void setPredictedPoints(ArrayList<Point> predictedPoints) {
		this.predictedPoints = predictedPoints;
	}


}
