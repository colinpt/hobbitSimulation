package Terrain;
import java.awt.Color;

import Game.Map;

public class Water extends Terrain{
	
	public Water(double depth, int xPos, int yPos) {
		super(
				false, 		// Passable for ground units 
				new Color(r.nextInt(15), r.nextInt(15) ,r.nextInt(20) +(int) (190 * (depth * 4))),
				xPos,
				yPos
			 );
	}
	
	
	@Override
	public void update(Map map) {
		
	}
}
