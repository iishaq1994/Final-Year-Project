package game;

// Imports
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Screen extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	public Thread thread = new Thread(this);
	private Frame frame;
	
	// Int variables
	public static int myWidth, myHeight;
	public static int coins = 10, life = 100;
	public static int killed = 0, killsToWin = 0;
	public static int level = 1, maxLevel = 5;

	// Boolean variables
	public static boolean isFirst = true;
	public static boolean isDebug = false;
	public static boolean won = false;
	
	public static double fps = 60.0;
	
	// Makes the original mouse point location at the 0, 0 coordinate
	public static Point mse = new Point(0, 0);

	public static Stage stage;
	public static Save save;
	public static Shop shop;
	
	// Imports images so they can be used in the game
	public static Image[] tilesetGround = new Image[10];
	public static Image[] tilesetAir = new Image[10];
	public static Image[] tilesetRes = new Image[10];
	public static Image[] tilesetEnemey = new Image[10];
	public static Enemy[] enemies = new Enemy[10];

	public Screen(Frame frame) {
		this.frame = frame;
		frame.addMouseListener(new MouseTracker());
		frame.addMouseMotionListener(new MouseTracker());
		thread.start();
	}
	
	// Keeps track of the number of kills and advances to the next stage when the number has been met. This number can be changed in the level files.
	public static void hasWon() {
		if (killed >= killsToWin) {
			killed = 0;
			won = true;
		}
	}

	public void define() {
		stage = new Stage();
		save = new Save();
		shop = new Shop();
		
		// Loads the images and splits into separate squares
		for (int i = 0; i < tilesetGround.length; i++) {
			tilesetGround[i] = new ImageIcon("res/tileset_ground.png").getImage();
			tilesetGround[i] = createImage(new FilteredImageSource(tilesetGround[i].getSource(), new CropImageFilter(0, 26 * i, 26, 26)));
		}

		for (int i = 0; i < tilesetAir.length; i++) {
			tilesetAir[i] = new ImageIcon("res/tileset_air.png").getImage();
			tilesetAir[i] = createImage(new FilteredImageSource(tilesetAir[i].getSource(), new CropImageFilter(0, 26 * i, 26, 26)));
		}

		tilesetRes[0] = new ImageIcon("res/cell.png").getImage();
		tilesetRes[1] = new ImageIcon("res/coin.png").getImage();
		tilesetRes[2] = new ImageIcon("res/heart.png").getImage();
		tilesetEnemey[0] = new ImageIcon("res/enemy.png").getImage();
		
		// Loads the next level from the levels folder
		save.loadSave(new File("levels/level" + level + ".lv"));

		// Creates the number of enemies according to the level
		for (int i = 0; i < enemies.length; i++) {
			enemies[i] = new Enemy();
		}
	}

	public void paintComponent(Graphics g) {
		if (isFirst) {
			myWidth = getWidth();
			myHeight = getHeight();
			define();

			isFirst = false;
		}

		if (!won) {
			
			// Sets the colour of the background and draws a black border around the game screen.

			g.setColor(new Color(120, 120, 120));
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(new Color(0, 0, 0));
			g.drawLine(stage.cell[0][0].x - 1, 0, stage.cell[0][0].x - 1, stage.cell[stage.worldHeight - 1][0].y + stage.cellSize);
			g.drawLine(stage.cell[0][stage.worldWidth - 1].x + stage.cellSize, 0, stage.cell[0][stage.worldWidth - 1].x + stage.cellSize, stage.cell[stage.worldHeight - 1][0].y + stage.cellSize);
			g.drawLine(stage.cell[0][0].x - 1, stage.cell[stage.worldHeight - 1][0].y + stage.cellSize, stage.cell[0][stage.worldWidth - 1].x + stage.cellSize, stage.cell[stage.worldHeight - 1][0].y + stage.cellSize);

			stage.draw(g);

			for (int i = enemies.length - 1; i >= 0; i--) {
				if (enemies[i].inGame) {
					enemies[i].draw(g);
				}
			}

			shop.draw(g);

			
			// Displays the game over screen if the player loses all of their lives
			
			if (life < 1) {
				g.setColor(new Color(120, 120, 120));
				g.fillRect(0, 0, myWidth, myHeight);
				g.setColor(new Color(255, 255, 255));
				g.setFont(new Font("Courier", Font.BOLD, 15));
				g.drawString("Game Over. The game will close shortly", 10, 20);
			}
			
			// Displays the next level screen if the player successfully completes the level

		} else {
			g.setColor(new Color(120, 120, 120));
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(new Color(0, 0, 0));
			g.setFont(new Font("Courier", Font.BOLD, 15));
			String str = "";
			if (level < maxLevel)
				str += "Level complete. Next level is loading...";
			else
				str += "Congratulations you have won. The game will close shortly.";
			g.drawString(str, 10, 20);
		}
	}
	// Controls how often the enemeies spawn
	public double spawnTime = 90, spawnFrame = 0;

	public void enemeySpawn() {
		if (spawnFrame >= spawnTime) {
			for (int i = 0; i < enemies.length; i++) {
				if (!enemies[i].inGame) {
					enemies[i] = new Enemy();
					enemies[i].spawnEnemy(Value.enemeyYellow);
					break;
				}
			}
			spawnFrame = 1;
		} else {
			spawnFrame++;
		}
	}

	public static double timera = 0;

	public static double winFrame = 1, winTime = 5 * (double) (fps);

	// Game loop handles the updating of the screen, fps etc
	
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / fps;
		double delta = 0;
		int updates = 0, frames = 0;

		while (true) {

			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			// Refresh the screen 60 times a second
			
			while (delta >= 1) {
				timera++;
				updates++;

				if (!isFirst && life > 0 && !won) {
					stage.physics();
					enemeySpawn();
					for (int i = 0; i < enemies.length; i++) {
						if (enemies[i].inGame) enemies[i].physics(i);
					}
				} else if (won) {
					if (winFrame >= winTime) {
						if (level >= maxLevel) {
							System.exit(0);
						} else {
							won = false;
							level++;
							coins = 10;
							define();
						}
						winFrame = 1;
					} else {
						winFrame++;
					}
				}

				delta--;
			}

			repaint();
			frames++;

			// Displays the games FPS (frames per second) and the UPS (Updates per second) in the frame
			
			if (System.currentTimeMillis() - timer >= 1000) {
				timer += 1000;
				frame.setTitle(Frame.title + " | ups: " + updates + " | fps: " + frames);
				updates = 0;
				frames = 0;
			}
		}
	}
}