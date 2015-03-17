package TankGame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * KeyControl class
 * 
 * Handles KeyEvents for player's keyboard input
 *
 */
public class KeyControl extends KeyAdapter {
	
	public void keyPressed(KeyEvent e) {
		// set gameEvents keyPress value
		TankGame.gameEvents.setValue(e);
	}

}
