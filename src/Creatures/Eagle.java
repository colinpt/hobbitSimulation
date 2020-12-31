package Creatures;
import Game.*;
import Items.Accessory;
import Items.Item;

import java.awt.Color;
import java.util.ArrayList;

public class Eagle extends Creature{
	
	public Eagle(int xPos, int yPos) {
		super(50 + r.nextInt(25), // Base health
			   3 + r.nextInt(5),  // Base armor
			   10 + r.nextInt(4), // Base attack
		       xPos, yPos);       // Start position
		this.moveSpeed = 2;
		this.detectionRadius = 4;
		this.isEvil = false;
		this.flying = true;
		
		this.color = new Color(170 + r.nextInt(25), 170 + r.nextInt(25), 100 + r.nextInt(25));
	}

	@Override
	public void move(Map map, int x, int y) {
		this.setX(GameUtility.fixPos(map, x));
		this.setY(GameUtility.fixPos(map, y));
	}

	@Override
	public void attack(Creature c) {
		c.takeDamage(this.getAttack());
		
	}

	@Override
	public void stay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replicate(int x, int y, Creature[][] next) {
	}

	@Override
	public void chooseAction(Map map, Creature[][] nextGrid) {
		// Ensure that the Eagle always has the "Eagle Feather" accessory
		if (this.inventory.getAccessory() == null) {
			this.inventory.setAccessory( (Accessory) map.itemPool.getItemByName("Eagle Feather"));
		}
		
		this.localEnemies 	= new ArrayList<>();
		this.enemiesInRange = new ArrayList<>();
		this.localAllies  	= new ArrayList<>();
		this.localItems 	= new ArrayList<>();
				
		int adjacentAllies = 0;
		// Detect creatures within detection radius
		for (int i = -this.getDetectionRadius(); i <= this.getDetectionRadius(); i++) {
			for (int j = -this.getDetectionRadius(); j <= this.getDetectionRadius(); j++) {
				int detectX = GameUtility.fixPos(map, x + i); 
				int detectY = GameUtility.fixPos(map, y + j); 

				Creature c = map.getCreature(detectX, detectY);
				// Detect nearby enemies
				if (c != null && c.isEvil) {
					localEnemies.add(c);
					// If they are in an adjacent square, they are attackable
					if (Math.abs(i) <= 1 && Math.abs(j) <= 1)
						enemiesInRange.add(c);
				}
				// Detect nearby allies
				else if (c != null && !c.isEvil && !map.getCreature(detectX, detectY).equals(this)) {
					localAllies.add(c);
					// Count how many are directly adjacent
					if (Math.abs(i) <= 1 && Math.abs(j) <= 1)
						adjacentAllies++;
				}
				Item item  = map.getItem(detectX, detectY);
				// Detect nearby items
				if (item != null) {
					localItems.add(item);
				}
			}
		}
		
		//*********************************MOVEMENT*****************************************
		
		boolean moved = false;
			
		// If the are enemies detected, and sufficient nearby allies, approach the enemy		
		if (localEnemies.size() > 0 && localAllies.size() >= 1) {
			int [] aggroMove = GameUtility.getMinDistanceFromEnemy(this, map, nextGrid, bounded);
			if (map.getCreature(aggroMove[0], aggroMove[1]) == null) {
				move(map, aggroMove[0], aggroMove[1]);
				moved = true;
			}
		}
		
		// If there no enemies detected, no nearby items, and there are nearby allies move towards them.
		else if (localAllies.size() > 0 && adjacentAllies == 0) {
			int[] friendlyMove = GameUtility.getMinDistanceFromAlly(this, map, nextGrid, bounded);
			if (map.getCreature(friendlyMove[0], friendlyMove[1]) == null) {
				move(map, friendlyMove[0], friendlyMove[1]);
				moved = true;
			}
		}
		// Otherwise, move in a random direction
		if (!moved) {
			int[] randMove = GameUtility.findValidRandomMove(this, getMoveSpeed(), map, nextGrid);
			if (randMove[0] < 0) stay();
			else move(map, randMove[0], randMove[1]);
		}
		//*********************************ACTIONS*****************************************
		if (enemiesInRange.size() > 0) {
			 attack(GameUtility.chooseLowestHealthTarget(enemiesInRange));
		 }
	}
}
