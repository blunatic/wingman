package TankGame;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

import javax.swing.*;
import javax.imageio.ImageIO;

import java.util.Random;
import java.io.BufferedReader;
import java.io.File;
import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;

import javax.sound.sampled.AudioSystem;

import com.opencsv.CSVReader;

/**
 * Main TankGame class
 * 
 * Sets up all game objects and runs the game.
 * 
 * @author Brendan Luna
 * 
 *         Due December 14th 2014 CSC 413.02 - Yoon
 * 
 */

public class TankGame extends JApplet implements Runnable {

	private Thread thread;
	CSVReader csvReader;
	BufferedReader buff;
	Bullet b;
	Bullet diagB;
	private BufferedImage bimg;
	Image miniMap, leftSide, rightSide;
	Graphics2D g2;
	int move = 0;
	int playerHealth = 20;
	int numberOfLives = 4;
	Random generator = new Random(123456789);

	Tank m1, m2;
	Explosion exp1;
	ScorePanel scoreBar1;
	ScorePanel scoreBar2;
	PowerUp pUp;
	Boolean boolGameOver = false;
	AudioPlayer playMusic, explosionSound1, explosionSound2;

	int w = 1500, h = 930; // fixed size window game
	static GameEvents gameEvents;

	ArrayList<Bullet> myBullets = new ArrayList<Bullet>(); // bullet array list
															// for player
	ArrayList<Bullet> diagonalBullets = new ArrayList<Bullet>();

	ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();

	Wall[] breakableWalls = new Wall[100];
	Wall[] solidWalls = new Wall[100];
	ArrayList<Wall> level = new ArrayList<Wall>();

	Image[] explosionImages = new Image[6];

	// 45 x 28 tile-map of walls
	int[][] map;

	Image background, myTank, myTank2, breakWall, strongWall, enemyImg,
			normalBulletShell, powerBulletShell, bulletright, bulletleft,
			explosion1, life, powerup, powerup2, gameover;

	public void init() {
		setFocusable(true);

		playMusic = new AudioPlayer(this, "Resources/backgroundTune.wav");
		playMusic.play();
		playMusic.loop();

		explosionSound1 = new AudioPlayer(this, "Resources/snd_explosion1.wav");
		explosionSound2 = new AudioPlayer(this, "Resources/snd_explosion2.wav");

		try {
			// load game map file (csv -> txt) into buffered reader
			csvReader = new CSVReader(new FileReader(new File(
					"Resources/gamemapTest.csv")));
			List<String[]> lines = csvReader.readAll();

			// convert csv list for wall spots for a 2D tile-map array
			String[][] data = new String[lines.size()][];
			data = lines.toArray(data);

			// map for holding walls
			map = new int[lines.size()][lines.size()];
			
			// set csv 0, 1, 2 values into map of walls
			for (int x = 0; x < data.length; x++) {
				for (int y = 0; y < data[x].length; y++) {
					map[x][y] = Integer.parseInt(data[x][y]);
				}
			}

			// load all game images
			background = ImageIO.read(new File("Resources/Background.png"));
			myTank = ImageIO.read(new File("Resources/tank_blue_light.png"));
			myTank2 = ImageIO.read(new File(
					"Resources/Tank_red_basic_strip60/Tank_red_basic_16.png"));
			breakWall = ImageIO.read(new File("Resources/Blue_wall1.png"));
			strongWall = ImageIO.read(new File("Resources/Blue_wall2.png"));
			enemyImg = ImageIO.read(new File("Resources/enemy1_1.png"));
			normalBulletShell = ImageIO.read(new File(
					"Resources/Shell_basic_strip60/Shell_basic_16.png"));
			powerBulletShell = ImageIO.read(new File(
					"Resources/Shell_heavy_strip60/Shell_heavy_16.png"));
			bulletright = ImageIO.read(new File("Resources/bulletRight.png"));
			bulletleft = ImageIO.read(new File("Resources/bulletLeft.png"));

			powerup = ImageIO.read(new File("Resources/Pickup_2.png"));
			powerup2 = ImageIO.read(new File("Resources/health_1.png"));

			explosion1 = ImageIO.read(new File("Resources/explosion1_1.png"));

			gameover = ImageIO.read(new File("Resources/gameOver.png"));

			life = ImageIO.read(new File("Resources/Weapon_strip4_4.png"));

			m1 = new Tank(this, "Player 1", myTank, 1325, 750, 15,
					playerHealth, numberOfLives);
			m2 = new Tank(this, "Player 2", myTank2, 50, 750, 15, playerHealth,
					numberOfLives);

			scoreBar1 = new ScorePanel(this, life, 25, 75, m2);
			scoreBar2 = new ScorePanel(this, life, 1325, 75, m1);

			pUp = new PowerUp(this, powerup, 0, 0, generator);

			gameEvents = new GameEvents();
			gameEvents.addObserver(m1);
			gameEvents.addObserver(pUp);
			gameEvents.addObserver(scoreBar1);
			gameEvents.addObserver(scoreBar2);

			// add walls to map
			for (int x = 0; x < data.length; x++) {
				for (int y = 0; y < map[x].length; y++) {
					if (map[x][y] == 1) {
						level.add(new Wall(this, breakWall, y * 32, x * 32,
								true));
					} else if (map[x][y] == 2) {
						level.add(new Wall(this, strongWall, y * 32, x * 32,
								false));
					}
				}
			}

			KeyControl key = new KeyControl();
			addKeyListener(key);

		} catch (Exception e) {
			System.out.print("No resources are found");
		}
	}

	public void loadTankBullets(Tank tank, int vertSpeed, int x, int y) {
		if (tank.powerUp) {
			myBullets.add(new Bullet(this, tank, powerBulletShell, vertSpeed,
					x, y));
		} else {
			myBullets.add(new Bullet(this, tank, normalBulletShell, vertSpeed,
					x, y));
		}
	}

	public void loadExplosion1(Object p, int x, int y) {
		exp1 = new Explosion(this, explosion1, x, y);
		exp1.draw(g2, this);
	}

	public void drawBackGroundWithTileImage() {
		int TileWidth = background.getWidth(this);
		int TileHeight = background.getHeight(this);

		int NumberX = (int) (w / TileWidth);
		int NumberY = (int) (h / TileHeight);

		for (int i = -1; i <= NumberY; i++) {
			for (int j = 0; j <= NumberX; j++) {
				g2.drawImage(background, j * TileWidth, i * TileHeight
						+ (move % TileHeight), TileWidth, TileHeight, this);
			}
		}
	}

	public void drawDemo() {
		if (!boolGameOver) {
			drawBackGroundWithTileImage();

			for (int i = 0; i < level.size(); i++) {
				level.get(i).draw(g2, this);
			}

			for (int i = 0; i < level.size(); i++) {
				level.get(i).update();
			}

			// bullet power up
			powerUps.add(new PowerUp(this, powerup, 750, 100, generator));
			// health power up
			powerUps.add(new PowerUpHealth(this, powerup2, 750, 600, generator));
			powerUps.get(0).draw(g2, this);
			powerUps.get(1).draw(g2, this);

			m1.draw(this);
			m2.draw(this);
			powerUps.get(0).update();
			powerUps.get(1).update();

			// draw player bullets
			for (int i = 0; i < myBullets.size(); i++) {
				b = myBullets.get(i);
				// remove player bullets when they're out of frame
				if (b.y < 0 || b.y > 896 || b.x < 0 || b.y > 1600) {
					myBullets.remove(i);
				} else {
					b.update();
					b.draw(g2, this);
				}
			}

			// set left side, right side screens and mini map boundaries
			// (for scaling the left and right side images)
			int xLeft, yLeft, xRight, yRight, width, height;
			xLeft = m2.x - 120;
			yLeft = m2.y - 300;
			xRight = m1.x - 120;
			yRight = m1.y - 300;

			width = 400;
			height = 600;
			if (xLeft < 0) {
				xLeft = 0;
			}

			if (xRight < 0) {
				xRight = 0;
			}

			if (yLeft < 0) {
				yLeft = 0;
			}

			if (yRight < 0) {
				yRight = 0;
			}
			if (xLeft + width > 1490) {
				xLeft = 1095;
			}

			if (xRight + width > 1490) {
				xRight = 1095;
			}

			if (yLeft + height > 900) {
				yLeft = 300;
			}

			if (yRight + height > 900) {
				yRight = 300;
			}

			leftSide = bimg.getSubimage(xLeft, yLeft, width, height);
			rightSide = bimg.getSubimage(xRight, yRight, width, height);

			leftSide = leftSide.getScaledInstance(800, 900, Image.SCALE_FAST);
			rightSide = rightSide.getScaledInstance(800, 900, Image.SCALE_FAST);
			miniMap = bimg.getScaledInstance(400, 225, Image.SCALE_FAST);

			// render and grab single frame
			BufferedImage display = new BufferedImage(this.w, this.h,
					BufferedImage.TYPE_INT_RGB);
			Graphics temp = display.getGraphics();

			temp.drawImage(leftSide, 0, 0, null);
			temp.drawImage(rightSide, 0, 0, null);
			temp.drawImage(miniMap, 600, 640, null);

			// draw left side, right side and minimap images on screen
			g2.drawImage(leftSide, 0, 0, this);
			g2.drawImage(rightSide, 800, 0, this);
			g2.drawImage(miniMap, 575, 600, this);

			// draw scorebar
			scoreBar1.draw(g2, this);
			scoreBar2.draw(g2, this);

			// display & check number of lives
			// also check for game over if lives are out
			if (m1.health <= 0) {
				m1.show = false;
				m1.numLives -= 1;
				if (m1.numLives > 0) {
					m1.health = playerHealth;
				}
				m1.show = true;
				m1.powerUp = false;
			}
			if (m2.health <= 0) {
				m2.show = false;
				m2.numLives -= 1;
				if (m2.numLives > 0) {
					m2.health = playerHealth;
				}
				m2.show = true;
				m2.powerUp = false;
			}

			if (m1.numLives == 0 || m2.numLives == 0) {
				boolGameOver = true;
			}

		}

		// game over screen
		if (boolGameOver) {
			drawBackGroundWithTileImage();

			g2.drawImage(gameover, 600, 300, this);

			scoreBar1.draw(g2, this);
			scoreBar2.draw(g2, this);
		}
	}

	public void paint(Graphics g) {
		if (bimg == null) {
			Dimension windowSize = getSize();
			bimg = (BufferedImage) createImage(windowSize.width,
					windowSize.height);
			g2 = bimg.createGraphics();
		}
		drawDemo();
		g.drawImage(bimg, 0, 0, this);
	}

	public void start() {
		thread = new Thread(this);
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
	}

	public void run() {

		Thread me = Thread.currentThread();
		while (thread == me) {
			repaint();
			try {
				thread.sleep(15);
			} catch (InterruptedException e) {
				break;
			}

		}
	}

	public static void main(String argv[]) {
		final TankGame demo = new TankGame();
		demo.init();
		JFrame f = new JFrame("TankGame!");
		f.addWindowListener(new WindowAdapter() {
		});
		f.getContentPane().add("Center", demo);
		f.pack();
		f.setSize(new Dimension(1500, 930));
		f.setVisible(true);
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		demo.start();
	}

}
