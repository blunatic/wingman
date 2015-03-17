package TankGame;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/**
 * Sets up and displays walls on the map.
 * 
 * @param tankgame
 *            the tankgame game instance
 * @param img
 *            image of tank
 * @param x
 *            xcoordinate for displaying
 * @param y
 *            ycoordinate for displaying
 * @param breakable boolean to set wall breakable option
 */
public class Wall implements Observer {

	private final TankGame tankgame;
	Image img;
	int x, y, sizeX, sizeY, speed;
	Random gen;
	boolean show;
	Rectangle bbox;
	boolean breakable;

	Wall(TankGame tankgame, Image img, int x, int y, boolean breakable) {
		this.tankgame = tankgame;
		this.img = img;
		this.x = x;
		this.y = y;
		this.show = true;
		this.sizeX = img.getWidth(null);
		this.sizeY = img.getHeight(null);
		this.breakable = breakable;
	}

	public void update() {

		// Wall vs. Tank 1 Collision
		if (this.tankgame.m1.collision(x, y, sizeX, sizeY)) {
			TankGame.gameEvents.setValue("WallHit1");
			TankGame.gameEvents.setValue("");
			// bump tank back horizontal
			if (this.tankgame.m1.x >= this.x) {
				this.tankgame.m1.x += 3;
			} else if (this.tankgame.m1.x <= this.x) {
				this.tankgame.m1.x -= 3;
			}
			// bump tank back vertical
			if (this.tankgame.m1.y > this.y) {
				this.tankgame.m1.y += 3;
			} else if (this.tankgame.m1.y < this.y) {
				this.tankgame.m1.y -= 3;
			}

		} else {
			TankGame.gameEvents.setValue("");
		}
		// Wall vs. Tank 2 Collision
		if (this.tankgame.m2.collision(x, y, sizeX, sizeY)) {
			TankGame.gameEvents.setValue("WallHit2");
			TankGame.gameEvents.setValue("");
			// bump tank back horizontal
			if (this.tankgame.m2.x >= this.x) {
				this.tankgame.m2.x += 3;
			} else if (this.tankgame.m2.x <= this.x) {
				this.tankgame.m2.x -= 3;
			}
			// bump tank back vertical
			if (this.tankgame.m2.y > this.y) {
				this.tankgame.m2.y += 3;
			} else if (this.tankgame.m2.y < this.y) {
				this.tankgame.m2.y -= 3;
			}
		} else {
			TankGame.gameEvents.setValue("");
		}

		// Wall vs. Tank Bullet Collision
		if (this.tankgame.b != null) {
			if (this.tankgame.b.collision(this.x, this.y, this.sizeX,
					this.sizeY)) {
				if (this.breakable) {
					TankGame.gameEvents.setValue("WallBulletHit");
					TankGame.gameEvents.setValue("");
					this.show = false;
					this.tankgame.b.show = false;
					this.tankgame.myBullets.remove(this.tankgame.b);
					this.reset();
				} else {
					TankGame.gameEvents.setValue("");
					this.tankgame.myBullets.remove(this.tankgame.b);
					explode();
				}
			}
		}

	}

	public int getY() {
		return this.y;
	}

	// empty, since wall doesn't need to respond to any messages
	public void update(Observable obj, Object arg) {
		GameEvents ge = (GameEvents) arg;
		if (ge.type == 2) {
			String msg = (String) ge.event;

		}
	}

	public boolean collision(int x, int y, int w, int h) {
		bbox = new Rectangle(this.x, this.y, this.sizeX - 5, this.sizeY - 5);
		Rectangle otherBBox = new Rectangle(x, y, w, h);
		if (this.bbox.intersects(otherBBox)) {
			return true;
		}
		return false;
	}

	public void explode() {
		this.tankgame.loadExplosion1(this, x, y);
	}

	public void reset() {
		this.x = Math.abs(this.tankgame.generator.nextInt() % (600 - 30));
		this.y = -10;
	}

	public void draw(Graphics2D g2, ImageObserver obs) {
		if (show) {
			this.tankgame.g2.drawImage(img, this.x, this.y, obs);
		}
	}
}