package org.example.game;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class ScoreManagerTest {
    public static void main(String[] args) throws Exception {
        scoresAreSavedAndLoadedInDescendingOrder();
        System.out.println("ScoreManagerTest passed.");
    }

    static void scoresAreSavedAndLoadedInDescendingOrder() throws Exception {
        Path tempDir = Files.createTempDirectory("score-manager-test");
        ScoreManager manager = new ScoreManager(tempDir.resolve("scores.txt"));

        require(manager.recordScore(new ScoreEntry("Taylor", 150, "Space", LocalDateTime.of(2026, 10, 4, 10, 0))),
                "Expected first score to save.");
        require(manager.recordScore(new ScoreEntry("Jordan", 250, "Food", LocalDateTime.of(2026, 10, 4, 11, 0))),
                "Expected second score to save.");

        List<ScoreEntry> scores = manager.loadScores();

        require(scores.size() == 2, "Expected two stored scores.");
        require("Jordan".equals(scores.get(0).getPlayerName()), "Expected highest score first.");
        require(scores.get(0).getScore() == 250, "Expected highest score value first.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}

