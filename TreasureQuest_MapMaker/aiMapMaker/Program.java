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

public class Program extends JFrame implements MouseMotionListener, MouseListener, MouseWheelListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final int GRID_SIZE = 20;
	public int UNIT_SIZE = 30;

	public int zoomRatio = 1;
	public int drawMode = 1;
	// 1 = draw, 2 = erase, 3 = movemap, 4 = pick color

	public int mousePosX;
	public int mousePosY;

	public int brushSize = 1;

	int centerX = 0;
	int centerY = 0;
	int oldCenterX = 0;
	int oldCenterY = 0;

	public boolean shiftisPressed;
	public boolean controlisPressed;
	public boolean altisPressed;

	int tileTypeListIndex = 0;
	TileType[] tileTypeList = new TileType[] {TileType.WATER,TileType.SAND,TileType.GRASSLAND,TileType.FOREST,TileType.ROCK};
	
	HashMap<Coordinate, TileType> caseMap = new HashMap<>();
	HashMap<Coordinate, TileType> mouseMap = new HashMap<>();
	MenuBar menu = new MenuBar(this);
	Save save = new Save(caseMap);

	DrawingPanel panel;
	
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

		panel = new DrawingPanel();

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

	class DrawingPanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void paint(Graphics g1) {

			Graphics2D g = (Graphics2D) g1;
			g.setPaint(Color.DARK_GRAY);
			g.fillRect(0, 0, 2000, 2000);

			for (Coordinate c : caseMap.keySet()) {
				int x = (c.getX() + centerX) * UNIT_SIZE;
				int y = (c.getY() + centerY) * UNIT_SIZE;
				g.setPaint(caseMap.get(c).color);
				g.fillRect(x, y, UNIT_SIZE, UNIT_SIZE);

				g.setPaint(Color.white);
				g.drawRect(x, y, UNIT_SIZE, UNIT_SIZE);
//				System.out.println(c);
			}
			for (Coordinate c : mouseMap.keySet()) {
				int x = (c.getX() + centerX) * UNIT_SIZE;
				int y = (c.getY() + centerY) * UNIT_SIZE;

				if(drawMode == 1) g.setPaint(Color.DARK_GRAY);
				else g.setPaint(menu.selectedTile.color);
					
				g.fillRect(x, y, UNIT_SIZE, UNIT_SIZE);
				
				g.setPaint(Color.white);
				g.drawRect(x, y, UNIT_SIZE, UNIT_SIZE);
			}

		}
	}

	private void moveTile(int x, int y) {
		for (Coordinate c : caseMap.keySet()) {
			c = new Coordinate(c.getX() + x, c.getY() + y);
		}
	}
	
	private void Tile(String action, HashMap<Coordinate, TileType> map, int x, int y) {
		if(action.equals("Remove"))map.remove(new Coordinate(x, y));
		if(action.equals("Add"))map.put(new Coordinate(x, y), menu.selectedTile);
	}

	private void addTileAccordingToBrushSize(String action,HashMap<Coordinate, TileType> map, int x, int y) {
		map.put(new Coordinate(x, y), menu.selectedTile);
		Tile(action, map, x, y);
		if (brushSize >= 2) {// size 2 or more, cross shape
			Tile(action, map, x + 1, y);
			Tile(action, map, x - 1, y);
			Tile(action, map, x, y + 1);
			Tile(action, map, x, y - 1);
		}
		if (brushSize >= 3) {// size 3 , 3x3 cube
			Tile(action, map, x + 1, y + 1);
			Tile(action, map, x + 1, y - 1);
			Tile(action, map, x - 1, y + 1);
			Tile(action, map, x - 1, y - 1);
		}
		if (brushSize >= 4) {// size 4, 3x3 + extra border
			Tile(action, map, x + 2, y + 1);
			Tile(action, map, x + 2, y);
			Tile(action, map, x + 2, y - 1);

			Tile(action, map, x - 2, y + 1);
			Tile(action, map, x - 2, y);
			Tile(action, map, x - 2, y - 1);

			Tile(action, map, x + 1, y + 2);
			Tile(action, map, x	, y + 2);
			Tile(action, map, x - 1, y + 2);

			Tile(action, map, x + 1, y - 2);
			Tile(action, map, x	, y - 2);
			Tile(action, map, x - 1, y - 2);

		}
	}

	@Override
	public void mouseDragged(MouseEvent me) {
//		int x = me.getX() - mousePosX;
//		int y = me.getY() - mousePosY;
		mouseMap.clear();
		int x = -centerX + (me.getX() - 8) / UNIT_SIZE;
		int y = -centerY + (me.getY() - 54) / UNIT_SIZE;

		if (drawMode == 1) {
			addTileAccordingToBrushSize("Add",mouseMap,x,y);
			addTileAccordingToBrushSize("Remove",caseMap,x,y);
		}
		if (drawMode == 2) {
			addTileAccordingToBrushSize("Add",caseMap,x,y);
//			caseMap.put(new Coordinate(x, y), menu.selectedTile);
		}
		if (drawMode == 3) {
			x = (me.getX() - mousePosX) / UNIT_SIZE + oldCenterX;
			y = (me.getY() - mousePosY) / UNIT_SIZE + oldCenterY;

			moveTile(x - centerX, x - centerY);

			centerX = x;
			centerY = y;
		}
		if (drawMode == 4) {
			if (caseMap.get(new Coordinate(x, y)) != null) {
				menu.selectedTile = caseMap.get(new Coordinate(x, y));
			}
		}

		System.out.println("Tile : " + x + " , " + y);
		System.out.println("center : " + centerX + " , " + centerY);

//		centerX = oldCenterX + x;
//		centerY = oldCenterY + y;

		this.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		int x = -centerX + (me.getX() - 8) / UNIT_SIZE;
		int y = -centerY + (me.getY() - 54) / UNIT_SIZE;
		mouseMap.clear();
		addTileAccordingToBrushSize("Add",mouseMap,x,y);
//		mouseMap.put(new Coordinate(x, y), null);
		this.repaint();
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

		if (me.getButton() == MouseEvent.BUTTON1) {
//            label.setText("Left Click!");
			if (shiftisPressed)
				drawMode = 3;
			else
				drawMode = 2;
		}
		if (me.getButton() == MouseEvent.BUTTON3) {
//            label.setText("Right Click!");
			drawMode = 1;
		}
		if (me.getButton() == MouseEvent.BUTTON2) {
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
		mouseMap.clear();
		this.repaint();
//		System.out.println("mouse exited");

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (controlisPressed) {
			brushSize = Math.min(Math.max(1, brushSize - e.getWheelRotation()), 4);
//			System.out.println("brushSize = " + brushSize);
		}else if (shiftisPressed) {
			tileTypeListIndex = Math.min(Math.max(0, tileTypeListIndex - e.getWheelRotation()), 4);
			menu.selectedTile = tileTypeList[tileTypeListIndex];
//			System.out.println("Color change = " + tileTypeList[tileTypeListIndex].color.toString());
			drawMode = 2;
		}else
			zoom(e.getWheelRotation());
		mouseMoved(e);
	}

	public void zoom(int i) {
		UNIT_SIZE = Math.min(Math.max(10, UNIT_SIZE - 2 * i), 50);
		this.repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) shiftisPressed = true;
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) controlisPressed = true;
		if (e.getKeyCode() == KeyEvent.VK_ALT) altisPressed = true;
		
		if (e.getKeyCode() == KeyEvent.VK_S) save.saveMap();
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) shiftisPressed = false;
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) controlisPressed = false;
		if (e.getKeyCode() == KeyEvent.VK_ALT) altisPressed = false;
		

	}

}

/*
 * 
 * TileType.WATER, Color.decode("#cff2fc"), TileType.GRASSLAND,
 * Color.decode("#f3fccf"), TileType.FOREST, Color.decode("#dcfccf"),
 * TileType.SAND, Color.decode("#fcefcf"), TileType.ROCK,
 * Color.decode("#e5e5e5"), TileType.UNKNOWN, Color.decode("#000000")
 */
