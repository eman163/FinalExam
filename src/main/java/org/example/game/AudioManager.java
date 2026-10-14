package org.example.game;

import javax.sound.sampled.*;
import java.net.URL;

/**
 * This class handles audio playback for game events.
 * Required element: Audio (plays jeopardy.wav for game events).
 * This fulfills the multimedia requirement of using Audio in the game.
 */
public class AudioManager {
    // Keeps track of the current sound being played
    private Clip currentClip;

    // Constructor for AudioManager
    public AudioManager() {
        currentClip = null;
    }

    // Play a sound file by name
    public void playSound(String soundFileName) {
        // Stop any sound that's currently playing
        stopSound();

        try {
            // Find the sound file in the resources folder
            URL soundUrl = getClass().getResource("/sounds/" + soundFileName);

            // If the file doesn't exist, print an error and stop
            if (soundUrl == null) {
                System.err.println("Sound file not found: " + soundFileName);
                return;
            }

            // Load the audio file
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundUrl);

            // Get a new clip to play the sound
            currentClip = AudioSystem.getClip();

            // Open the clip and load the audio
            currentClip.open(audioStream);

            // Start playing the sound
            currentClip.start();

        } catch (Exception e) {
            // If anything goes wrong, print the error message
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }

    // Stop the currently playing sound
    public void stopSound() {
        // Check if there is a sound playing
        if (currentClip != null && currentClip.isRunning()) {
            // Stop the sound
            currentClip.stop();
            // Close the clip
            currentClip.close();
            // Clear the clip variable
            currentClip = null;
        }
    }

    // Check if a sound is currently playing
    public boolean isPlaying() {
        // Return true if a clip exists and is playing
        return currentClip != null && currentClip.isRunning();
    }
}



