package TankGame;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.util.Observable;

/**
 * Sets up and displays bullet objects from the player's tank.
 * 
 * @param wingman
 *            the tankgame game instance
 * @param p
 *            tank object that will use bullet
 * @param img
 *            image of bullet
 * @param speed
 *            speed of bullet
 * @param angX
 *            angle for bullet to fly when shot from tank and tank is turned (x coord)
 * @param angY
 *            angle for bullet to fly when shot from tank and tank is turned (y coord)
 */

public class Bullet {
	private final TankGame tankgame;
	Image img;
	int x, y, speed, width, height, angX, angY;
	boolean show;
	Rectangle bbox;
	String bulletOwner;
	Tank t;

	Bullet(TankGame tankgame, Tank t, Image img, int speed, int angX, int angY) {
		this.tankgame = tankgame;
		this.img = img;
		this.speed = speed;
		this.x = t.x + 20;
		this.y = t.y;
		this.angX = angX;
		this.angY = angY;
		this.show = true;
		this.width = img.getWidth(null);
		this.height = img.getHeight(null);
		this.bulletOwner = t.playerName;
		this.t = t;
	}

	public void update() {
		this.x += this.angX;
		this.y -= this.angY;

		// Tank 1's Bullet vs. Tank 2 Collision
		if (this.tankgame.m2.collision(x, y, width, height)) {
			// bump tank back
			if (this.tankgame.b.bulletOwner == "Player 2") {
				TankGame.gameEvents.setValue("");
			} else {
				TankGame.gameEvents.setValue("ShotBy1");
				TankGame.gameEvents.setValue("");
				this.show = false;
				this.tankgame.myBullets.remove(this.tankgame.b);
			}
		} else {
			TankGame.gameEvents.setValue("");
		}

		// Tank 2's Bullet vs. Tank 1 Collision
		if (this.tankgame.m1.collision(x, y, width, height)) {
			// bump tank back
			if (this.tankgame.b.bulletOwner == "Player 1") {
				TankGame.gameEvents.setValue("");
			} else {
				TankGame.gameEvents.setValue("ShotBy2");
				TankGame.gameEvents.setValue("");
				this.show = false;
				this.tankgame.myBullets.remove(this.tankgame.b);

			}
		} else {
			TankGame.gameEvents.setValue("");
		}

	}

	public void draw(Graphics2D g2, ImageObserver obs) {
		AffineTransform temp = g2.getTransform();
		g2.rotate(Math.toRadians(this.t.turningAngle), this.x
				+ (this.width / 2), this.y + (this.height / 2));
		g2.drawImage(this.img, this.x, this.y, obs);
		g2.setTransform(temp);

	}

	public boolean collision(int x, int y, int w, int h) {
		bbox = new Rectangle(this.x, this.y, this.width, this.height);
		Rectangle otherBBox = new Rectangle(x, y, w, h);
		if (this.bbox.intersects(otherBBox)) {
			return true;
		}
		return false;
	}
}