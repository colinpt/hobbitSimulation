package Terrain;
import java.awt.Color;
import java.util.Random;

import Game.Map;
import Items.*;

public class Terrain {
	protected static Random r = new Random();
	private boolean passable;
	private Color color;
	protected int x;
	protected int y;
	
	public Terrain(boolean passable, Color color, int xPos, int yPos) {
		this.passable = passable;
		this.color = color;
		this.x = xPos;
		this.y = yPos;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	protected void setColor(Color c) {
		this.color = c;
	}
	
	public boolean isPassable() {
		return passable;
	}
	
	public void update(Map map) {
		int seed = r.nextInt(10000);
		if (seed == 0) {
			Item item = map.itemPool.getRandomItem();
			item.setPos(x, y);
			map.placeItem(item);
		}
	}
}
