package me.offeex.bloomware.api.util;

import me.offeex.bloomware.Bloomware;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Random;

public class SoundUtil {
    private final static Random random = new Random();

    public static void playSound() {
        if (shouldPlay()) {
            try {
                AudioInputStream stream = AudioSystem.getAudioInputStream(Bloomware.class.getResourceAsStream("/assets/bloomware/sound/pidrila.wav"));
                Clip clip = AudioSystem.getClip();
                clip.open(stream);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean shouldPlay() {
        return random.nextInt(0, 100) <= 1;
    }
}
