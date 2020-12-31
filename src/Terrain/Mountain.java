package Terrain;
import java.awt.Color;

public class Mountain extends Terrain{
	
	public Mountain(double height, int xPos, int yPos) {
		super(
				true, 		// Passable for ground units 
				new Color(0,0,0),
				xPos,
				yPos
			 );
		int rockColor = (int) (40 * (height * 3));
		this.setColor(new Color(r.nextInt(15)+ rockColor, r.nextInt(15) + rockColor, r.nextInt(15) + rockColor));
		if (height > 0.86) { 
			
		}
			
		}

}
