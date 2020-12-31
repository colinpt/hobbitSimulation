package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import Creatures.*;
import Game.GameUtility;
import Game.Map;
import Items.*;

class GameUtilityTests {
	Map map = new Map(10);
	@Test
	void testDistance() {
		int x1 = 1, y1 = 1;
		int x2 = 1, y2 = 1;
		
		double dist = GameUtility.distance(x1, y1, x2, y2);
		assertEquals(dist, 0);
		
		x2 = 2;
		dist = GameUtility.distance(x1, y1, x2, y2);
		assertEquals(dist, 1);
	}
	
	@Test
	void testFixPos() {
		int x = -1;
		x = GameUtility.fixPos(map, x);
		assertEquals(x, 9);
		
		x = 10;
		x = GameUtility.fixPos(map, x);
		assertEquals(x, 0);
	}
	
	@Test
	void testIsWithinBounds() {
		int x = 10;
		assertFalse(GameUtility.isWithinBounds(map, x));
		x = -1;
		assertFalse(GameUtility.isWithinBounds(map, x));
		x = 0;
		assertTrue(GameUtility.isWithinBounds(map, x));
		x = 9;
		assertTrue(GameUtility.isWithinBounds(map, x));
	}
	
	@Test 
	void testChooseLowestHealthTarget(){
		ArrayList<Creature> targets = new ArrayList<>();
		for (int i = 1; i < 5; i++) {
			Hobbit h = new Hobbit(0, i);
			h.setCurrentHealth(i);
			targets.add(h);
			}
		Creature target = GameUtility.chooseLowestHealthTarget(targets);
		assertEquals(target.getCurrentHealth(), 1);
	}
	
	/*
	# = possible move
	x = correct move
		H-#-0-0-#
	    #-X-0-0-#
	    0-0-I-0-0
	    0-0-0-0-0
	    #-#-0-0-#
	 */
	@Test
	void testMinDistanceFromOneItem() {
		map = new Map(5);
		map.initAllGrassTerrain();
		Creature[][] cGrid = new Creature[5][5];
		Item item = new Item(2, 2); // Middle of 5x5 grid
		Hobbit h = new Hobbit(0,0);
		map.placeItem(item);
		h.addLocalItem(item);
		int[] results = GameUtility.getMinDistanceFromItem(h, map, cGrid, false);
		assertEquals(results[0], 1);
		assertEquals(results[1], 1);
	}
	/*
	# = possible move
	x = correct move
		1-#-0-0-#
	    #-X-0-0-#
	    0-0-2-0-0
	    0-0-0-0-0
	    #-#-0-0-#
	 */
	@Test
	void testMinDistanceFromOneAlly() {
		map = new Map(5);
		map.initAllGrassTerrain();
		Creature[][] cGrid = new Creature[5][5];
		Hobbit h1 = new Hobbit(0, 0); // Middle of 5x5 grid
		Hobbit h2 = new Hobbit(2, 2);
		map.placeCreature(h1);
		map.placeCreature(h2);
		h1.addLocalAlly(h2);
		int[] results = GameUtility.getMinDistanceFromAlly(h1, map, cGrid, false);
		assertEquals(results[0], 1);
		assertEquals(results[1], 1);
	}
	/*
	# = possible move
	x = correct move
		H-#-0-0-#
	    #-X-0-0-#
	    0-0-N-0-0
	    0-0-0-0-0
	    #-#-0-0-#
	 */
	@Test
	void testMinDistanceFromEnemy() {
		map = new Map(5);
		map.initAllGrassTerrain();
		Creature[][] cGrid = new Creature[5][5];
		Hobbit h = new Hobbit(0, 0); 
		Nazgul n = new Nazgul(2, 2); // Middle of 5x5 grid
		map.placeCreature(h);
		map.placeCreature(n);
		h.addLocalEnemy(n);
		int[] results = GameUtility.getMinDistanceFromEnemy(h, map, cGrid, false);
		assertEquals(results[0], 1);
		assertEquals(results[1], 1);
	}
	
	/*
	# = possible move
	x = correct move
		 X-#-#-0-0
	     #-H-#-0-0
	     #-#-N-0-0
	     0-0-0-0-0
	     0-0-0-0-0
	 */
	@Test
	void testMaxDistanceFromOneEnemy() {
		map = new Map(5);
		map.initAllGrassTerrain();
		Creature[][] cGrid = new Creature[5][5];
		Hobbit h = new Hobbit(1, 1); 
		Nazgul n = new Nazgul(2, 2); // Middle of 5x5 grid
		map.placeCreature(h);
		map.placeCreature(n);
		h.addLocalEnemy(n);
		int[] results = GameUtility.getMaxDistanceFromEnemy(h, map, cGrid, false);
		assertEquals(results[0], 0);
		assertEquals(results[1], 0);
	}
	
	/*
	# = possible move
	x = correct move
		 0-X-#-#-0
	     0-#-H-N-0
	     0-N-#-#-0
	     0-0-0-0-0
	     0-0-0-0-0
	 */
	@Test
	void testMaxDistanceFromMultipleEnemies() {
		map = new Map(5);
		map.initAllGrassTerrain();
		Creature[][] cGrid = new Creature[5][5];
		Hobbit h = new Hobbit(2, 1); 
		Nazgul n1 = new Nazgul(1, 2);
		Nazgul n2 = new Nazgul(3, 1);
		map.placeCreature(h);
		map.placeCreature(n1);
		map.placeCreature(n2);
		h.addLocalEnemy(n1);
		h.addLocalEnemy(n2);
		int[] results = GameUtility.getMaxDistanceFromEnemy(h, map, cGrid, false);
		assertEquals(results[0], 1);
		assertEquals(results[1], 0);
	}
	/*
	# = possible move
	x = correct move
		 #-1-#-0-0
	     #-#-X-3-0
	     0-2-0-0-0
	     0-0-0-0-0
	     #-#-#-0-0
	 */
	@Test
	void testMinDistanceFromMultipleAllies() {
		map = new Map(5);
		map.initAllGrassTerrain();
		Creature[][] cGrid = new Creature[5][5];
		Hobbit h1 = new Hobbit(1, 0); // Middle of 5x5 grid
		Hobbit h2 = new Hobbit(1, 2);
		Hobbit h3 = new Hobbit(3, 1);
		map.placeCreature(h1);
		map.placeCreature(h2);
		map.placeCreature(h3);
		h1.addLocalAlly(h2);
		h1.addLocalAlly(h3);
		int[] results = GameUtility.getMinDistanceFromAlly(h1, map, cGrid, false);
		assertEquals(results[0], 2);
		assertEquals(results[1], 1);
	}
	
	/*
	# = possible move
	x = correct move
		I-0-0-0-0
	    0-X-#-#-0
	    0-#-H-#-0 
	    0-#-#-#-0
	    0-0-0-0-I
	    
	 When there are multiple items, it will always just approach the first one scanned.
	 */
	@Test
	void testMinDistanceFromMultipleItems() {
		map = new Map(5);
		map.initAllGrassTerrain();
		Creature[][] cGrid = new Creature[5][5];
		Item item1 = new Item(0, 0);
		Item item2 = new Item(4, 4);
		Hobbit h = new Hobbit(2,2);
		map.placeItem(item1);
		h.addLocalItem(item1);
		map.placeItem(item2);
		h.addLocalItem(item2);
		int[] results = GameUtility.getMinDistanceFromItem(h, map, cGrid, false);
		assertEquals(results[0], 1);
		assertEquals(results[1], 1);
	}
	/*
	# = possible move
	x = correct move
		I-0-0-0-0 		I-0-0-0-0
	    0-#-#-#-0		0-#-#-#-0
	    0-#-H-#-0  -->  0-#-H-#-0 
	    0-#-#-I-0  		0-#-#-X-0
	    0-0-0-0-0 		0-0-0-0-0
	    
	 When there are multiple items, it will always just approach the first one scanned.
	 unless, there is one that is within range this turn.
	 */
	@Test
	void testThatCreatureWillBeDirectedToItemIfWithinRange() {
		map = new Map(5);
		map.initAllGrassTerrain();
		Creature[][] cGrid = new Creature[5][5];
		Item item1 = new Item(0, 0);
		Item item2 = new Item(3, 3);
		Hobbit h = new Hobbit(2,2);
		map.placeItem(item1);
		h.addLocalItem(item1);
		map.placeItem(item2);
		h.addLocalItem(item2);
		int[] results = GameUtility.getMinDistanceFromItem(h, map, cGrid, false);
		assertEquals(results[0], 3);
		assertEquals(results[1], 3);
	}
	/*
	# = possible move
	x = correct move
		0-0-0-0-0-0-0-0
	    X-#-#-0-0-0-0-0
	    #-H-#-0-0-0-0-I 
	    #-#-#-0-0-0-0-0
	    0-0-0-0-0-0-0-0
	    0-0-0-0-0-0-0-0
	    0-0-0-0-0-0-0-0
	    0-0-0-0-0-0-0-0
	 */
	@Test
	void testThatCreatureWillCrossBoundryToGatherItem() {
		map = new Map(8);
		map.initAllGrassTerrain();
		Creature[][] cGrid = new Creature[5][5];
		Item item = new Item(7, 2);
		Hobbit h = new Hobbit(1,2);
		map.placeItem(item);
		h.addLocalItem(item);
		int[] results = GameUtility.getMinDistanceFromItem(h, map, cGrid, false);
		assertEquals(results[0], 0);
		assertEquals(results[1], 1);
	}
	/*
	# = possible move
	x = correct move
		0-0-0-0-0 		0-0-0-0-0
	    I-I-#-I-0		I-X-#-I-0
	    0-#-H-#-0  -->  0-#-H-#-0 
	    0-#-#-#-I  		0-#-#-#-I
	    0-0-0-0-0 		0-0-0-0-0
	    
	 When there are multiple items, it will always just approach the first one scanned.
	 unless, there is one that is within range this turn.
	 */
	@Test
	void testThatCreatureWillChooseAnItemWhenThereAreMultipleInRange() {
		map = new Map(5);
		map.initAllGrassTerrain();
		Creature[][] cGrid = new Creature[5][5];
		Item item1 = new Item(0, 1);
		Item item2 = new Item(1, 1);
		Item item3 = new Item(3, 1);
		Item item4 = new Item(4, 3);
		Hobbit h = new Hobbit(2,2);
		map.placeItem(item1);
		h.addLocalItem(item1);
		map.placeItem(item2);
		h.addLocalItem(item2);
		map.placeItem(item3);
		h.addLocalItem(item3);
		map.placeItem(item4);
		h.addLocalItem(item4);
		int[] results = GameUtility.getMinDistanceFromItem(h, map, cGrid, false);
		assertEquals(results[0], 1);
		assertEquals(results[1], 1);
	}
	
}
