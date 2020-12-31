package Creatures;
import Game.*;
import Items.*;

import java.awt.Color;
import java.util.ArrayList;

public class Hobbit extends Creature{
	
	private int turnsSinceFood = 0;
	
	public Hobbit(int xPos, int yPos) {
		super(25 + r.nextInt(15), // Base health
			   2 + r.nextInt(1), // Base armor
			   6 + r.nextInt(2), // Base attack
			   xPos, yPos);      // Start position
		this.moveSpeed = 1;
		this.detectionRadius = 3;
		this.isEvil = false;
		this.bounded = false;
		this.color = new Color(130 + r.nextInt(15),80 + r.nextInt(15),40);
	}

	@Override
	public void move(Map map, int x, int y) {
		this.setX(GameUtility.fixPos(map, x));
		this.setY(GameUtility.fixPos(map, y));
	}

	@Override
	public void attack(Creature c) {
		c.takeDamage(this.getAttack());
		c.applyDOT(this.getDot());
	}

	@Override
	public void stay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replicate(int x, int y, Creature[][] next) {
		next[x][y] = new Hobbit(x,y);
	}
	
	@Override
	public void chooseAction(Map map, Creature[][] nextGrid) {

		this.localEnemies 	= new ArrayList<>();
		this.enemiesInRange = new ArrayList<>();
		this.localAllies  	= new ArrayList<>();
		this.localItems 	= new ArrayList<>();
				
		int adjacentAllies = 0;
		// Detect creatures within detection radius
		for (int i = -this.getDetectionRadius(); i <= this.getDetectionRadius(); i++) {
			for (int j = -this.getDetectionRadius(); j <= this.getDetectionRadius(); j++) {
				int detectX = GameUtility.fixPos(map, this.getX() + i); 
				int detectY = GameUtility.fixPos(map, this.getY() + j); 

				Creature c = map.getCreature(detectX, detectY);
				// Detect nearbly enemies
				if (c != null && c.isEvil) {
					localEnemies.add(c);
					// Check if they are in attack range
					if (Math.abs(i) <= this.getAttackRange() && Math.abs(j) <= this.getAttackRange())
						enemiesInRange.add(c);
				}
				// Detect nearby allies
				else if (c != null && !c.isEvil && !map.getCreature(detectX, detectY).equals(this)) {
					localAllies.add(c);
					// Count how many are directly adjacent
					if (Math.abs(i) <= 2 && Math.abs(j) <= 2)
						adjacentAllies++;
				}
				
				Item item = map.getItem(detectX, detectY);
				// Detect nearby items
				if (item != null) {
					localItems.add(item);
				}
			}
		}
		
		//*********************************MOVEMENT*****************************************
		// If there are no nearby enemies, but nearby items, move towards them.
		if (localItems.size() > 0) {
				int [] itemMove = GameUtility.getMinDistanceFromItem(this, map, nextGrid, bounded);
				move(map, itemMove[0], itemMove[1]);
		}
		// If there are enemies detected, and not enough nearby allies, find the maximum movable distance from them
		else if (localEnemies.size() > 0 && localAllies.size() < 2) {
			int [] safestMove = GameUtility.getMaxDistanceFromEnemy(this, map, nextGrid, bounded);
				move(map, safestMove[0], safestMove[1]);
		}
		
		// If the are enemies detected, and sufficient nearby allies, approach the enemy
		
		else if (localEnemies.size() > 0 && localAllies.size() >= 2) {
			int [] aggroMove = GameUtility.getMinDistanceFromEnemy(this, map, nextGrid, bounded);
				move(map, aggroMove[0], aggroMove[1]);
		}
		
		
		// If there no enemies detected, no nearby items, and there are nearby allies move towards them.
		else if (localAllies.size() > 0 && adjacentAllies == 0 && localItems.size() == 0) {
			int[] friendlyMove = GameUtility.getMinDistanceFromAlly(this, map, nextGrid, bounded);
				move(map, friendlyMove[0], friendlyMove[1]);
		}
		// Otherwise, move in a random direction
		else {
			int[] randMove = GameUtility.findValidRandomMove(this, this.getMoveSpeed(), map, nextGrid);
			if (randMove[0] < 0) stay();
			else move(map, randMove[0], randMove[1]);
		}
		//*********************************ACTIONS*****************************************
		// Pick up any items in space
		if (map.getItem(x, y) != null) {
			Item item = map.pickUpItem(x, y);
			if (!(item instanceof Food))
				this.inventory.checkPickup((Equipable) item, this, map);
			else {
				turnsSinceFood = 0;
				this.gainLife(4);
			}
		}
		// Check if there is any active damage over time effects
		this.checkDOT();
		// Gain life from any passive healing items
		if (++timeStepsSinceHeal % 30 == 0) {
			this.gainLife(this.getPassiveHealing());
		}
		// Lose health if food has not be eaten
		if (++turnsSinceFood % 20 == 0) {
			currentHealth -= 2;
		}
		// Attempt to replicate
		int seed = r.nextInt(50);
		if (++this.turnsSinceReplication > 60 && localEnemies.size() == 0 && seed == 0) {
			this.turnsSinceReplication = 0;
			int[] randSpace = GameUtility.findValidRandomMove(this, detectionRadius, map, nextGrid);
			if (randSpace[0] != -1)
				replicate(randSpace[0], randSpace[1], nextGrid);
		}
		
		// If there are enemies in range to attack, do so.
		// If the attack kills the enemy, loot it.
		else if (enemiesInRange.size() > 0) {
			 Creature target = GameUtility.chooseLowestHealthTarget(enemiesInRange);
			 attack(target);
			 if (target.getCurrentHealth() <= 0) {
				 ArrayList<Item> loot = target.getInventory().getLoot();
				 for (Item item : loot) {
					 this.getInventory().checkPickup((Equipable) item, this, map);
				 }
			 }
		 }
		
		
	}
	// Methods used for unit testing
	public void addLocalItem(Item item) {
		if (this.localItems == null)
			this.localItems = new ArrayList<>();
		localItems.add(item);
	}
	
	public void addLocalAlly(Creature c) {
		if (this.localAllies == null)
			this.localAllies = new ArrayList<>();
		localAllies.add(c);
	}
	
	public void addLocalEnemy(Creature c) {
		if (this.localEnemies == null)
			this.localEnemies = new ArrayList<>();
		localEnemies.add(c);
	}
	
	public void checkPassiveHealing() {
		if (timeStepsSinceHeal % 30 == 0) {
			this.gainLife(this.getPassiveHealing());
		}
	}
}
