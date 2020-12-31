package Items;

import java.awt.Color;

public class Accessory extends Equipable{
	protected int moveSpeed;
	protected int passiveHP;
	public Accessory(int xPos, int yPos, String name, int damage, 
			int armor, int maxHP, int detection, int moveSpeed, int passiveHP, int rarity) {
		super(xPos, yPos, name, damage, armor, maxHP, detection, rarity);
		this.moveSpeed = moveSpeed;
		this.passiveHP = passiveHP;
		
		this.setColor(new Color(252,186,3));
	}
	public int getMoveSpeed() {
		return moveSpeed;
	}
	public int getPassiveHP() {
		return passiveHP;
	}


}
