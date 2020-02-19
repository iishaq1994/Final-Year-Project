package game;

// Imports
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Shop {
	
	// Int variables
	public static int shopWidth = 7;
	public static int buttonSize = 64;
	public static int cellSpace = 2;
	public static int largeCellSpace = 21;
	public static int iconSize = 20;
	public static int iconSpace = 6;
	public static int iconTextY = 15;
	public static int itemIn = 4;
	public static int heldId = -1, realId = -1;
	public static int[] shopID = { Value.airTowerCircle, Value.airTowerTriangle, Value.airTowerDiamond, Value.airTowerStar, Value.airTowerPentagon, Value.airAir, Value.airTrashCan };
	public static int[] price = { 5, 10, 15, 20, 25, 0, 0, 0 };

	// Creates the rectangles below the game
	public Rectangle[] cell = new Rectangle[shopWidth];
	public Rectangle cellHealth, cellCoins;

	public boolean holdsItem = false;

	public Shop() {
		define();
	}
	
	// Deletes the item you are holding when you press the trash can
	public void click(int mouseButton) {
		if (mouseButton == 1) {
			for (int i = 0; i < cell.length; i++) {
				if (cell[i].contains(Screen.mse)) {
					if (shopID[i] != Value.airAir) {
						if (shopID[i] == Value.airTrashCan) {
							holdsItem = false;
							heldId = Value.airAir;
						} else {
							heldId = shopID[i];
							realId = i;
							holdsItem = true;
						}
					}
				}
			}
			
			// Allows you to place a tower if you have the right amount of money
			if (holdsItem) {
				if (Screen.coins >= price[realId]) {
					for (int y = 0; y < Screen.stage.cell.length; y++) {
						for (int x = 0; x < Screen.stage.cell[0].length; x++) {
							if (Screen.stage.cell[y][x].contains(Screen.mse)) {
								if (Screen.stage.cell[y][x].groundID != Value.groundRoad && Screen.stage.cell[y][x].airID == Value.airAir) {
									Screen.stage.cell[y][x].airID = heldId;
									Screen.coins -= price[realId];
								}
							}
						}
					}
				}
			}
		}
	}
	// Draws the rectangles for the differnet UI elements
	public void define() {
		for (int i = 0; i < cell.length; i++)
			cell[i] = new Rectangle(Screen.myWidth / 2 - shopWidth * (buttonSize + cellSpace) / 2 + (buttonSize + cellSpace) * i, Screen.stage.cell[Screen.stage.worldHeight - 1][0].y 
									+ Screen.stage.cellSize + largeCellSpace, buttonSize, buttonSize);
		cellHealth = new Rectangle(Screen.stage.cell[0][0].x - 1, cell[0].y, iconSize, iconSize);
		cellCoins = new Rectangle(Screen.stage.cell[0][0].x - 1, cell[0].y + cell[0].height - iconSize, iconSize, iconSize);
	}

	public void draw(Graphics g) {
		

		for (int i = 0; i < cell.length; i++) {
			if (cell[i].contains(Screen.mse)) {
				g.setColor(new Color(255, 255, 255, 150));
				g.fillRect(cell[i].x, cell[i].y, cell[i].width, cell[i].height);
			}
			// Fills in each rectangle with its own tower.
			g.drawImage(Screen.tilesetRes[0], cell[i].x, cell[i].y, cell[i].width, cell[i].height, null);
			if (shopID[i] != Value.airAir) g.drawImage(Screen.tilesetAir[shopID[i]], cell[i].x + itemIn, cell[i].y + itemIn, cell[i].width - itemIn * 2, cell[i].height - itemIn * 2, null);
			if (price[i] > 0) {
				g.setColor(new Color(255, 255, 255));
				g.setFont(new Font("Courier", Font.BOLD, 15));
				g.drawString("$" + price[i], cell[i].x + itemIn, cell[i].y + itemIn + 10);
			}
		}
		// Draws the health and coin icons
		g.drawImage(Screen.tilesetRes[2], cellHealth.x, cellHealth.y, cellHealth.width, cellHealth.height, null);
		g.drawImage(Screen.tilesetRes[1], cellCoins.x, cellCoins.y, cellCoins.width, cellCoins.height, null);
		g.setFont(new Font("Courier", Font.BOLD, 15));
		g.setColor(new Color(255, 255, 255));
		g.drawString("" + Screen.life, cellHealth.x + cellHealth.width + iconSpace, cellHealth.y + iconTextY);
		g.drawString("$" + Screen.coins, cellCoins.x + cellCoins.width + iconSpace, cellCoins.y + iconTextY);
		
		// Draws the item you are currently holding next to your cursor
		if (holdsItem) g.drawImage(Screen.tilesetAir[heldId], Screen.mse.x - (cell[0].width - itemIn * 2) / 2 + itemIn, Screen.mse.y - (cell[0].height - itemIn * 2) / 2 
									+ itemIn, cell[0].width - itemIn * 2, cell[0].height - itemIn * 2, null);
	}
}