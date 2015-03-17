package TankGame;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;
import java.awt.geom.AffineTransform;

/**
 * Sets up and displays tanks within game.
 * 
 * @param tankgame
 *            the TankGame game instance
 * @param playerName
 *            String of Player's name
 * @param img
 *            image of tank
 * @param x
 *            xcoordinate for displaying
 * @param y
 *            ycoordinate for displaying
 * @param speed
 *            speed of tank
 * @param health
 *            health of the tank to start
 * @param numLives the number of lives to start
 */
public class Tank implements Observer {
	private final TankGame tankgame;
	Image img;
	int x, y, speed, width, height, angX, angY;
	Rectangle bbox;
	boolean boom;
	int health;
	int numLives;
	int score;
	boolean show;
	String playerName;
	boolean powerUp;
	double turningAngle;

	Tank(TankGame tankgame, String playerName, Image img, int x, int y,
			int speed, int health, int numLives) {
		this.tankgame = tankgame;
		this.img = img;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.health = health;
		this.numLives = numLives;
		this.score = 0;
		this.show = true;
		this.playerName = playerName;
		this.width = img.getWidth(null);
		this.height = img.getHeight(null);
		this.powerUp = false;
		this.turningAngle = 0;
	}

	public void draw(ImageObserver obs) {
		// handle rotation of tank
		AffineTransform temp = this.tankgame.g2.getTransform();
		this.tankgame.g2.rotate(Math.toRadians(this.turningAngle), this.x
				+ (this.width / 2), this.y + (this.height / 2));
		this.tankgame.g2.drawImage(this.img, this.x, this.y, obs);
		this.tankgame.g2.setTransform(temp);
	}

	public boolean collision(int x, int y, int w, int h) {
		bbox = new Rectangle(this.x, this.y, this.width, this.height);
		Rectangle otherBBox = new Rectangle(x, y, w, h);
		if (this.bbox.intersects(otherBBox)) {
			return true;
		}
		return false;
	}

	public void shootBullet(Tank tank) {
		angX = (int) (tank.speed * Math.sin(Math.toRadians(tank.turningAngle)));
		angY = (int) (tank.speed * Math.cos(Math.toRadians(tank.turningAngle)));
		this.tankgame.loadTankBullets(tank, speed, angX, angY);
	}

	public void explode() {
		this.tankgame.loadExplosion1(this, x, y);
	}

	public void update(Observable obj, Object arg) {
		GameEvents ge = (GameEvents) arg;
		if (ge.type == 1) {
			KeyEvent e = (KeyEvent) ge.event;
			switch (e.getKeyCode()) {

			case KeyEvent.VK_LEFT:
				this.tankgame.m1.turningAngle -= 15;
				break;
			case KeyEvent.VK_RIGHT:
				this.tankgame.m1.turningAngle += 15;
				break;
			case KeyEvent.VK_UP:
				this.tankgame.m1.x += speed
						* Math.sin(Math
								.toRadians(this.tankgame.m1.turningAngle));
				this.tankgame.m1.y -= speed
						* Math.cos(Math
								.toRadians(this.tankgame.m1.turningAngle));
				break;
			case KeyEvent.VK_DOWN:
				this.tankgame.m1.x -= speed
						* Math.sin(Math
								.toRadians(this.tankgame.m1.turningAngle));
				this.tankgame.m1.y += speed
						* Math.cos(Math
								.toRadians(this.tankgame.m1.turningAngle));
				break;
			case KeyEvent.VK_A:
				this.tankgame.m2.turningAngle -= 15;
				break;
			case KeyEvent.VK_D:
				this.tankgame.m2.turningAngle += 15;
				break;
			case KeyEvent.VK_W:
				this.tankgame.m2.x += speed
						* Math.sin(Math
								.toRadians(this.tankgame.m2.turningAngle));
				this.tankgame.m2.y -= speed
						* Math.cos(Math
								.toRadians(this.tankgame.m2.turningAngle));
				break;
			case KeyEvent.VK_S:
				this.tankgame.m2.x -= speed
						* Math.sin(Math
								.toRadians(this.tankgame.m2.turningAngle));
				this.tankgame.m2.y += speed
						* Math.cos(Math
								.toRadians(this.tankgame.m2.turningAngle));
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;

			default:
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					System.out.println("Fire");
					shootBullet(this.tankgame.m1);
				}
				if (e.getKeyChar() == ' ') {
					System.out.println("Fire");
					shootBullet(this.tankgame.m2);
				}
			}
		} else if (ge.type == 2) {
			String msg = (String) ge.event;
			if (msg.equals("WallHit1")) {
				System.out.println("Wall bumped by Tank 1!");
			}
			if (msg.equals("WallHit2")) {
				System.out.println("Wall bumped by Tank 2!");
			}
			if (msg.equals("WallBulletHit")) {
				System.out.println("Wall destroyed");
			}

			if (msg.equals("PowerUp1")) {
				System.out.println("Power up grabbed by Player 1!");
				this.tankgame.m1.powerUp = true;
				this.tankgame.powerUps.get(0).reset();
			}

			if (msg.equals("PowerUp2")) {
				System.out.println("Power up grabbed by Player 2!");
				this.tankgame.m2.powerUp = true;
				this.tankgame.powerUps.get(0).reset();
			}
			
			if (msg.equals("PowerUpHealth1")) {
				System.out.println("Health grabbed by Player 1!");
				this.tankgame.m1.health += 10;
			}

			if (msg.equals("PowerUpHealth2")) {
				System.out.println("Health grabbed by Player 2!");
				this.tankgame.m2.health += 10;
			}

			if (msg.equals("ShotBy2")) {
				System.out.println("Shot by Tank 2!");
				if (this.tankgame.m2.powerUp) {
					this.tankgame.m1.health -= 15;

				} else {
					this.tankgame.m1.health -= 10;
				}
				this.tankgame.explosionSound1.play();
				this.tankgame.m1.explode();
			}

			if (msg.equals("ShotBy1")) {
				System.out.println("Shot by Tank 1!");
				if (this.tankgame.m1.powerUp) {
					this.tankgame.m2.health -= 15;
				} else {
					this.tankgame.m2.health -= 10;

				}
				this.tankgame.explosionSound1.play();
				this.tankgame.m2.explode();
			}

		}

	}
}