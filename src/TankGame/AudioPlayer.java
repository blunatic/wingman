package TankGame;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Used for loading, playing, and looping sound files within the game.
 * @param wingman the TankGame game instance
 * @param filename the String filename of the audio file
 */
public class AudioPlayer {
	private final TankGame tankgame;
	private Clip clip;

	public AudioPlayer(TankGame wingman, String filename) {
		this.tankgame = wingman;
		try {
			File file = new File(filename);
			if (file.exists()) {
				AudioInputStream sound = AudioSystem
						.getAudioInputStream(file);
				// load the sound into memory (a Clip)
				clip = AudioSystem.getClip();
				clip.open(sound);
			} else {
				throw new RuntimeException("Sound: file not found: "
						+ filename);
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException("Sound: Malformed URL:" + e);
		}

		catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
			throw new RuntimeException("Sound: Unsupported Audio File: "
					+ e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Sound: Input/Output Error: " + e);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Sound: Line Unavailable Exception Error: " + e);
		}

		// play, stop, loop the sound clip
	}

	public void play() {
		clip.setFramePosition(0); // Must always rewind!
		clip.start();
	}

	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stop() {
		clip.stop();
	}
}