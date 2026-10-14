package org.example.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class UnscrambleRound extends PuzzleRound implements Hintable {
    private static final Random RANDOM = new Random();
    private final String scrambledWord;

    public UnscrambleRound(String answer) {
        this(answer, 100);
    }

    public UnscrambleRound(String answer, int basePoints) {
        super(answer, "Unscramble the letters to make a word.", basePoints);
        this.scrambledWord = scramble(answer);
    }

    private String scramble(String word) {
        List<Character> letters = new ArrayList<>();
        for (char letter : word.toUpperCase().toCharArray()) {
            letters.add(letter);
        }

        String shuffled = word.toUpperCase();
        int attempts = 0;
        while (shuffled.equals(word.toUpperCase()) && attempts < 8) {
            Collections.shuffle(letters, RANDOM);
            StringBuilder builder = new StringBuilder();
            for (char letter : letters) {
                builder.append(letter);
            }
            shuffled = builder.toString();
            attempts++;
        }

        return shuffled;
    }

    // Required element: overridden method
    @Override
    public String getRoundTitle() {
        return "Unscramble Round";
    }

    @Override
    public String buildPromptText() {
        return getPrompt() + "\n\nScrambled word: " + scrambledWord;
    }

    @Override
    public String getHint() {
        return "The answer starts with '" + getAnswer().charAt(0) + "' and ends with '"
                + getAnswer().charAt(getAnswer().length() - 1) + "'.";
    }

    @Override
    public String getAssistText() {
        return "Move letters around in your head and look for a familiar pattern.";
    }
}
