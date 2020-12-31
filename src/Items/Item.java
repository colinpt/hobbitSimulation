package Items;

import java.awt.Color;
import java.util.Random;

public class Item {
	protected Random r = new Random();
	protected Color color;
	protected double drawSize;
	protected int x;
	protected int y;
	
	public Item(int xPos, int yPos){
		this.x = xPos;
		this.y = yPos;
	}
	public Item(Item toClone) {
		this.x = toClone.getX();
		this.y = toClone.getY();
		this.color = toClone.getColor();
		this.drawSize = toClone.getSize();
	}
	public Color getColor() {
		return this.color;
	}
	
	public void setColor(Color c) {
		this.color = c;
	}
	
	public double getSize() {
		return this.drawSize;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
