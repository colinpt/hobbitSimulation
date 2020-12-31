package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Game.Map;
import Creatures.*;
class TerrainTests {
	Map map = new Map(5);
	@Test
	void testThatFireDoesDamageToTheRightCreatures() {
		map.initAllFireTerrain(); // Fire does 6 true damage per tick
		Balrog balrog = new Balrog(0,0);
		Eagle eagle = new Eagle(1,0);
		Hobbit hobbit = new Hobbit(2,0);
		Nazgul nazgul = new Nazgul(3,0);
		balrog.setCurrentHealth(10);
		map.placeCreature(balrog);
		eagle.setCurrentHealth(10);
		map.placeCreature(eagle);
		hobbit.setCurrentHealth(10);
		map.placeCreature(hobbit);
		nazgul.setCurrentHealth(10);
		map.placeCreature(nazgul);
		for (int i = 0; i <= 3; i++) {
			map.getTerrain(i, 0).update(map);
		}
		assertEquals(10, balrog.getCurrentHealth());// Does not take fire damage
		assertEquals(10, eagle.getCurrentHealth()); // Does not take fire damage
		assertEquals(4, hobbit.getCurrentHealth());	// Takes fire damage
		assertEquals(4, nazgul.getCurrentHealth()); // Takes fire damage
	}
	
	@Test
	void testThatBurntForestBuffsNazgulDamage() {
		map.initAllBurntTerrain();
		Nazgul nazgul = new Nazgul(0,0);
		nazgul.setAttack(0);
		map.placeCreature(nazgul);
		nazgul.chooseAction(map, new Creature[5][5]);
		assertEquals(3, nazgul.getAttack());
	}

}
