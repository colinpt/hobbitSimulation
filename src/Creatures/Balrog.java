package Creatures;
import Game.*;
import Items.Weapon;
import Terrain.*;

import java.awt.Color;
import java.util.ArrayList;

public class Balrog extends Creature{

	public Balrog(int xPos, int yPos) {
		super(125 + r.nextInt(25), // Base health
		       3 + r.nextInt(3),  // Base armor
		      17 + r.nextInt(8),  // Base attack
			   xPos, yPos);       // Start position
		this.isEvil = true;
		
		this.color = new Color(230, 20, 20);
		this.moveSpeed = 1;
		this.detectionRadius = 3;
	}

	@Override
	public void move(Map map, int x, int y) {
		this.setX(x);
		this.setY(y);
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
		// Ensure Balrog is equipped with a flame whip
		if (this.inventory.getWeapon() == null) {
			this.inventory.setWeapon((Weapon) map.itemPool.getItemByName("Flame Whip"));
		}
		// Initialize needed data structures
		this.localEnemies = new ArrayList<>();
		this.enemiesInRange = new ArrayList<>();
		
		// Detect creatures within detection radius
			for (int i = -this.getDetectionRadius(); i <= this.getDetectionRadius(); i++) {
				for (int j = -this.getDetectionRadius(); j <= this.getDetectionRadius(); j++) {
					int detectX = x + i; 
					int detectY = y + j; 
					
					if (GameUtility.isWithinBounds(map, detectX) && GameUtility.isWithinBounds(map, detectY)) {
						Creature c = map.getCreature(detectX, detectY);
					
						if (c != null && !c.isEvil) {
							localEnemies.add(c);
							if (Math.abs(i) <= getAttackRange() && Math.abs(j) <= getAttackRange())
								enemiesInRange.add(c);
						}
					}
				}
			}
		//*********************************MOVEMENT*****************************************
		// Balrogs only move on occasion
		int moveSeed = r.nextInt(5);
		if (moveSeed == 0) {
			// If there are any enemies nearby, approach them.
			 if (localEnemies.size() > 0) {
				 int[] aggroMove = GameUtility.getMinDistanceFromEnemy(this, map, nextGrid, bounded);
				 if (map.getCreature(aggroMove[0], aggroMove[1]) == null)
						move(map, aggroMove[0], aggroMove[1]);
			}
			// Otherwise, move in a random direction
			else {
				int[] randMove = GameUtility.findValidRandomMoveBounded(this, getMoveSpeed(), map, nextGrid);
				if (randMove[0] < 0) stay();
				else move(map, randMove[0], randMove[1]);
			}
		}
		// If there are enemies in range to attack, do so.
				 else if (enemiesInRange.size() > 0) {
					 attack(GameUtility.chooseLowestHealthTarget(enemiesInRange));
				 }
		
		// Give Balrogs a chance to ignite a Forest tile if they are standing on it.
		if (map.getTerrain(x, y) instanceof Forest) {
			int seed = r.nextInt(100);
			Forest curForest = (Forest) map.getTerrain(x, y);
			if (seed == 0 && curForest.isBurnable()) {
				curForest.setFire();
			}
		}
		
	}

}
