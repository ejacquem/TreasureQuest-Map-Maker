package aiMapMaker;

import java.awt.Color;


public enum TileType {
	SAND(		Color.decode("#fcefcf"),	'S'),
	GRASSLAND(	Color.decode("#f3fccf"),	'P'),
	FOREST(		Color.decode("#dcfccf"),	'F'),
	ROCK(		Color.decode("#e5e5e5"),	'R'),
	WATER(		Color.decode("#cff2fc"),	'X'), 
	EMPTY(		Color.DARK_GRAY,	' ');
	
	Color color;
	char letter;
	
	TileType(Color color,char letter){
		this.color = color;
		this.letter = letter;
	}
	
	public static TileType getType(char letter) {
		switch (letter) {
		case 'X':return WATER;
		case 'S':return SAND;
		case 'P':return GRASSLAND;
		case 'F':return FOREST;
		case 'R':return ROCK;
		default :return EMPTY;
		}
	}
}