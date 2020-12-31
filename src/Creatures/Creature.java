package Creatures;
import Game.*;
import Items.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;


public abstract class Creature {
	protected static Random r = new Random();
	
	protected boolean isEvil;
	
	protected ArrayList<Creature> localEnemies;
	protected ArrayList<Creature> localAllies;
	protected ArrayList<Item> localItems;
	protected ArrayList<Creature> enemiesInRange;
	
	protected boolean bounded;
	protected boolean flying;
	protected int currentHealth;
	protected int maxHealth; 
	protected int bonusHealth;
	protected int armor;
	protected int attack;
	protected int detectionRadius;
	protected int moveSpeed;
	
	protected int x;
	protected int y;
	protected int timeStepsSinceHeal = 0;
	protected int turnsSinceReplication = 0;
	protected int currentDOT;
	protected int dotCounter;
	protected Color color;
	
	protected Inventory inventory = new Inventory();
	
	public Creature(int health, int armor, int attack, int xPos, int yPos) {
		this.currentHealth = health;
		this.maxHealth	   = health;
		this.armor 	= armor;
		this.attack = attack;
		this.x = xPos;
		this.y = yPos;
		this.timeStepsSinceHeal = 0;
	}
	
	abstract public void move(Map map, int x, int y);
	abstract public void attack(Creature c);
	abstract public void stay();
	abstract public void replicate(int x, int y, Creature[][] next);
	abstract public void chooseAction(Map cur, Creature[][] next);
	
	public void takeDamage(int damage) {
		if (damage - this.getArmor() > 0) currentHealth -= (damage - this.getArmor());
	}
	
	public void takeTrueDamage(int damage) {
		currentHealth -= damage;
	}
	
	public void applyDOT(int dot) {
		this.dotCounter = 0;
		if (dot > this.currentDOT) currentDOT = dot;
	}
	
	public void checkDOT() {
		if (this.currentDOT > 0) {
			if (++dotCounter % 8 == 0) this.takeTrueDamage(currentDOT);
			if (dotCounter >= 64) {
				dotCounter = 0;
				currentDOT = 0;
			}
		}
	}
	
	public double getSize() {
		return (((double) currentHealth / (double) this.getMaxHealth()) * 0.6) + 0.4;
	}

	public void setHealth(int health) {
		if (health > maxHealth){
			this.currentHealth = this.maxHealth;
		}
		this.currentHealth = health;
	}
	public void gainLife(int health) {
		if ((this.currentHealth + health) > maxHealth + inventory.getMaxHPFromEquipment()){
			this.currentHealth = this.maxHealth + inventory.getMaxHPFromEquipment();
		}
		else this.currentHealth += health;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getMaxHealth() {
		return maxHealth + inventory.getMaxHPFromEquipment();
	}

	public int getDetectionRadius() {
		return detectionRadius + inventory.getDetectionFromEquipment();
	}

	public int getMoveSpeed() {
		return moveSpeed + inventory.getMoveSpeedFromEquipment();
	}
	
	public int getAttackRange() {
		return 1 + inventory.getAttackRangeFromEquipment();
	}
	
	public int getPassiveHealing() {
		return inventory.getHealthRegenFromEquipment();
	}
	
	public int getArmor() {
		return armor + inventory.getArmorFromEquipment();
	}


	public int getAttack() {
		return attack + inventory.getDamageFromEquipment();
	}
	
	public int getDot() {
		return inventory.getDamageOverTimeFromEquipment();
	}

	public Color getColor() {
		return this.color;
	}

	public ArrayList<Creature> getLocalEnemies() {
		return localEnemies;
	}

	public ArrayList<Creature> getLocalAllies() {
		return localAllies;
	}

	public ArrayList<Creature> getEnemiesInRange() {
		return enemiesInRange;
	}

	public ArrayList<Item> getLocalItems() {
		return localItems;
	}

	public boolean isBounded() {
		return bounded;
	}

	public boolean isFlying() {
		return flying;
	}

	public int getBonusHealth() {
		return bonusHealth;
	}

	public void setBonusHealth(int bonusHealth) {
		this.bonusHealth = bonusHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public void setArmor(int armor) {
		this.armor = armor;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setTimeStepsSinceHeal(int timeStepsSinceHeal) {
		this.timeStepsSinceHeal = timeStepsSinceHeal;
	}

	public void setMoveSpeed(int moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	public int getTurnsSinceReplication() {
		return turnsSinceReplication;
	}

	public void setTurnsSinceReplication(int turnsSinceReplication) {
		this.turnsSinceReplication = turnsSinceReplication;
	}

	public int getTimeStepsSinceHeal() {
		return timeStepsSinceHeal;
	}

	public void setDetectionRadius(int detectionRadius) {
		this.detectionRadius = detectionRadius;
	}

	public int getCurrentDOT() {
		return currentDOT;
	}

	public void setCurrentDOT(int currentDOT) {
		this.currentDOT = currentDOT;
	}

	public int getDotCounter() {
		return dotCounter;
	}

	public void setDotCounter(int dotCounter) {
		this.dotCounter = dotCounter;
	}
}
