package org.example.game;

import java.util.Queue;

public class GameSession {
    private String playerName = "Player";
    private String theme = "Mixed Showdown";
    private Queue<PuzzleRound> upcomingRounds;
    private PuzzleRound currentRound;
    private int score;
    private int lives;
    private int roundNumber;
    private int totalRounds;
    private boolean hintUsedThisRound;
    private boolean gameStarted;

    // Starts a completely new game and resets all game values
    public void startNewGame(String playerName, String theme) {
        if (playerName == null || playerName.isBlank()) {
            this.playerName = "Player";
        } else {
            this.playerName = playerName.trim();
        }

        if (theme == null || theme.isBlank()) {
            this.theme = "Mixed Showdown";
        } else {
            this.theme = theme.trim();
        }

        this.upcomingRounds = RoundFactory.createRounds(this.theme);
        this.totalRounds = upcomingRounds.size();
        this.score = 0;
        this.lives = 3;
        this.roundNumber = 0;
        this.hintUsedThisRound = false;
        this.gameStarted = true;
        advanceToNextRound();
    }

    public PuzzleRound getCurrentRound() {
        return currentRound;
    }

    public boolean canUseHint() {
        return currentRound instanceof Hintable && !hintUsedThisRound;
    }

    public String useHint() {
        if (!(currentRound instanceof Hintable)) {
            return "No hint is available for this round.";
        }

        if (hintUsedThisRound) {
            return "You already used the hint for this round.";
        }

        Hintable hintableRound = (Hintable) currentRound;
        hintUsedThisRound = true;
        return hintableRound.getHint();
    }

    // Required element: method that throws the custom exception
    public TurnResult submitGuess(String guess, int secondsLeft) throws InvalidGuessException {
        validateGuess(guess);

        if (currentRound == null) {
            return new TurnResult(false, "There is no active round.", "", 0);
        }

        if (currentRound.matches(guess)) {
            int points = currentRound.calculatePoints(secondsLeft, hintUsedThisRound);
            score += points;
            return new TurnResult(true, "Correct!", currentRound.getAnswer(), points);
        }

        lives = Math.max(0, lives - 1);
        return new TurnResult(false, "Incorrect.", currentRound.getAnswer(), 0);
    }

    public TurnResult forfeitCurrentRound(String message) {
        if (currentRound == null) {
            return new TurnResult(false, "There is no active round.", "", 0);
        }

        lives = Math.max(0, lives - 1);
        return new TurnResult(false, message, currentRound.getAnswer(), 0);
    }

    public void advanceToNextRound() {
        hintUsedThisRound = false;

        if (upcomingRounds == null) {
            currentRound = null;
        } else {
            currentRound = upcomingRounds.poll();
        }

        if (currentRound != null) {
            roundNumber++;
        }
    }

    public boolean isGameOver() {
        return gameStarted && (lives <= 0 || currentRound == null);
    }

    public boolean hasStarted() {
        return gameStarted;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public int getRoundsRemaining() {
        if (upcomingRounds == null) {
            return 0;
        }

        return upcomingRounds.size();
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getTheme() {
        return theme;
    }

    private void validateGuess(String guess) throws InvalidGuessException {
        if (guess == null || guess.isBlank()) {
            throw new InvalidGuessException("Please enter a guess before pressing Submit.");
        }

        String trimmedGuess = guess.trim();
        if (!trimmedGuess.matches("[A-Za-z ]+")) {
            throw new InvalidGuessException("Use letters and spaces only for your guess.");
        }

        if (trimmedGuess.length() < 2) {
            throw new InvalidGuessException("Your guess must contain at least two letters.");
        }
    }
}

