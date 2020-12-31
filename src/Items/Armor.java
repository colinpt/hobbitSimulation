package Items;

import java.awt.Color;

public class Armor extends Equipable{
	protected int moveSpeed;
	protected String type;
	public Armor(int xPos, int yPos, String name, String type, int damage, 
			int armor, int maxHP, int detection, int moveSpeed, int rarity) {
		super(xPos, yPos, name, damage, armor, maxHP, detection, rarity);
		this.moveSpeed = moveSpeed;
		this.type = type;
		this.setColor(new Color(110,80,60));
	}
	public int getMoveSpeed() {
		return moveSpeed;
	}
	public String getType() {
		return type;
	}

}
