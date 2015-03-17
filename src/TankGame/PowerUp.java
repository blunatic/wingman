package TankGame;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/**
 * Sets up and displays power up item in the game.
 * @param wingman the tankgame game instance
 * @param img image of powerup
 * @param x xcoordinate for displaying 
 * @param y ycoordinate for displaying
 * @param gen random generator
 */
public class PowerUp implements Observer {
	private final TankGame tankgame;
	Image img;
	int x, y, width, height;
	Random gen;
	Rectangle bbox;
	boolean show;

	PowerUp(TankGame tankgame, Image img, int x, int y, Random gen) {
		this.tankgame = tankgame;
		this.img = img;
		this.x = x;
		this.y = y;
		this.gen = gen;
		this.width = img.getWidth(null);
		this.height = img.getHeight(null);
		this.show = true;
	}

	public void update() {

		if (this.tankgame.m1.collision(this.x, this.y, this.width, this.height)) {
			TankGame.gameEvents.setValue("PowerUp1");
			TankGame.gameEvents.setValue("");
			this.tankgame.pUp.show = false;
			this.reset();
		}

		if (this.tankgame.m2.collision(this.x, this.y, this.width, this.height)) {
			TankGame.gameEvents.setValue("PowerUp2");
			TankGame.gameEvents.setValue("");
			this.tankgame.pUp.show = false;
			this.reset();
		}
	}

	public void update(Observable obj, Object arg) {
		GameEvents ge = (GameEvents) arg;
		if (ge.type == 2) {
			String msg = (String) ge.event;
			if (msg.equals("PowerUp")) {
				System.out.println("Power Up: ");
			}
		}
	}

	public void draw(Graphics2D g2, ImageObserver obs) {
		if (show)
			this.tankgame.g2.drawImage(img, x, y, obs);
	}

	public boolean collision(int x, int y, int w, int h) {
		bbox = new Rectangle(x, y, width, height);
		Rectangle otherBBox = new Rectangle(x, y, w, h);
		if (this.bbox.intersects(otherBBox)) {
			return true;
		}
		return false;
	}

	public void reset() {
		this.x = Math.abs(this.gen.nextInt() % (1000 - 30));
		this.y = Math.abs(this.gen.nextInt() % (700 - 30));
	}
}