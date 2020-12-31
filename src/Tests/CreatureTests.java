package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Creatures.*;
import Game.Map;

class CreatureTests {
	Map map = new Map(5);
	@Test
	void testThatDamageOverTimeIsInflicted() {
		Hobbit h = new Hobbit(0,0);
		int curHealth = 10;
		h.setCurrentHealth(curHealth);
		h.setCurrentDOT(1); // 1 damage every 8 ticks for 64 ticks
		for (int i = 1; i <= 64; i ++) {
		h.checkDOT();
		if (i % 8 == 0) curHealth--;
		assertEquals(curHealth, h.getCurrentHealth());
		}
		// Check that DOT is now over
		assertEquals(0, h.getCurrentDOT());
	}

}
