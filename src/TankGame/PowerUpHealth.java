package TankGame;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Random;

public class PowerUpHealth extends PowerUp{
	private final TankGame tankgame;

	PowerUpHealth(TankGame tankgame,Image img, int x, int y, Random gen) {
		super(tankgame, img, x, y, gen);
		this.tankgame = tankgame;
	}
	@Override
		public void update() {

			if (this.tankgame.m1.collision(this.x, this.y, this.width, this.height)) {
				TankGame.gameEvents.setValue("PowerUpHealth1");
				TankGame.gameEvents.setValue("");
				this.tankgame.pUp.show = false;
				this.reset();
			}

			if (this.tankgame.m2.collision(this.x, this.y, this.width, this.height)) {
				TankGame.gameEvents.setValue("PowerUpHealth2");
				TankGame.gameEvents.setValue("");
				this.tankgame.pUp.show = false;
				this.reset();
			}
		}
}
