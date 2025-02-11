package main;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundEffect {

    public static void playSound(String soundFile) {
        try {
            // Cargar el archivo de sonido
            File sound = new File("sounds/" + soundFile);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(sound);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            // Reproducir el sonido
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}

