package aiMapMaker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * Permet de créer plus facilement une carte du jeu "Treasure Quest"
 * 
 * @author Lucas Jacquemin
 *
 */

public class Program extends JFrame implements MouseMotionListener, MouseListener, MouseWheelListener , KeyListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final int GRID_SIZE = 20;
	public int UNIT_SIZE = 30;
	
	public int zoomRatio = 1;
	public int drawMode = 1;
	//1 = draw, 2 = erase, 3 = movemap, 4 = pick color
	
	public int mousePosX;
	public int mousePosY;
	
	int centerX = 0;
	int centerY = 0;
	int oldCenterX = 0;
	int oldCenterY = 0;
	
	public boolean shiftisPressed;
	
	HashMap<Coordinate, TileType> caseMap = new HashMap<>();
	MenuBar menu = new MenuBar(this);
	Save save = new Save(caseMap);

	public static void main(String[] args) {
		new Program();
	}	
	
	Program() {
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
//		setLayout(new BorderLayout());
		setSize(1080, 720);
//		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("ai Map Maker");
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addKeyListener(this);
		
		DrawingPanel panel = new DrawingPanel();
		
		setJMenuBar(menu);
		add(panel);
		
		save.textFileToMap();
		
		panel.repaint();
		setVisible(true);
	}
	
//	
//	private Component makeSettingsPanel() {
//		// TODO Auto-generated method stub
//		JPanel p = new JPanel();
//		return new JPanel();
//	}

	class DrawingPanel extends JPanel{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
			
		public void paint(Graphics g1) {
			
			Graphics2D g = (Graphics2D) g1;
			g.setPaint(Color.DARK_GRAY);
			g.fillRect(0,0,2000,2000);
			
			for (Coordinate c : caseMap.keySet()) {
				int x = (c.getX()+centerX)*UNIT_SIZE;
				int y = (c.getY()+centerY)*UNIT_SIZE;
				g.setPaint(caseMap.get(c).color);
				g.fillRect(x,y, UNIT_SIZE,UNIT_SIZE);
				
				g.setPaint(Color.white);
				g.drawRect(x, y, UNIT_SIZE, UNIT_SIZE);
//				System.out.println(c);
			}			
		}
	}

	private void moveTile(int x,int y) {
		for (Coordinate c : caseMap.keySet()) {
			c = new Coordinate(c.getX()+x,c.getY()+y);
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent me) {
//		int x = me.getX() - mousePosX;
//		int y = me.getY() - mousePosY;
		int x = -centerX + (me.getX()-8)/UNIT_SIZE;
		int y = -centerY + (me.getY()-54)/UNIT_SIZE;
		
		if(drawMode == 1) {
			if(caseMap.get(new Coordinate(x,y)) != null)caseMap.remove(new Coordinate(x,y));			
		}
		if(drawMode == 2) {
			caseMap.put(new Coordinate(x,y),menu.selectedTile);			
		}
		if(drawMode == 3) {
			x = (me.getX() - mousePosX)/UNIT_SIZE + oldCenterX;
			y = (me.getY() - mousePosY)/UNIT_SIZE + oldCenterY;
			
			moveTile(x-centerX, x-centerY);
			
			centerX = x;
			centerY = y;
		}
		if(drawMode == 4) {
			if(caseMap.get(new Coordinate(x,y)) != null) {
				menu.selectedTile = caseMap.get(new Coordinate(x,y));
			}
		}
		
		System.out.println("Tile : "+x+" , "+y);
		System.out.println("center : "+centerX+" , "+centerY);
		
//		centerX = oldCenterX + x;
//		centerY = oldCenterY + y;
		
		
		this.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseDragged(e);
	}

	@Override
	public void mousePressed(MouseEvent me) {
		mousePosX = me.getX();
		mousePosY = me.getY();
		
		if(me.getButton() == MouseEvent.BUTTON1) {
//            label.setText("Left Click!");
			if(shiftisPressed) drawMode = 3;
			else drawMode = 2;
          }
          if(me.getButton() == MouseEvent.BUTTON3) {
//            label.setText("Right Click!");
        	  drawMode = 1;
          }
          if(me.getButton() == MouseEvent.BUTTON2) {
//            label.setText("middle Click!");
        	  drawMode = 4;
          }
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		oldCenterX = centerX;
		oldCenterY = centerY;
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		zoom(e.getWheelRotation());
	}
	public void zoom(int i) {
		UNIT_SIZE = Math.min(Math.max(10, UNIT_SIZE-2*i), 50);
		this.repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
			shiftisPressed = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_S) {
			save.saveMap();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
			shiftisPressed = false;
		}
		
	}
	
	
}

/*
 * 
 * TileType.WATER, Color.decode("#cff2fc"), TileType.GRASSLAND,
 * Color.decode("#f3fccf"), TileType.FOREST, Color.decode("#dcfccf"),
 * TileType.SAND, Color.decode("#fcefcf"), TileType.ROCK,
 * Color.decode("#e5e5e5"), TileType.UNKNOWN, Color.decode("#000000")
 */
