package Items;

import java.util.ArrayList;

import Creatures.Creature;
import Game.Map;

public class Inventory {
	
	protected ArrayList<Item> items = new ArrayList<>();
	protected Armor helm  = null;
	protected Armor chest = null;
	protected Armor boots = null;
	protected Weapon weapon = null;
	protected Accessory accessory = null;
	
	
	public void add(Item item) {
		items.add(item);
	}
	
	public void checkPickup(Equipable item, Creature c, Map map) {
		// Check item type
		if (item instanceof Armor) {
			// Check armor type
			if (((Armor) item).getType().strip().equals("Helmet")) {
				// Check if armor of that type is already equipped
				if (helm == null) helm = (Armor) item;
				// If there is, check if the new armor is better
				else if (((Armor) item).getRarity() > helm.getRarity()) {
					// If the new armor is better, equip it, and store the old armor.
					add(helm);
					helm = (Armor) item;
					}
				// If the new armor is not better, store it.
				add(item);
			}
			// Repeat for other armor types.
			else if (((Armor) item).getType().strip().equals("Chest")) {
				if (chest == null) chest = (Armor) item;
				else if (((Armor) item).getRarity() > chest.getRarity()) {
					add(chest);
					chest = (Armor) item;
					}
				else add(item);
			}
			else if (((Armor) item).getType().strip().equals("Boots")) {
				if (boots == null) boots = (Armor) item;
				else if (((Armor) item).getRarity() > boots.getRarity()) {
					add(boots);
					boots = (Armor) item;
					}
				else add(item);
			}
			// 
		}
		else if (item instanceof Weapon) {
			if (weapon == null) weapon = (Weapon) item;
			else if (((Weapon) item).getRarity() > weapon.getRarity()) {
				add(boots);
				weapon = (Weapon) item;
				}
			else add(item);
		}
		else if (item instanceof Accessory) {
			if (accessory == null) accessory = (Accessory) item;
			else if (((Accessory) item).getRarity() > accessory.getRarity()) {
				add(boots);
				accessory = (Accessory) item;
				}
			else add(item);
		}
		
	}
	
	public ArrayList<Item> getLoot(){
		if (helm != null) items.add(helm);
		if (chest != null) items.add(chest);
		if (boots != null) items.add(boots);
		if (weapon != null) items.add(weapon);
		if (accessory != null) items.add(accessory);
		return items;
	}
	
	public int getDamageFromEquipment() {
		int bonusDamage = 0;
		if (helm != null) bonusDamage += helm.getDamage();
		if (chest != null) bonusDamage += chest.getDamage();
		if (boots != null) bonusDamage += boots.getDamage();
		if (weapon != null) bonusDamage += weapon.getDamage();
		if (accessory != null) bonusDamage += accessory.getDamage();
		return bonusDamage;
	}

	public int getArmorFromEquipment() {
		int bonusArmor = 0;
		if (helm != null) bonusArmor += helm.getArmor();
		if (chest != null) bonusArmor += chest.getArmor();
		if (boots != null) bonusArmor += boots.getArmor();
		if (weapon != null) bonusArmor += weapon.getArmor();
		if (accessory != null) bonusArmor += accessory.getArmor();
		return bonusArmor;
	}

	public int getMaxHPFromEquipment() {
		int bonusHP = 0;
		if (helm != null) bonusHP += helm.getMaxHP();
		if (chest != null) bonusHP += chest.getMaxHP();
		if (boots != null) bonusHP += boots.getMaxHP();
		if (weapon != null) bonusHP += weapon.getMaxHP();
		if (accessory != null) bonusHP += accessory.getMaxHP();
		return bonusHP;
	}

	public int getDetectionFromEquipment() {
		int bonusDetection = 0;
		if (helm != null) bonusDetection += helm.getDetection();
		if (weapon != null) bonusDetection += weapon.getDetection();
		if (accessory != null) bonusDetection += accessory.getDetection();
		return bonusDetection;
	}
	
	public int getMoveSpeedFromEquipment() {
		int bonusMoveSpeed = 0;
		if (boots != null) bonusMoveSpeed += boots.getMoveSpeed();
		if (accessory != null) bonusMoveSpeed += accessory.getMoveSpeed();
		return bonusMoveSpeed;
	}
	
	public int getHealthRegenFromEquipment() {
		int healthRegen = 0;
		if (accessory != null) healthRegen += accessory.getPassiveHP();
		return healthRegen;
	}
	
	public int getAttackRangeFromEquipment() {
		int bonusRange = 0;
		if (weapon != null) bonusRange += weapon.getRange();
		return bonusRange;
	}
	
	public int getDamageOverTimeFromEquipment() {
		int dot = 0;
		if (weapon != null) dot += weapon.getDot();
		return dot;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public Armor getHelm() {
		return helm;
	}

	public Armor getChest() {
		return chest;
	}

	public Armor getBoots() {
		return boots;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public Accessory getAccessory() {
		return accessory;
	}

	public void setHelm(Armor helm) {
		this.helm = helm;
	}

	public void setChest(Armor chest) {
		this.chest = chest;
	}

	public void setBoots(Armor boots) {
		this.boots = boots;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	public void setAccessory(Accessory accessory) {
		this.accessory = accessory;
	}

}
