package org.example.game;

public class GameSessionTest {
    public static void main(String[] args) throws Exception {
        correctGuessAwardsPoints();
        invalidGuessThrowsCustomException();
        System.out.println("GameSessionTest passed.");
    }

    static void correctGuessAwardsPoints() throws InvalidGuessException {
        GameSession session = new GameSession();
        session.startNewGame("Alex", "Space");

        PuzzleRound firstRound = session.getCurrentRound();
        require(firstRound != null, "Expected a starting round.");

        TurnResult result = session.submitGuess(firstRound.getAnswer(), 10);

        require(result.isCorrect(), "Expected the answer to be marked correct.");
        require(result.getPointsEarned() >= 25, "Expected positive points.");
        require(result.getPointsEarned() == session.getScore(), "Expected score to match earned points.");
    }

    static void invalidGuessThrowsCustomException() {
        GameSession session = new GameSession();
        session.startNewGame("Alex", "Food");

        try {
            session.submitGuess("123", 10);
            throw new IllegalStateException("Expected InvalidGuessException for numeric input.");
        } catch (InvalidGuessException expected) {
            require(expected.getMessage().contains("letters"), "Expected a helpful validation message.");
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}


