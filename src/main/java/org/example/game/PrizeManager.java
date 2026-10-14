package org.example.game;

import java.util.*;

/**
 * This class manages prizes won during the game.
 * It maintains lists of prizes won and available prizes.
 * Required element: This is my array/list of prize objects.
 */
public class PrizeManager {
    // List of prizes the player has won
    private List<String> prizesList;

    // List of prizes that are still available to win
    private List<String> availablePrizes;

    // Constructor for PrizeManager
    public PrizeManager() {
        // Create a new list for prizes won
        prizesList = new ArrayList<>();

        // Create a new list for available prizes
        availablePrizes = new ArrayList<>();

        // Add all the vacation prizes to the available list
        availablePrizes.add("cruise");
        availablePrizes.add("gas");
        availablePrizes.add("spa");
    }

    // Add a prize when the player wins one
    public void addPrize(String prizeName) {
        // Check if the prize is available
        if (availablePrizes.contains(prizeName)) {
            // Add it to the won prizes
            prizesList.add(prizeName);

            // Remove it from available prizes (can't win it twice)
            availablePrizes.remove(prizeName);
        }
    }

    // Get all prizes that have been won
    public List<String> getPrizesList() {
        // Return a copy of the list
        return new ArrayList<>(prizesList);
    }

    // Get all prizes that are still available to win
    public List<String> getAvailablePrizes() {
        // Return a copy of the list
        return new ArrayList<>(availablePrizes);
    }

    // Get the image file name for a prize
    public String getPrizeImageFileName(String prizeName) {
        // Add .png extension to the prize name
        return prizeName + ".png";
    }

    // Check if a specific prize has been won
    public boolean hasPrize(String prizeName) {
        // Return true if the prize is in the won list
        return prizesList.contains(prizeName);
    }

    // Get how many prizes have been won
    public int getPrizeCount() {
        // Return the size of the won prizes list
        return prizesList.size();
    }

    // Reset all prizes (start a new game)
    public void resetPrizes() {
        // Clear the won prizes
        prizesList.clear();

        // Clear the available prizes
        availablePrizes.clear();

        // Add all prizes back to the available list
        availablePrizes.add("cruise");
        availablePrizes.add("gas");
        availablePrizes.add("spa");
    }
}









