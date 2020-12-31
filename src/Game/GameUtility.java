package Game;
import java.util.ArrayList;
import java.util.Random;
import Creatures.*;
import Items.Item;
import Terrain.*;

public class GameUtility {
	public static Random r = new Random();
	
	private final static double TAU = 6.2831853;
	public static double distance(int x1, int y1, int x2, int y2) {
		//return Math.sqrt(((x2-x1)*(x2-x1)) + ((y2-y1)*(y2-y1)));
		return Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
	}
	
	public static int fixPos(Map map, int x) {
		int boundary = map.getSize();
		if (x >= boundary) return (x - boundary);
		else if (x < 0) return (boundary + x);
		else return x;
		
	}
	
	public static boolean isWithinBounds(Map map, int x) {
		int boundary = map.getSize();
		if (x >= boundary || x < 0) return false;
		else return true;
	}
	
	public static int[] findValidRandomMove(Creature c, int speed, Map map, Creature[][] nextGrid) {
		int x = c.getX();
		int y = c.getY();
		int attempts = 0;
		int randX = GameUtility.fixPos(map, (x + (r.nextInt((2 * speed) + 1) - speed)));
		int randY = GameUtility.fixPos(map, (y + (r.nextInt((2 * speed) + 1) - speed)));
		Terrain curTerrain = map.getTerrain(randX, randY);
		// Prevent creature overlap
		while (nextGrid[randX][randY] != null 
			|| (!curTerrain.isPassable() && !c.isFlying())
			|| (curTerrain instanceof Forest && ((Forest) curTerrain).isOnFire())
			|| (randX == c.getX() && randY == c.getY())){
				randX = GameUtility.fixPos(map, (x + (r.nextInt((2 * speed) + 1) - speed)));
				randY = GameUtility.fixPos(map,(y + (r.nextInt((2 * speed) + 1) - speed)));
				curTerrain = map.getTerrain(randX, randY);
			if (attempts++ > 15) return new int[] {-1, -1};
		}
		return new int[] {randX, randY};
	}
	
	public static int[] findValidRandomMoveBounded(Creature c, int speed, Map map, Creature[][] nextGrid) {
		int x = c.getX();
		int y = c.getY();
		int attempts = 0;
		int randX = x + (r.nextInt((2 * speed) + 1) - speed);
		int randY = y + (r.nextInt((2 * speed) + 1) - speed);
		// Prevent creature overlap
		while (GameUtility.isWithinBounds(map, randX) == false 
		    || GameUtility.isWithinBounds(map, randY) == false
		    || (map.getTerrain(randX, randY) instanceof Forest && ((Forest) map.getTerrain(randX, randY)).isOnFire())
		    || nextGrid[randX][randY] != null 
		    || (!map.getTerrain(randX, randY).isPassable()) && !c.isFlying()
		    || (randX == c.getX() && randY == c.getY())) {
				randX = x + (r.nextInt((2 * speed) + 1) - speed);
				randY = y + (r.nextInt((2 * speed) + 1) - speed);
			if (attempts++ > 15) return new int[] {-1, -1};
		}
		return new int[] {randX, randY};
	}
	
	public static int[] getMaxDistanceFromEnemy(Creature c, Map map, Creature[][] nextGrid, boolean bounded) {
		double maxDistance = Double.NEGATIVE_INFINITY;
		int moveSpeed = c.getMoveSpeed();
		ArrayList<Creature> localEnemies = c.getLocalEnemies();
		int	bestX = c.getX(), bestY = c.getY(), curX, curY;
		for (int i = -moveSpeed; i <= moveSpeed; i++) {
			for (int j = -moveSpeed; j <= moveSpeed; j++) {
				
				if (bounded) {
					curX = c.getX() + i;
					curY = c.getY() + j;
				}
				else {
					curX = GameUtility.fixPos(map, c.getX() + i);
					curY = GameUtility.fixPos(map, c.getY() + j);
				}
				
				double totalDist = 0;
				// Only get distance if not bounded, or if it is, the coordiantes must be valid.
				if (!bounded 
				|| (bounded && (GameUtility.isWithinBounds(map, curX) 
				&& GameUtility.isWithinBounds(map, curY)))){
					for (int k = 0; k < localEnemies.size(); k++) {
						int enemyX = localEnemies.get(k).getX();
						int enemyY = localEnemies.get(k).getY();
						// Correct for boundaries
						if (!bounded) {
							if (Math.abs(c.getX() - enemyX) > c.getDetectionRadius()) {
								if (enemyX < map.getSize() / 2) enemyX += map.getSize();
								else if (enemyX > map.getSize() / 2)	enemyX -= map.getSize();	
							}
							if (Math.abs(c.getY() - enemyY) > c.getDetectionRadius()) {
								if (enemyY < map.getSize() / 2) enemyY += map.getSize();
								else if (enemyY > map.getSize() / 2) enemyY -= map.getSize();	
							}	
						}
						totalDist += GameUtility.distance(curX, curY, enemyX, enemyY);
					}
				}
				if (totalDist > maxDistance && nextGrid[curX][curY] == null 
					&& (c.isFlying() || map.getTerrain(curX, curY).isPassable())
					&& (curX != c.getX() && curY != c.getY())) {	
						maxDistance = totalDist;
						bestX = curX;
						bestY = curY;
				}
			}
		}
		
		return new int[] {bestX, bestY};
	}
	
	public static int[] getMinDistanceFromEnemy(Creature c, Map map, Creature[][] nextGrid, boolean bounded) {
		double minDistance = Double.POSITIVE_INFINITY;
		int moveSpeed = c.getMoveSpeed();
		ArrayList<Creature> localEnemies = c.getLocalEnemies();
		int	bestX = c.getX(), bestY = c.getY(), curX, curY;
		for (int i = -moveSpeed; i <= moveSpeed; i++) {
			for (int j = -moveSpeed; j <= moveSpeed; j++) {
				
				if (bounded) {
					curX = c.getX() + i;
					curY = c.getY() + j;
				}
				else {
					curX = GameUtility.fixPos(map, c.getX() + i);
					curY = GameUtility.fixPos(map, c.getY() + j);
				}
				double totalDist = 0;
				// Only get distance if not bounded, or if it is, the coordiantes must be valid.
				if (!bounded 
					|| (bounded && (GameUtility.isWithinBounds(map, curX) 
					&& GameUtility.isWithinBounds(map, curY)))){
					for (int k = 0; k < localEnemies.size(); k++) {
						int targetX = localEnemies.get(k).getX();
						int targetY = localEnemies.get(k).getY();
						// Correct for boundaries
						if (!bounded) {
							if (Math.abs(c.getX() - targetX) > c.getDetectionRadius()) {
								if (targetX < map.getSize() / 2) targetX += map.getSize();
								else if (targetX > map.getSize() / 2)	targetX -= map.getSize();	
							}
							if (Math.abs(c.getY() - targetY) > c.getDetectionRadius()) {
								if (targetY < map.getSize() / 2) targetY += map.getSize();
								else if (targetY > map.getSize() / 2)	targetY -= map.getSize();	
							}	
						}
						totalDist += GameUtility.distance(curX, curY, targetX, targetY);
					}
					if (totalDist < minDistance && nextGrid[curX][curY] == null 
						&& (c.isFlying() || map.getTerrain(curX, curY).isPassable())
						&& (curX != c.getX() && curY != c.getY())) {	
							minDistance = totalDist;
							bestX = curX;
							bestY = curY;
					}
				}
			}
		}
		return new int[] {bestX, bestY};
	}
	
	public static int[] getMinDistanceFromAlly(Creature c, Map map, Creature[][] nextGrid, boolean bounded) {
		double minDistance = Double.POSITIVE_INFINITY;
		int moveSpeed = c.getMoveSpeed();
		ArrayList<Creature> localAllies = c.getLocalAllies();
		int	bestX = c.getX(), bestY = c.getY(), curX, curY;
		for (int i = -moveSpeed; i <= moveSpeed; i++) {
			for (int j = -moveSpeed; j <= moveSpeed; j++) {
				
				if (bounded) {
					curX = c.getX() + i;
					curY = c.getY() + j;
				}
				else {
					curX = GameUtility.fixPos(map, c.getX() + i);
					curY = GameUtility.fixPos(map, c.getY() + j);
				}
				double totalDist = 0;
				// Only get distance if not bounded, or if it is, the coordinates must be valid.
				if (!bounded 
					|| (bounded && (GameUtility.isWithinBounds(map, curX) 
					&& GameUtility.isWithinBounds(map, curY)))){
					for (int k = 0; k < localAllies.size(); k++) {
						int allyX = localAllies.get(k).getX();
						int allyY = localAllies.get(k).getY();
						// Correct for boundaries
						if (!bounded) {
							if (Math.abs(c.getX() - allyX) > c.getDetectionRadius()) {
								if (allyX < map.getSize() / 2) allyX += map.getSize();
								else if (allyX > map.getSize() / 2)	allyX -= map.getSize();	
							}
							if (Math.abs(c.getY() - allyY) > c.getDetectionRadius()) {
								if (allyY < map.getSize() / 2) allyY += map.getSize();
								else if (allyY > map.getSize() / 2)	allyY -= map.getSize();	
							}	
						}
						totalDist += GameUtility.distance(curX, curY, allyX, allyY);
					}
					if (totalDist < minDistance && nextGrid[curX][curY] == null 
						&& (c.isFlying() || map.getTerrain(curX, curY).isPassable())
						&& (curX != c.getX() && curY != c.getY())) {						
							minDistance = totalDist;
							bestX = curX;
							bestY = curY;
					}
				}
			}
		}
		return new int[] {bestX, bestY};
	}
	
	public static int[] getMinDistanceFromItem(Creature c, Map map, Creature[][] nextGrid, boolean bounded) {
		double minDistance = Double.POSITIVE_INFINITY;
		int moveSpeed = c.getMoveSpeed();
		ArrayList<Item> localItems = c.getLocalItems();
		
		int itemIndex = 0;
		int itemX = localItems.get(itemIndex).getX();
		int itemY = localItems.get(itemIndex).getY();
		
		// Correct for boundaries
		if (!bounded) {
			if (Math.abs(c.getX() - itemX) > c.getDetectionRadius()) {
				if (itemX < map.getSize() / 2) itemX += map.getSize();
				else if (itemX > map.getSize() / 2)	itemX -= map.getSize();	
			}
			if (Math.abs(c.getY() - itemY) > c.getDetectionRadius()) {
				if (itemY < map.getSize() / 2) itemY += map.getSize();
				else if (itemY > map.getSize() / 2)	itemY -= map.getSize();	
			}	
		}
		int	bestX = c.getX(), bestY = c.getY(), curX, curY;
		for (int i = -moveSpeed; i <= moveSpeed; i++) {
			for (int j = -moveSpeed; j <= moveSpeed; j++) {
				
				if (bounded) {
					curX = c.getX() + i;
					curY = c.getY() + j;
				}
				else {
					curX = GameUtility.fixPos(map, c.getX() + i);
					curY = GameUtility.fixPos(map, c.getY() + j);
				}
				// If an item is in movement range, move to it.
				for (int k = 0; k < localItems.size(); k++) {
					if (curX == localItems.get(k).getX() && curY == localItems.get(k).getY()) 
						return new int[] {curX, curY};
				}
				
				// Only get distance if not bounded, or if it is, the coordiantes must be valid.
				if (!bounded 
					|| (bounded && (GameUtility.isWithinBounds(map, curX) 
					&& GameUtility.isWithinBounds(map, curY)))){
						double bestDist = 0;
						bestDist = GameUtility.distance(curX, curY, itemX, itemY);
							
					if (bestDist < minDistance && nextGrid[curX][curY] == null 
						&& (c.isFlying() || map.getTerrain(curX, curY).isPassable())
						&& (curX != c.getX() && curY != c.getY())) {
						minDistance = bestDist;
						bestX = curX;
						bestY = curY;
					}
				}
			}
		}
		return new int[] {bestX, bestY};
	}

	public static Creature chooseLowestHealthTarget(ArrayList<Creature> enemies) {
		int lowestHealth = Integer.MAX_VALUE;
		 Creature target = null;
		 for (int i = 0; i < enemies.size(); i++) {
			 Creature current = enemies.get(i);
			 int curHealth = current.getCurrentHealth();
			 if ( curHealth < lowestHealth) {
				 lowestHealth = curHealth;
				 target = current;
			 }
		 }
		 return target;
	}
	
	public static double[][] generateSimplexNoise(int width, int height){
		OpenSimplexNoise noise = new OpenSimplexNoise(r.nextLong());
		double[][] simplexnoise = new double[width][height];
	    double frequency = 5f / (double) width;
	      
	    for(int x = 0; x < width; x++){
	    	for(int y = 0; y < height; y++){
	    		simplexnoise[x][y] = (double) (noise.eval(x * frequency, y * frequency) + 1) / 2; //generate values between 0 and 1
	    		
	    		// Using multiple octaves of noise could have interesting more natural results, but it requires a lot of tweaking to get consistently nice results.
	    		//simplexnoise[x][y] = octaveSum(x, y, 4, 0.01f, 0.4f, noise);
	        }
	      }
	      
	   return simplexnoise;
	   }
	
	public static double octaveSum(int x, int y, int iterations, double scale, double persistence, OpenSimplexNoise noise) {
		double maxAmp = 0;
		double amp = 1;
		double freq = scale;
		double noiseSum = 0;
			
		for(int i = 0; i < iterations; i++) {
	        noiseSum += noise.eval(x * freq, y * freq) * amp;
	        maxAmp += amp;
	        amp *= persistence;
	        freq *= 2;
		}
		
	    noiseSum /= maxAmp;
	    noiseSum = (noiseSum + 1) / 2;
	    //System.out.println(noiseSum);

		return noiseSum;
	}
}
	
	