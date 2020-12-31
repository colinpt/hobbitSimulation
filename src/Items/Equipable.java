package Items;

public class Equipable extends Item implements Comparable<Equipable>{
	
	protected String name;
	protected int damage;
	protected int armor;
	protected int maxHP;
	protected int detection;
	protected int rarity;
	
	public Equipable(int xPos, int yPos, String name, int damage, 
			int armor, int maxHP, int detection, int rarity) {
		super(xPos, yPos);
		this.name 		= name;
		this.damage 	= damage;
		this.armor 		= armor;
		this.maxHP 		= maxHP;
		this.detection 	= detection;
		this.rarity		= rarity;
		
		this.drawSize = 0.5;
	}
		
	public int compareTo(Equipable that) {
		if (this.getRarity() > that.getRarity()) return 1;
		else if (this.getRarity() < that.getRarity()) return -1;
		else return 0;
	}

	public String getName() {
		return name;
	}

	public int getDamage() {
		return damage;
	}

	public int getArmor() {
		return armor;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public int getDetection() {
		return detection;
	}

	public int getRarity() {
		return rarity;
	}

}
