package Items;

import java.awt.Color;

public class Weapon extends Equipable{
	protected int range;
	protected int dot; // Damage over time
	public Weapon(int xPos, int yPos, String name, int damage, 
			int armor, int maxHP, int detection, int range, int dot, int rarity) {
		super(xPos, yPos, name, damage, armor, maxHP, detection, rarity);
		this.range = range;
		this.dot = dot;
		
		this.setColor(new Color(125,125,125));
	}
	

	public int getRange() {
		return range;
	}
	public int getDot() {
		return dot;
	}

}
