package org.example.game;

// Required element: parent class that has 2 or more child classes that extend it
public abstract class PuzzleRound {
    private final String answer;
    private final String prompt;
    private final int basePoints;

    // Required element: overloaded constructor
    protected PuzzleRound(String answer, String prompt) {
        this(answer, prompt, 100);
    }

    // Required element: overloaded constructor
    protected PuzzleRound(String answer, String prompt, int basePoints) {
        this.answer = normalize(answer);
        this.prompt = prompt;
        this.basePoints = Math.max(basePoints, 25);
    }

    public boolean matches(String guess) {
        return normalize(guess).equals(answer);
    }

    public int calculatePoints(int secondsLeft, boolean usedHint) {
        int hintPenalty = usedHint ? 15 : 0;
        int timeBonus = Math.max(secondsLeft, 0) * 4;
        return Math.max(25, basePoints + timeBonus - hintPenalty);
    }

    protected String normalize(String text) {
        if (text == null) {
            return "";
        }

        return text.trim()
                .replaceAll("\\s+", " ")
                .toUpperCase();
    }

    public String getAnswer() {
        return answer;
    }

    public String getPrompt() {
        return prompt;
    }

    public int getBasePoints() {
        return basePoints;
    }

    public abstract String getRoundTitle();

    public abstract String buildPromptText();
}
