package game;

// Imports
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Enemy extends Rectangle {

	private static final long serialVersionUID = 1L;
	
	// Int variables
	public int xCoord, yCoord;
	public int health, maxHealth = 200;
	public int healthSpace = 2, healthHeight = 4;
	public int enemeySize = Screen.stage.cellSize;
	public int direction;
	public int enemyID = Value.enemyAir;
	public final int up = 0, down = 1, right = 2, left = 3;
	
	// Double variables
	public double xd, yd;
	public double enemeyWalk;
	
	public boolean inGame = false, died = false;

	public Enemy() {

	}
	// Spawns the enemies
	public void spawnEnemy(int enemyID) {
		for (int y = 0; y < Screen.stage.cell.length; y++) {
			if (Screen.stage.cell[y][0].groundID == Value.groundRoad) {
				setBounds(Screen.stage.cell[y][0].x, Screen.stage.cell[y][0].y, enemeySize, enemeySize);
				xd = x;
				yd = this.y;
				xCoord = 0;
				yCoord = y;
				enemeyWalk = 0.0;
				direction = right;
				health = maxHealth;
				break;
			}
		}

		this.enemyID = enemyID;

		inGame = true;
	}

	public void deleteEnemey() {
		inGame = false;
	}
	
	// Moves the health bar down as the enemy loses health
	public void loseHealth() {
		Screen.life -= 1;
	}

	public double walkFrame = 1, enemyWalkSpeed = 2;

	public void physics(int i) {

		if (direction == up) {
			yd -= enemyWalkSpeed;
			y = (int) yd;
		} else if (direction == down) {
			yd += enemyWalkSpeed;
			y = (int) yd;
		} else if (direction == right) {
			xd += enemyWalkSpeed;
			x = (int) xd;
		} else if (direction == left) {
			xd -= enemyWalkSpeed;
			x = (int) xd;
		}

		enemeyWalk += enemyWalkSpeed;
		if (enemeyWalk >= Screen.stage.cellSize) {
			if (direction == up) {
				yCoord--;
			} else if (direction == down) {
				yCoord++;
			} else if (direction == right) {
				xCoord++;
			} else if (direction == left) {
				xCoord--;
			}
			
			// Makes the enemies only walk on the road tile.

			try {
				if (direction != down && direction != up) {
					if (Screen.stage.cell[yCoord + 1][xCoord].groundID == Value.groundRoad) {
						direction = down;
					} else if (Screen.stage.cell[yCoord - 1][xCoord].groundID == Value.groundRoad) {
						direction = up;
					}
				} else if (direction != right && direction != left) {
					if (Screen.stage.cell[yCoord][xCoord + 1].groundID == Value.groundRoad && direction != left) {
						direction = right;
					} else if (Screen.stage.cell[yCoord][xCoord - 1].groundID == Value.groundRoad && direction != right) {
						direction = left;
					}
				}
			} catch (Exception e) {
			}

			if (Screen.stage.cell[yCoord][xCoord].airID == Value.airHouse) {
				deleteEnemey();
				loseHealth();
			}

			enemeyWalk -= Screen.stage.cellSize;
		}

	}

	public void loseHealth(int amount) {
		health -= amount;
		checkDeath();
	}

	public void checkDeath() {
		if (health <= 0 && died == false) {
			deleteEnemey();
			died = true;
			getMoney();
			Screen.killed++;
			Screen.hasWon();
		}
	}
	
	// Adds the money to the coin total
	public void getMoney() {
		Screen.coins += Value.deathReward[enemyID];
	}

	public boolean isDead() {
		return died;
	}

	public void draw(Graphics g) {
		
		// Draws the enemies
		g.drawImage(Screen.tilesetEnemey[enemyID], x, y, width, height, null);
		
		// Draws their health bars starting green and turning red as they lose health
		g.setColor(new Color(219, 26, 13));
		g.fillRect(x, y - healthSpace - healthHeight, enemeySize, healthHeight);

		g.setColor(new Color(61, 234, 35));
		g.fillRect(x, y - healthSpace - healthHeight, enemeySize * health / maxHealth, healthHeight);

		g.setColor(new Color(0, 0, 0));
		g.drawRect(x, y - healthSpace - healthHeight, enemeySize, healthHeight);

	}

}