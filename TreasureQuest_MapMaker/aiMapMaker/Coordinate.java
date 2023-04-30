package aiMapMaker;

import java.util.Objects;

public class Coordinate {

	private int posX;
	private int posY;
	
	Coordinate(Coordinate coo){
		this(coo.posX,coo.posY);
	}

	Coordinate(int x, int y){
		this.posX = x;
		this.posY = y;
	}
	
	public int getX() {
		return posX;
	}

	public int getY() {
		return posY;
	}

	public Coordinate add(int x, int y) {
		return new Coordinate(posX+x,posY+y);
	}

	@Override
	public int hashCode() {
		return Objects.hash(posX, posY);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		return posX == other.posX && posY == other.posY;
	}

	@Override
	public String toString() {
		return "Coordinate [posX=" + posX + ", posY=" + posY + "]";
	}
	
}