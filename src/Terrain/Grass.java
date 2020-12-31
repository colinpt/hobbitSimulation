package Terrain;
import java.awt.Color;

import Game.*;
import Items.*;

public class Grass extends Terrain{

	public Grass(double depth, int xPos, int yPos) {
		super(
				true, 		// Passable for ground units 
				new Color(0,0,0),
				xPos,
				yPos
			 );
			// I can't believe I just did this for a grass color...
			// f(x) = 850.66x^2 - 816.63x + 195.99
			// When depth approaches 0.25 grassColor = 45~
			// When depth approaches 0.40 grassColor = 5~
			// When depth approaches 0.70 grassColor = 45~
			int grassColor = (int) ((850.66*(depth*depth)) - (816.63 * (depth)) + 195.99);
			this.setColor(new Color(r.nextInt(10) + grassColor, r.nextInt(20) + 180 - grassColor, r.nextInt(15)));
		
		
	}
	
	@Override
	public void update(Map map) {
		super.update(map);
		
		int seed = r.nextInt(8000);
		if (seed == 0 && map.getItem(x, y) == null) {
			map.placeItem(new Food(x,y));
		}
	}
}
