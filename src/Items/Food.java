package Items;

import java.awt.Color;

import Creatures.Creature;

public class Food extends Item{
	
	public Food(int xPos, int yPos) {
		super(xPos, yPos);
		this.color = new Color(150 + r.nextInt(50), r.nextInt(25), 0);
		this.drawSize = 0.5;
	}
	
	public void consume(Creature c) {
		
	}
	
}





	

