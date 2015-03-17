package TankGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;

/**
 * ScorePanel class
 * @param wingman the tankgame game instance
 * @param img image of mini player tanks, used to display lives
 * @param x xcoordinate for displaying 
 * @param y ycoordinate for displaying
 * @param p the tank object to keep scoring and lives for
 */
public class ScorePanel implements Observer {
	private final TankGame tankgame;
	Image img;
	int x, y, width, height;
	Font font = new Font(Font.MONOSPACED, Font.PLAIN, 20);
	Tank p;
	int lives;
	int scoreNumber;

	ScorePanel(TankGame tankgame, Image img, int x, int y, Tank p) {
		this.tankgame = tankgame;
		this.img = img;
		this.x = x;
		this.y = y;
		this.p = p;
		this.scoreNumber = p.score;
	}

	public void draw(Graphics2D g2, ImageObserver obs) {
		lives = x - 30;

		g2.setColor(Color.green);

		// draw player health bar
		if (p.health > 18) {
			g2.setColor(Color.green);
		} else if (p.health > 10) {
			g2.setColor(Color.yellow);
		} else {
			g2.setColor(Color.red);
		}

		// draw lives on screen
		for (int i = 0; i < p.numLives; i++) {
			g2.drawImage(img, lives, 1, obs);
			// space out life images
			lives += 30;
		}
		g2.setFont(font);
		g2.fill3DRect(x, y, p.health * 4, 20, true);
		g2.drawString("SCORE: " + p.score, x - 2, y - 5);
	}

	public void update(Observable obj, Object arg) {
		// null
	}

}