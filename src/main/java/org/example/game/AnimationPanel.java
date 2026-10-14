package org.example.game;

import javax.swing.*;
import java.awt.*;

/**
 * This is my animation panel for displaying prize and feedback images.
 * Required element: Animation with images (Check mark, X mark, and prize images)
 * This panel displays an image and then clears it after a short delay.
 */
public class AnimationPanel extends JPanel {
    // Stores the image to display
    private ImageIcon displayedImage;

    // Stores the name of the prize or feedback message to show with the image
    private String displayedCaption;

    // Counter to track how long the image has been shown
    private int animationCounter;

    // How long to show the image (in timer ticks)
    private static final int ANIMATION_DURATION = 20;

    // Constructor for AnimationPanel
    public AnimationPanel() {
        // Start with no image
        displayedImage = null;
        displayedCaption = null;
        animationCounter = 0;

        // Set the size of this panel
        setPreferredSize(new Dimension(300, 340));

        // Set background color to white
        setBackground(Color.WHITE);
    }

    // Load and display an image for animation
    public void showImage(String fileName) {
        showImage(fileName, null);
    }

    // Load and display an image with a caption
    public void showImage(String fileName, String caption) {
        // Reset the animation counter
        animationCounter = 0;
        displayedCaption = caption;

        // Try to load the image
        try {
            // Find the image file in the resources folder
            java.net.URL imageUrl = getClass().getResource("/images/" + fileName);

            // If the file exists, load it and scale it
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);

                // Scale the image to fit the panel
                Image scaledImage = icon.getImage().getScaledInstance(
                    300,  // width
                    260,  // height
                    Image.SCALE_SMOOTH  // high quality scaling
                );

                // Store the scaled image
                displayedImage = new ImageIcon(scaledImage);
            }
        } catch (Exception e) {
            // If anything goes wrong, print the error
            System.err.println("Error loading image: " + e.getMessage());
        }

        // Repaint the panel to show the image
        repaint();
    }

    // Update the animation (called by a timer in GameFrame)
    public void updateAnimation() {
        // Increase the counter
        animationCounter++;

        // Repaint to show the current state
        repaint();
    }

    // Check if the animation is still playing
    public boolean isAnimating() {
        // Return true if we haven't reached the end of the animation yet
        return animationCounter < ANIMATION_DURATION;
    }

    // Clear the displayed image
    public void clearImage() {
        // Remove the image
        displayedImage = null;
        displayedCaption = null;
        // Reset counter
        animationCounter = 0;
        // Repaint to clear the screen
        repaint();
    }

    // This method is called by Swing to draw the panel
    @Override
    protected void paintComponent(Graphics g) {
        // Call the parent method to draw the background
        super.paintComponent(g);

        // Only draw if there's an image to display
        if (displayedImage != null) {
            // Draw the image on the panel
            displayedImage.paintIcon(this, g, 0, 0);

            // Draw the caption under the image if one is set
            if (displayedCaption != null && !displayedCaption.isBlank()) {
                Graphics2D graphics2D = (Graphics2D) g.create();
                graphics2D.setColor(Color.BLACK);
                graphics2D.setFont(getFont().deriveFont(Font.BOLD, 18f));

                FontMetrics metrics = graphics2D.getFontMetrics();
                int textWidth = metrics.stringWidth(displayedCaption);
                int x = Math.max(0, (getWidth() - textWidth) / 2);
                int y = 300;
                graphics2D.drawString(displayedCaption, x, y);
                graphics2D.dispose();
            }
        }
    }
}



