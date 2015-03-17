package TankGame;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
/**
 * Sets up and displays explosions on strong walls and other player's tank.
 * @param tankgame the tankgame game instance
 * @param img image of diagonal bullet
 */
public class Explosion {

	private final TankGame tankgame;
	Image img;
	int x, y;
	boolean show;
	Tank t;

	Explosion(TankGame tankgame, Image img, int x, int y) {
		this.tankgame= tankgame; 
		this.img = img;
		this.x = x;
		this.y = y;
	}

	public void draw(Graphics2D g2, ImageObserver obs) {
		g2.drawImage(img, x, y, obs);
	}

}