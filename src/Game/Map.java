package Game;
import java.io.File;
import java.util.Random;
import java.util.Scanner;

import Terrain.*;
import Creatures.*;
import Items.*;

public class Map {
	private Random r = new Random();
	
	private int gridSize;
	
	private Creature[][] creatureGrid;
	private Terrain[][] terrainGrid;
	private Item[][] itemGrid;
	
	public EquipableItemPool itemPool = new EquipableItemPool();
	
	public Map(int gridSize) {
		this.gridSize = gridSize;
		itemGrid = new Item[gridSize][gridSize];
		creatureGrid = new Creature[gridSize][gridSize];
		terrainGrid = new Terrain[gridSize][gridSize];
	}
	
	public int getSize() {
		return gridSize;
	}
	
	public Creature getCreature(int x, int y) {
		return creatureGrid[x][y];
	}
	
	public Terrain getTerrain(int x, int y) {
		return terrainGrid[x][y];
	}
	
	public Item getItem(int x, int y) {
		return itemGrid[x][y];
	}
	
	public Item pickUpItem(int x, int y) {
		Item item = itemGrid[x][y];
		itemGrid[x][y] = null;
		return item;
	}
	
	public void placeItem(Item item) {
		itemGrid[item.getX()][item.getY()] = item;
	}
	
	public void placeCreature(Creature c) {
		creatureGrid[c.getX()][c.getY()] = c;
	}
	public void timeStep() {
		Creature[][] nextCreatureGrid = new Creature[gridSize][gridSize];
		
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				
				if (creatureGrid[x][y] != null) {
					Creature c = creatureGrid[x][y];
					c.chooseAction(this, nextCreatureGrid);
					if (c.getCurrentHealth() > 0) nextCreatureGrid[c.getX()][c.getY()] = c; 
				}
				terrainGrid[x][y].update(this);
			}
		}
		creatureGrid = nextCreatureGrid;
	}
	

	public void initCreatures() {
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				int seed = r.nextInt(5000);//10000
				// Eagles can spawn anywhere
				if (seed < 10) creatureGrid[x][y] = new Eagle(x,y);
				// Others must been on passable terrain
				if (getTerrain(x,y).isPassable()) {
				if (seed >= 10 && seed < 15) creatureGrid[x][y] = new Balrog(x,y);
				else if (seed < 45) creatureGrid[x][y] = new Nazgul(x,y);
				else if (seed < 100) creatureGrid[x][y] = new Hobbit(x,y);
				}
				// TODO add obstacles & items
				
			}
		}
	}
	
	public void initTerrain() {
		double[][] noiseGrid = GameUtility.generateSimplexNoise(gridSize, gridSize);
		
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {

				double elevation = noiseGrid[x][y];
				if (elevation > 0.60){
					terrainGrid[x][y] = new Forest(x, y);
				}
			}
		}
			
		noiseGrid = GameUtility.generateSimplexNoise(gridSize, gridSize);
			
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				double elevation = noiseGrid[x][y];
				
				if (elevation > 0.82) {
					terrainGrid[x][y] = new Snow(elevation, x, y);
				}
				else if (elevation > 0.70){
					terrainGrid[x][y] = new Mountain(elevation, x, y);
				}
				else if (elevation < 0.25) {
					terrainGrid[x][y] = new Water(elevation, x, y);
				}
				else if (!(terrainGrid[x][y] instanceof Forest)){
					terrainGrid[x][y] = new Grass(elevation, x, y);
				}
			}

		}
	}
	
	public void initItemPools() {
		try {
			File file = new File("assets/weapons.csv");
			Scanner sc = new Scanner(file);
			// Skip title row
			sc.nextLine();
			while (sc.hasNextLine()) {
				String[] curWeapon = sc.nextLine().split(",");
				String name   = curWeapon[0];
				int damage    = Integer.parseInt(curWeapon[1]);
				int armor     = Integer.parseInt(curWeapon[2]);
				int maxHP     = Integer.parseInt(curWeapon[3]);
				int detection = Integer.parseInt(curWeapon[4]);
				int range     = Integer.parseInt(curWeapon[5]);
				int dot       = Integer.parseInt(curWeapon[6]);
				int rarity    = Integer.parseInt(curWeapon[7]);
				Weapon w = new Weapon(0,0, name, damage, armor, maxHP, detection, range, dot, rarity);
				itemPool.addItem(w);				
			}
			file = new File("assets/armor.csv");
			sc = new Scanner(file);
			sc.nextLine();
			while (sc.hasNextLine()) {
				String[] curArmor = sc.nextLine().split(",");
				String name   = curArmor[0];
				String type	  = curArmor[1];
				int damage    = Integer.parseInt(curArmor[2]);
				int armor     = Integer.parseInt(curArmor[3]);
				int maxHP     = Integer.parseInt(curArmor[4]);
				int detection = Integer.parseInt(curArmor[5]);
				int moveSpeed = Integer.parseInt(curArmor[6]);
				int rarity 	  = Integer.parseInt(curArmor[7]);
				Armor a = new Armor(0,0, name, type, damage, armor, maxHP, detection, moveSpeed, rarity);
				itemPool.addItem(a);
			}
			file = new File("assets/accessories.csv");
			sc = new Scanner(file);
			sc.nextLine();
			while (sc.hasNextLine()) {
				String[] curAccessory = sc.nextLine().split(",");
				String name   = curAccessory[0];
				int damage    = Integer.parseInt(curAccessory[1]);
				int armor     = Integer.parseInt(curAccessory[2]);
				int maxHP     = Integer.parseInt(curAccessory[3]);
				int detection = Integer.parseInt(curAccessory[4]);
				int moveSpeed = Integer.parseInt(curAccessory[5]);
				int passiveHP = Integer.parseInt(curAccessory[6]);
				int rarity 	  = Integer.parseInt(curAccessory[7]);
				Accessory a = new Accessory(0,0, name, damage, armor, maxHP, detection, moveSpeed, passiveHP, rarity);
				itemPool.addItem(a);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
	}
	
	// For unit testing purposes
	public void initAllGrassTerrain() {
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				terrainGrid[x][y] = new Grass(0.5, x, y);
			}
		}
	}
	
	public void initAllMountainTerrain() {
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				terrainGrid[x][y] = new Mountain(0.85, x, y);
			}
		}
	}
	
	public void initAllFireTerrain() {
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				terrainGrid[x][y] = new Forest(x, y);
				((Forest) terrainGrid[x][y]).setFire();
			}
		}
	}
	
	public void initAllBurntTerrain() {
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				terrainGrid[x][y] = new Forest(x, y);
				((Forest) terrainGrid[x][y]).extinguish();
			}
		}
	}
}
