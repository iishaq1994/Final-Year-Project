package game;

// Imports
import java.awt.Graphics;

public class Stage {
	
	// Int variables 
	public int worldWidth = 12;
	public int worldHeight = 8;
	public int cellSize = 64;

	public Cell[][] cell;

	public Stage() {
		define();
	}
	
	// Creates the game window according to the values above (IF CHANGED HERE MAKE SURE YOU CHANGE THE LEVEL FILES TO REFELCT THIS CHANGE)
	public void define() {
		cell = new Cell[worldHeight][worldWidth];

		for (int y = 0; y < cell.length; y++)
			for (int x = 0; x < cell[0].length; x++)
				cell[y][x] = new Cell((Screen.myWidth / 2) - ((worldWidth * cellSize) / 2) + (x * cellSize), y * cellSize, cellSize, cellSize, Value.groundGrass, Value.airAir);

	}
	
	// Allows the enemies to move across cells
	public void physics() {
		for (int y = 0; y < cell.length; y++) {
			for (int x = 0; x < cell[0].length; x++) {
				cell[y][x].physics();
			}
		}
	}
	// Draws the graphics onto the frame
	public void draw(Graphics g) {
		for (int y = 0; y < cell.length; y++)
			for (int x = 0; x < cell[0].length; x++)
				cell[y][x].draw(g);
		for (int y = 0; y < cell.length; y++)
			for (int x = 0; x < cell[0].length; x++)
				cell[y][x].shoot(g);
	}
}