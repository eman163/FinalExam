package org.example.game;

// Beginner-friendly class used to store the result of one turn
public class TurnResult {
	private final boolean correct;
	private final String message;
	private final String correctAnswer;
	private final int pointsEarned;

	public TurnResult(boolean correct, String message, String correctAnswer, int pointsEarned) {
		this.correct = correct;
		this.message = message;
		this.correctAnswer = correctAnswer;
		this.pointsEarned = pointsEarned;
	}

	public boolean isCorrect() {
		return correct;
	}

	public String getMessage() {
		return message;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public int getPointsEarned() {
		return pointsEarned;
	}
}


