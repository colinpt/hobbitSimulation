package Items;

import java.util.ArrayList;
import java.util.Random;

public class EquipableItemPool {
	Random r = new Random();
	
	ArrayList<Equipable> whiteItems = new ArrayList<>();
	ArrayList<Equipable> bronzeItems = new ArrayList<>();
	ArrayList<Equipable> silverItems = new ArrayList<>();
	ArrayList<Equipable> goldItems = new ArrayList<>();
	ArrayList<Equipable> dropOnlyItems = new ArrayList<>();
	
	public EquipableItemPool(){}
	
	public void addItem(Equipable item) {
		int rarity = item.getRarity();
		if 		(rarity == 0) whiteItems.add(item);
		else if (rarity == 1) bronzeItems.add(item);
		else if (rarity == 2) silverItems.add(item);
		else if (rarity == 3) goldItems.add(item);
		else if (rarity == 4) dropOnlyItems.add(item);
	}
	
	public Equipable getRandomItem() {
		Equipable e = null;
		int seed = r.nextInt(100);
		if (seed >= 40) e = whiteItems.get(r.nextInt(whiteItems.size()));
		else if (seed >= 15) e = bronzeItems.get(r.nextInt(bronzeItems.size()));
		else if (seed >= 5) e = silverItems.get(r.nextInt(silverItems.size()));
		else e = goldItems.get(r.nextInt(goldItems.size()));
		
		Equipable copy = null;
		if (e instanceof Weapon) {
			copy = new Weapon(e.getX(), e.getY(), e.getName(), e.getDamage(), e.getArmor(), 
					e.getMaxHP(), e.getDetection(),((Weapon) e).getRange(), ((Weapon) e).getDot(), e.getRarity()); 
		}
		else if (e instanceof Armor) {
			copy = new Armor(e.getX(), e.getY(), e.getName(), ((Armor) e).getType(), e.getDamage(), e.getArmor(), 
					e.getMaxHP(), e.getDetection(), ((Armor) e).getMoveSpeed(), e.getRarity()); 
		}
		else if (e instanceof Accessory) {
			copy = new Accessory(e.getX(), e.getY(), e.getName(), e.getDamage(), e.getArmor(), 
					e.getMaxHP(), e.getDetection(), ((Accessory) e).getMoveSpeed(), ((Accessory) e).getPassiveHP(), e.getRarity()); 
		}
		return copy;
	}
	
	public Equipable getItemByName(String name) {
		for (int i = 0; i < whiteItems.size(); i++) {
			Equipable curr = whiteItems.get(i);
			if (curr.getName().equals(name)) return curr;
		}
		for (int i = 0; i < bronzeItems.size(); i++) {
			Equipable curr = bronzeItems.get(i);
			if (curr.getName().equals(name)) return curr;
		}
		for (int i = 0; i < silverItems.size(); i++) {
			Equipable curr = silverItems.get(i);
			if (curr.getName().equals(name)) return curr;
		}
		for (int i = 0; i < goldItems.size(); i++) {
			Equipable curr = goldItems.get(i);
			if (curr.getName().equals(name)) return curr;
		}
		for (int i = 0; i < dropOnlyItems.size(); i++) {
			Equipable curr = dropOnlyItems.get(i);
			if (curr.getName().equals(name)) return curr;
		}
		return null;
	}
}
