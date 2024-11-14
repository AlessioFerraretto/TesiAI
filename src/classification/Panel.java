package classification;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ColorUIResource;

import neuralNetwork.RandomSingleton;

public class Panel extends JPanel  {

	public static int DIMENSION = 700;
	public static final int SIZE = 20;
	
	private RepaintListener frame;
	private ArrayList<Point> points = new ArrayList<Point>();
	private ArrayList<Point> determinedPoints = new ArrayList<Point>();
	private long lastPress = 0;
	private static final long timeBetweenPresses = 100;
	private boolean editable;

	public Panel(RepaintListener frame) {
		super();
		this.frame = frame;
		editable = true;

		setFocusable(true);
		requestFocus();
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

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
				} else {
					type = Color.RED;
				}

				points.add(new Point(x,y, type));
				frame.repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

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
		
		for(Point p : determinedPoints) {
			g.setColor(p.getType());
			g.fillRect(p.getX()-1, p.getY()-1, 2, 2);
		}
		
		for(Point p : points) {
			g.setColor(p.getType());
			g.fillRect(p.getX()-SIZE/2, p.getY()-SIZE/2, SIZE, SIZE);
		}
	}

	public void setEditable(boolean b) {
		editable = b;
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	public void setDeterminedPoints(ArrayList<Point> determinedPoints) {
		this.determinedPoints = determinedPoints;
	}


}
