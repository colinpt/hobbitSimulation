package Terrain;
import java.awt.Color;

import Creatures.*;
import Game.*;
import Items.*;

public class Forest extends Terrain{
	
	private boolean onFire = false;
	private boolean burnt = false;
	private int stepsOnFire = 0;
	private int stepsSpentBurnt = 0;
	public Forest(int xPos, int yPos) {
		super(
				true, 		// Passable for ground units 
				new Color(r.nextInt(10), r.nextInt(15) + 60, r.nextInt(10) + 25),
				xPos,
				yPos
			 );
	}
	public void extinguish(){
		onFire = false;
		burnt = true;
		int seed = r.nextInt(10);
		if (seed < 3)
			this.setColor(new Color(80 + r.nextInt(10), 40 + r.nextInt(30), 40 + r.nextInt(20)));
		else
			this.setColor(new Color(40 + r.nextInt(10), r.nextInt(30), r.nextInt(20)));
	}
	
	private void regrow() {
		burnt = false;
		this.setColor(new Color(r.nextInt(10), r.nextInt(15) + 60, r.nextInt(10) + 25));
	}
	@Override
	public Color getColor(){
		if (onFire) return new Color(220 + r.nextInt(35), r.nextInt(180), r.nextInt(15));
		else return super.getColor();
	}
	
	public void setFire() {
		onFire = true;
	}
	
	public boolean isBurnable() {
		if (onFire == false && burnt == false) return true;
		else return false;
	}
	
	public boolean isOnFire(){
		return onFire;
	}
	
	public boolean isBurnt() {
		return burnt;
	}
	
	@Override
	public void update(Map map) {
		super.update(map);
		
		int seed = r.nextInt(5000);
		if (seed == 0 && map.getItem(x, y) == null && !onFire && !burnt) {
			map.placeItem(new Food(x,y));
		}
		
		// Deal damage to non-Balrog ground creatures standing in fire
		// Also burn up any non-equipable items in fire
		if (onFire) {
			Creature c = map.getCreature(x, y);
			if (c != null && !(c instanceof Balrog) && !(c instanceof Eagle))
				c.takeTrueDamage(6);
			Item item = map.getItem(x, y);
			if (item != null){
				map.pickUpItem(x, y);
			}
			
		}
		if (onFire && ++stepsOnFire < 30) {
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					int curX = x + i, curY = y + j;
					if (GameUtility.isWithinBounds(map, curX) && GameUtility.isWithinBounds(map, curY)) {
						Terrain curTerrain = map.getTerrain(x + i, y + j);
						if (curTerrain instanceof Forest) {
							int fireChance = r.nextInt(25);
							if (fireChance == 0 && ((Forest) curTerrain).isBurnable()) {
								((Forest) curTerrain).setFire();
							}
						}
					}
				}
			}
		}
		else if (stepsOnFire >= 30) {
			this.extinguish();
			stepsOnFire = 0;
		}
		else if (burnt && ++stepsSpentBurnt >= 140) {
			this.regrow();
			stepsSpentBurnt = 0;
		}
	}
}
