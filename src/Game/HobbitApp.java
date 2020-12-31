package Game;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Terrain.Terrain;

import java.awt.*;
import Creatures.*;
import Items.*;
public class HobbitApp {
static JPanel panel;
static JFrame frame;

private static final int FRAME_DELAY = 100;
private static final int GRID_SIZE = 100;


private static final int width = 1200;
private static final int height = 1200;
public static void main(String[] args) throws InterruptedException{

    panel = new JPanel();
    Dimension dim = new Dimension(width, height);
    panel.setPreferredSize(dim);
    frame = new JFrame();
    frame.setSize(width + 25, height + 50);
    Container contentPane = frame.getContentPane();
    contentPane.add(panel);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    Map map = new Map(GRID_SIZE);
    map.initTerrain();
    map.initCreatures();
    map.initItemPools();
while (true) {
        map.timeStep();
        Graphics g = panel.getGraphics();
        Graphics(map, g);
        Thread.sleep(FRAME_DELAY);
        g.dispose();
    }
}

public static void Graphics(Map map, Graphics g) {
	Graphics2D g2d = (Graphics2D) g;
	
	int tileCount = map.getSize();
    int tileSize = (width / map.getSize());
    if (map.getSize() % 50 != 0) tileSize += 1;
    
    int x = 0, y = 0;
    for (int j = 0; j < tileCount; j++) {
    	for (int i = 0; i < tileCount; i++) {
    		// Draw terrain
    		Terrain currTile = map.getTerrain(i, j);
    		g2d.setColor(currTile.getColor());
    		g2d.fillRect(x, y, tileSize, tileSize);
    		int size;
    		// Draw Items
    		Item item = map.getItem(i, j);
    		if (item != null) {
    			size = (int) (tileSize * item.getSize());
    			g2d.setColor(item.getColor());
    			Color rarityColor = getColorViaRarity(item);
    			g2d.setStroke(new BasicStroke(1));
    			if (item instanceof Food)
    				g2d.fillOval(x + (size / 2), y + (size / 2), size, size);
    			else if (item instanceof Weapon) {
    				g2d.fillPolygon(new int[] {x + 1, x + (tileSize/2), x + tileSize - 1}, new int[] {y + tileSize - 1, y, y + tileSize - 1}, 3);
    				g2d.setColor(rarityColor);
    				g2d.drawPolygon(new int[] {x + 1, x + (tileSize/2), x + tileSize - 1}, new int[] {y + tileSize - 1, y, y + tileSize - 1}, 3);
    			}
    			else if (item instanceof Armor) {
    				g2d.fillPolygon(
    					// This is a hexagon, can't you tell?
    					new int[] {x + 1, x + 1, x + (size/2), x + (3*size/2), x + tileSize - 1, x + tileSize - 1, x + (3*size/2), x + (size/2)},
    					new int[] {y + (3*size/2), y + (size/2), y + 1, y + 1, y + (size/2), y + (3*size/2), y + tileSize - 1, y + tileSize - 1}, 8);
    				g2d.setColor(rarityColor);
    				g2d.drawPolygon(
    						new int[] {x + 1, x + 1, x + (size/2), x + (3*size/2), x + tileSize - 1, x + tileSize - 1, x + (3*size/2), x + (size/2)},
        					new int[] {y + (3*size/2), y + (size/2), y + 1, y + 1, y + (size/2), y + (3*size/2), y + tileSize - 1, y + tileSize - 1}, 8);
    			}
    			else if (item instanceof Accessory) {
    				g2d.fillOval(x + (size / 2), y + (size / 2), (3*size/2), (3*size/2));
    				g2d.setColor(rarityColor);
    				g2d.drawOval(x + (size / 2), y + (size / 2), (3*size/2), (3*size/2));
    			}
    		}
    		// Draw creatures
    		Creature c = map.getCreature(i, j);
    		if (c != null) {
    			size = (int) (tileSize * c.getSize());
    			int offset = (tileSize / 2) - (size / 2);
    			g2d.setColor(c.getColor());
    			g2d.fillRect(x + offset, y + offset, size, size);
    			Inventory items = c.getInventory();
    			if (items.getHelm() != null) {
    				Color rarityColor = getColorViaRarity(items.getHelm());
    				g2d.setColor(rarityColor);
    				g2d.setStroke(new BasicStroke((size/8) + 1)); 
    				g2d.drawLine(x + offset, y + offset, x + offset + size, y + offset);
    			}
    			if (items.getChest() != null) {
    				Color rarityColor = getColorViaRarity(items.getChest());
    				g2d.setColor(rarityColor);
    				g2d.setStroke(new BasicStroke((size/6) + 1)); 
    				g2d.drawLine(x + offset, y + offset + (size / 2), x + offset + size, y + offset + (size / 2));
    			}
    			if (items.getBoots() != null) {
    				Color rarityColor = getColorViaRarity(items.getBoots());
    				g2d.setColor(rarityColor);
    				g2d.setStroke(new BasicStroke((size/8) + 1)); 
    				g2d.drawLine(x + offset, y + offset + size, x + offset + (size/4), y + offset + size);
    				g2d.drawLine(x + offset + (3*size/4), y + offset + size, x + offset + size, y + offset + size);
    			}
    			if (items.getAccessory() != null) {
    				Color rarityColor = getColorViaRarity(items.getAccessory());
    				g2d.setColor(rarityColor);
    				g2d.setStroke(new BasicStroke(1)); 
    				g2d.drawOval(x + offset + size/4, y + offset + size/4, size / 2, size / 2);
    			}
    			if (items.getWeapon() != null) {
    				Color rarityColor = getColorViaRarity(items.getWeapon());
    				g2d.setColor(rarityColor);
    				g2d.setStroke(new BasicStroke((size/6) + 1)); 
    				g2d.drawLine(x + offset + size, y + offset, x + offset + size, y + offset + size);
    			}
    			
    		}
    		// Handy coordinate display for debugging.
//    		g2d.setColor(Color.WHITE);
//			g2d.drawString(String.valueOf(i) + "," + String.valueOf(j), x, y + (tileSize/2));
    		
    		x += tileSize;
    	}
    	x = 0;
    	y += tileSize;
    }
    
	}

private static Color getColorViaRarity(Item item) {
	Color rarityColor = null;
	if (item instanceof Equipable) {
		rarityColor = new Color(255, 255, 255);
		if (((Equipable) item).getRarity() == 1)
			rarityColor = new Color(200, 115, 0);
		else if (((Equipable) item).getRarity() == 2)
			rarityColor = new Color(170, 180, 180);
		else if (((Equipable) item).getRarity() == 3)
			rarityColor = new Color(255, 215, 35);
		else if (((Equipable) item).getName().equals("Flame Whip")
			  || ((Equipable) item).getName().equals("The One Ring"))
			rarityColor = new Color(250, 120, 0);
		else if (((Equipable) item).getName().equals("Eagle Feather"))
			rarityColor = new Color(235, 210, 165);
		
	}
	return rarityColor;
}
}