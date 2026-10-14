package org.example.game;

public class CategoryRound extends PuzzleRound implements Hintable {
    private final String clue;

    public CategoryRound(String answer, String clue) {
        this(answer, clue, 100);
    }

    public CategoryRound(String answer, String clue, int basePoints) {
        super(answer, "Solve the clue to find the hidden word.", basePoints);
        this.clue = clue;
    }

    @Override
    public String getRoundTitle() {
        return "Clue Round";
    }

    @Override
    public String buildPromptText() {
        return getPrompt() + "\n\nClue: " + clue;
    }

    @Override
    public String getHint() {
        return "The answer begins with '" + getAnswer().charAt(0) + "' and has " + getAnswer().length()
                + " letters.";
    }

    @Override
    public String getAssistText() {
        return "Focus on the clue and think of the best single-word answer.";
    }
}
