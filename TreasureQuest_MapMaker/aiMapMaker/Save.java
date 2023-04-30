package aiMapMaker;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Collectors;

import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

public class Save {

	String textFilePath = ".\\map-sample.txt";
	HashMap<Coordinate, TileType> caseMap;
	
	String mapString;
	
	char[][] charmap;
	
	Save(HashMap<Coordinate, TileType> caseMap){
		this.caseMap = caseMap;
	}
	
	public void saveMap() {
		charmap = toCharMap();
		String text ="";
		try {
			FileWriter writer = new FileWriter(textFilePath);
			for(int i = 0; i<charmap.length-1;i++) {
				text+=charArraytoString(charmap[i])+"\r\n";
			}
			text+=charArraytoString(charmap[charmap.length-1]);
			mapString = text;
			writer.append(text);
			writer.close();
		} catch (Exception e) {}
	}
	
	private char[][] toCharMap() {
		if(caseMap.isEmpty())return new char[][] {{' '}};
		int a = 1_000_000;
		int minx = a, miny=a, maxx = -a, maxy=-a;
		for (Coordinate c : caseMap.keySet()) {
			minx = Math.min(c.getX(), minx);
			miny = Math.min(c.getY(), miny);
			maxx = Math.max(c.getX(), maxx);
			maxy = Math.max(c.getY(), maxy);
		}
		int width = maxx-minx+1;
		int height = maxy-miny+1;
		System.out.println("minx :" + minx + "maxx :" + maxx);
		System.out.println("HEIGHT :" + height + "WIDTH :" + width);
		
		//had to inverse height and width here for some reason
		char[][] map = new char[height][width];
		System.out.println("START SAVING          \n\n");
		for(int i = 0; i<height;i++) {
			for(int j = 0; j<width;j++) {
				Coordinate coo = new Coordinate(j+minx, i+miny);//same here
				if(caseMap.get(coo)!=null) {
					map[i][j] = caseMap.get(coo).letter;
					System.out.println("Saved case at : " + coo);
				}else map[i][j] = ' ';
			}
		}
		return map;
	}
	
	public void saveToClipBoard() {
		saveMap();
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(new StringSelection(mapString), null);
	}
	
	private String charArraytoString(char[] arr) {
		String str = "";
		for(char c :arr) {
			str += c;
		}
		return str;
	}
	
	public void textFileToMap() {
		char[][] mapData = parseFile();
		for (int i = 0; i < mapData.length; i++) {
			for (int j = 0; j < mapData[i].length; j++) {
				TileType type = TileType.getType(mapData[i][j]);
				if (type != TileType.UNKNOWN)
					caseMap.put(new Coordinate(j, i), type);
			}
		}
	}
	/**
	 * Convertit le contenu d'un fichier en un tableau de caractères.
	 * 
	 * @author Nicolas Hendrikx
	 */
	public char[][] parseFile() {
		Path filePath = Paths.get(textFilePath);
		
		try(var reader = Files.newBufferedReader(filePath)) {
			return reader
				.lines()
				.filter(line -> !line.startsWith("#"))
				.filter(line -> !line.isBlank())
				.map(line -> line.toCharArray())
				.collect(Collectors.toList())
				.toArray(new char[0][]);
		} catch(IOException ioe) {
//			throw new RuntimeException(ioe);
		}
		return new char[][] {};
	}
	
}
