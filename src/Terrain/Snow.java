package Terrain;
import java.awt.Color;

import Game.Map;

public class Snow extends Terrain{
	
	public Snow(double height, int xPos, int yPos){
		super(
				false,
				new Color(0,0,0),
				xPos,
				yPos
				);
				
		
		int snowColor = (int) (210 * (height + 0.20));
		this.setColor(new Color(r.nextInt(15)+ snowColor, r.nextInt(15) + snowColor, r.nextInt(15) + snowColor));
	}
	
	@Override
	public void update(Map map) {
		
	}
}
