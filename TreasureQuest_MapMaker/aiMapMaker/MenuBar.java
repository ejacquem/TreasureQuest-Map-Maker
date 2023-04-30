package aiMapMaker;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

public class MenuBar extends JMenuBar implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	TileType selectedTile = TileType.WATER;

	JMenu tileMenu = new JMenu("TileType");
	JMenu saveMenu = new JMenu("Save");
	JMenu controlMenu = new JMenu("Control");
	
	JMenuItem OpenSaveFolder = new JMenuItem("Open Save Folder");
	JMenuItem SaveFile = new JMenuItem("SaveFile");
	JMenuItem CopyToClipBoard = new JMenuItem("Copy To ClipBoard");
	
	ButtonGroup groupTile = new ButtonGroup();
	JRadioButtonMenuItem water = new JRadioButtonMenuItem("Water");
	JRadioButtonMenuItem sand = new JRadioButtonMenuItem("Sand");
	JRadioButtonMenuItem grass = new JRadioButtonMenuItem("Grass");
	JRadioButtonMenuItem forest = new JRadioButtonMenuItem("Forest");
	JRadioButtonMenuItem rock = new JRadioButtonMenuItem("Rock");
	
	Program p;

	MenuBar(Program p){
		this.p = p;
		add(tileMenu);
		add(saveMenu);
		add(controlMenu);
		
		addAncestorListener(null);
		
		saveMenu.add(OpenSaveFolder);
		saveMenu.addSeparator();
		saveMenu.add(SaveFile);
		saveMenu.add(CopyToClipBoard);
		
		water.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD0, 0));
		sand.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD1, 0));
		grass.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, 0));
		forest.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD3, 0));
		rock.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0));
		
		SaveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		CopyToClipBoard.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		
		groupTile.add(water);
		groupTile.add(sand);
		groupTile.add(grass);
		groupTile.add(forest);
		groupTile.add(rock);
		
		tileMenu.add(water);
		tileMenu.add(sand);
		tileMenu.add(grass);
		tileMenu.add(forest);
		tileMenu.add(rock);
		
		JMenuItem test;
		test = new JMenuItem("Left_Click   | draw");
		test.setFont(new Font(Font.MONOSPACED, 1, 12));
		controlMenu.add(test);
		test = new JMenuItem("Middle_Click | pick color");
		test.setFont(new Font(Font.MONOSPACED, 1, 12));
		controlMenu.add(test);
		test = new JMenuItem("Right_Click  | erase");
		test.setFont(new Font(Font.MONOSPACED, 1, 12));
		controlMenu.add(test);
		test = new JMenuItem("Shift+Left_Click | move the map");
		test.setFont(new Font(Font.MONOSPACED, 1, 12));
		controlMenu.addSeparator();
		controlMenu.add(test);
		
		water.addActionListener(this);
		sand.addActionListener(this);
		grass.addActionListener(this);
		forest.addActionListener(this);
		rock.addActionListener(this);
		OpenSaveFolder.addActionListener(this);
		SaveFile.addActionListener(this);
		CopyToClipBoard.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == water) selectedTile = TileType.WATER;		
		if(e.getSource() == sand) selectedTile = TileType.SAND;		
		if(e.getSource() == grass) selectedTile = TileType.GRASSLAND;		
		if(e.getSource() == forest) selectedTile = TileType.FOREST;		
		if(e.getSource() == rock) selectedTile = TileType.ROCK;
		if(e.getSource() == SaveFile) p.save.saveMap();
		if(e.getSource() == CopyToClipBoard) p.save.saveToClipBoard();
		if(e.getSource() == OpenSaveFolder) {
			try {
				Desktop.getDesktop().open(new File(".\\"));
			} catch (Exception e2) {}
		}
	}
}
