package game;

// Imports
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Cell extends Rectangle {

	private static final long serialVersionUID = 1L;
	public Rectangle towerSquare;
	
	//ID number for the ground so each cell can have it's own texture
	public int airID;
	//ID number for the tower so each cell can have a tower on the cell
	public int groundID;
	public int towerSquareSize = 128;
	

	public int shotEnemey = -1;
	public boolean isShooting = false;

	public Cell(int x, int y, int width, int height, int groundID, int airID) {
		setBounds(x, y, width, height);
		towerSquare = new Rectangle(x - towerSquareSize / 2, y - towerSquareSize / 2, width + towerSquareSize, height + towerSquareSize);
		this.airID = airID;
		this.groundID = groundID;
	}
	
	
	// Draws the ground layer
	public void draw(Graphics g) {
		g.drawImage(Screen.tilesetGround[groundID], x, y, width, height, null);
	// Draws the air layer
		if (airID != Value.airAir) {
			g.drawImage(Screen.tilesetAir[airID], x, y, width, height, null);
		}
	}

	public double loseFrame = 1, loseTime = 1 / (double) (Screen.fps);
	
	// Draws the tower shooting if the enemies come into range
	public void physics() {
		if (shotEnemey != -1 && towerSquare.intersects(Screen.enemies[shotEnemey])) {
			isShooting = true;
		} else
			isShooting = false;

		if (!isShooting) {
			if (airID != Value.airAir && airID != Value.airHouse) {
				for (int i = Screen.enemies.length - 1; i >= 0; i--) {
					if (Screen.enemies[i].inGame) {
						if (towerSquare.intersects(Screen.enemies[i])) {
							isShooting = true;
							shotEnemey = i;
						}
					}
				}
			}
		}
		// While the tower shoots lower the health of the target
		if (isShooting) {
			if (loseFrame >= loseTime) {
				Screen.enemies[shotEnemey].loseHealth(1);
				loseFrame -= loseTime;
			} else {
				loseFrame++;
			}
			// Once the target is dead move onto the next one
			if (Screen.enemies[shotEnemey].isDead()) {
				shotEnemey = -1;
				isShooting = false;
				loseFrame = 1;
			}
		}
	}
	
	// Draws the tower shooting at the enemies
	public void shoot(Graphics g) {	
		if (Screen.isDebug) {
			if (airID == Value.airTowerCircle) {
				g.setColor(new Color(0, 0, 0));
				g.drawRect(towerSquare.x, towerSquare.y, towerSquare.width, towerSquare.height);
			}
		}
		if (isShooting) {
			g.setColor(new Color(255, 255, 255));
			g.drawLine(x + width / 2, y + height / 2, Screen.enemies[shotEnemey].x + Screen.enemies[shotEnemey].width / 2, Screen.enemies[shotEnemey].y + Screen.enemies[shotEnemey].height / 2);
		}

	}
}