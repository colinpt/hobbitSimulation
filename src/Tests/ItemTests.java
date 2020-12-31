package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Creatures.*;
import Game.Map;
import Items.*;

class ItemTests {
	
		Map map = new Map(3);
		
		
	@Test
	void testThatEquippedItemDealsAdditionalDamage() {
		map.initItemPools();
		Hobbit h = new Hobbit(0,0);
		Nazgul n = new Nazgul(0,0);
		Equipable item = map.itemPool.getItemByName("Dagger"); // Damage of 2
		// Set attackers damage to 0 to test only weapon damage
		h.setAttack(0);
		// Remove any armor from target
		n.setArmor(0);
		// Control for health
		n.setCurrentHealth(10);
		h.attack(n);
		// Confirm attack did no damage before equipping item
		assertEquals(10, n.getCurrentHealth());
		// Equip item
		h.getInventory().checkPickup(item, h, map);
		h.attack(n);
		// Check damage after
		assertEquals(8, n.getCurrentHealth());
	}
	
	@Test 
	void testThatEquippedArmorPreventsDamage(){
		map.initItemPools();
		Hobbit h = new Hobbit(0,0);
		Nazgul n = new Nazgul(0,0);
		Equipable item = map.itemPool.getItemByName("Leather Cap"); // Armor of 1
		h.setArmor(0);
		n.setAttack(1);
		// Control for health
		h.setCurrentHealth(10);
		n.attack(h);
		// Confirm attack did damage before equipping the armor
		assertEquals(9, h.getCurrentHealth());
		// Equip item
		h.getInventory().checkPickup(item, h, map);
		n.attack(h);
		// Check health after to ensure armor blocked the damage
		assertEquals(9, h.getCurrentHealth());
	}
	
	@Test
	void testThatMaxHealthIsBuffedFromEquipment(){
		map.initItemPools();
		Hobbit h = new Hobbit(0,0);
		Equipable item = map.itemPool.getItemByName("Scale Mail"); // HP buff of 10
		h.setCurrentHealth(10);
		h.setMaxHealth(10);
		h.gainLife(11);
		// Confirm that gaining life did not exceed maximum before equipping item
		assertEquals(10, h.getCurrentHealth());
		// Equip item
		h.getInventory().checkPickup(item, h, map);
		h.gainLife(11);
		// Check health after to ensure maximum was raised
		assertEquals(20, h.getCurrentHealth());
	}
	
	@Test
	void testPassiveHealingFromEquipment() {
		map.initItemPools();
		Hobbit h = new Hobbit(0,0);
		Equipable item = map.itemPool.getItemByName("Vilya"); // Passive healing of 3
		h.setCurrentHealth(1);
		h.setTimeStepsSinceHeal(30);
		h.checkPassiveHealing();
		// Confirm that no life was gained before equipping item
		assertEquals(1, h.getCurrentHealth());
		// Equip item
		h.getInventory().checkPickup(item, h, map);
		h.checkPassiveHealing();
		// Check health after to ensure maximum was raised
		assertEquals(4, h.getCurrentHealth());
	}
	
	@Test
	void testThatKillingAnEnemyGrantsItsLoot() {
		map.initItemPools();
		Hobbit h = new Hobbit(0,0);
		Nazgul n = new Nazgul(0,0);
		Equipable item = map.itemPool.getItemByName("Leather Cap"); // Armor of 1
		h.setArmor(0);
		h.getInventory().checkPickup(item, h, map);
		n.setAttack(10);
		h.setCurrentHealth(1);
		assertEquals(n.getInventory().getHelm(), null);
		n.testAttackWithLooting(h, map);
		assertEquals(n.getInventory().getHelm(), item);
	}
}
