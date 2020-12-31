package Creatures;
import java.awt.Color;
import java.util.ArrayList;
import Game.*;
import Items.Equipable;
import Items.Food;
import Items.Item;
import Terrain.*;
public class Nazgul extends Creature{
	
	private int turnsSinceLastKill = 5;
	private int attackBuff;
	
	public Nazgul(int xPos, int yPos) {
		super(35 + r.nextInt(10), // Base health
			   2 + r.nextInt(2), // Base armor
			   7 + r.nextInt(3), // Base attack
			   xPos, yPos);      // Start position
		this.moveSpeed = 2;
		this.detectionRadius = 3;
		this.isEvil = true;
		this.bounded = true;
		this.color = new Color(r.nextInt(15), r.nextInt(15), r.nextInt(15));
	}

	@Override
	public void move(Map map, int x, int y) {
		this.setX(x);
		this.setY(y);
	}
	
	@Override
	public int getAttack() {
		return this.attack + attackBuff;
	}
	
	@Override
	public void attack(Creature c) {
		c.takeDamage(this.getAttack());
		if (c.getCurrentHealth() < 0) {
			turnsSinceLastKill = 0;
			this.gainLife(3);
		}
	}

	@Override
	public void stay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replicate(int x, int y, Creature[][] next) {
		next[x][y] = new Nazgul(x, y);
	}

	@Override
	public void chooseAction(Map map, Creature[][] nextGrid) {
		// Initialize needed data structures
		this.localEnemies = new ArrayList<>();
		this.enemiesInRange = new ArrayList<>();
		this.localItems = new ArrayList<>();
		// Nazgul must kill Hobbits to stay alive
		if (++turnsSinceLastKill % 40 == 0) {
			currentHealth--;
		}
		// Check if there is any active damage over time effects
				this.checkDOT();
		// Gain life from any passive healing items
			if (++timeStepsSinceHeal % 30 == 0) {
				this.gainLife(this.getPassiveHealing());
			}
		// Mountains debuffs the movement of Nazgul
		if (map.getTerrain(x, y) instanceof Mountain) this.moveSpeed = 1;
		// Burnt ground buffs the attack of Nazgul
		if (map.getTerrain(x, y) instanceof Forest && ((Forest) map.getTerrain(x, y)).isBurnt()) 
			this.attackBuff = 3;
		
		// Detect creatures within detection radius
		for (int i = -getDetectionRadius(); i <= getDetectionRadius(); i++) {
			for (int j = -getDetectionRadius(); j <= getDetectionRadius(); j++) {
				int detectX = x + i; 
				int detectY = y + j; 
				
				if (GameUtility.isWithinBounds(map, detectX) && GameUtility.isWithinBounds(map, detectY)) {
					Creature c = map.getCreature(detectX, detectY);
				
					if (c != null && !c.isEvil) {
						localEnemies.add(c);
						if (Math.abs(i) <= getAttackRange() && Math.abs(j) <= getAttackRange())
							enemiesInRange.add(c);
					}
					
					Item item = map.getItem(detectX, detectY);
					// Detect nearby items
					if (item != null) {
						localItems.add(item);
					}
				}
				
				
			}
		}
		//*********************************MOVEMENT*****************************************
		// If there are no nearby enemies, but nearby items, move towards them.
		if (localItems.size() > 0 && localEnemies.size() == 0) {
			int [] itemMove = GameUtility.getMinDistanceFromItem(this, map, nextGrid, bounded);
			move(map, itemMove[0], itemMove[1]);
			}
		// If there are any enemies nearby, approach them.
		else if (localEnemies.size() > 0) {
			 int[] aggroMove = GameUtility.getMinDistanceFromEnemy(this, map, nextGrid, bounded);
			 if (map.getCreature(aggroMove[0], aggroMove[1]) == null)
					move(map, aggroMove[0], aggroMove[1]);
		}
		// Otherwise, move in a random direction
		else {
			int[] randMove = GameUtility.findValidRandomMoveBounded(this, moveSpeed, map, nextGrid);
			if (randMove[0] < 0) stay();
			else move(map, randMove[0], randMove[1]);
		}		 
		//*********************************ACTIONS*****************************************
		// Pick up any items in space, Nazgul cannot eat food, but they can destroy it.
			if (map.getItem(x, y) != null) {
				Item item = map.pickUpItem(x, y);
				if (!(item instanceof Food))
					this.inventory.checkPickup((Equipable) item, this, map);
			}
		 
		 if (++this.turnsSinceLastKill < 10 && r.nextInt(20) == 0) {
			// Find a random coordinate within the detection radius to replicate at
			 int[] randSpace = GameUtility.findValidRandomMoveBounded(this, getDetectionRadius(), map, nextGrid);
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
		 // Remove movement debuff if no longer on a mountain
		 if (!(map.getTerrain(x, y) instanceof Mountain)) this.moveSpeed = 2;
		 // Remove attack buff if no longer on burnt grounds
		 if (!(map.getTerrain(x, y) instanceof Forest) 
		 || (map.getTerrain(x, y) instanceof Forest && !((Forest) map.getTerrain(x, y)).isBurnt())) 
				this.attackBuff = 0;
	}
	
	// Unit testing methods
	public void testAttackWithLooting(Creature target, Map map) {
		 attack(target);
		 if (target.getCurrentHealth() <= 0) {
			 ArrayList<Item> loot = target.getInventory().getLoot();
			 for (Item item : loot) {
				 this.getInventory().checkPickup((Equipable) item, this, map);
			 }
		 }
	}

}