package org.example.game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScoreManager {
    private static final int MAX_ENTRIES = 10;
    private final Path filePath;

    // Required element: overloaded constructor
    public ScoreManager() {
        this(Path.of("high_scores.txt"));
    }

    // Required element: overloaded constructor
    public ScoreManager(Path filePath) {
        this.filePath = filePath;
    }

    // Required element: read/write something to a File
    public List<ScoreEntry> loadScores() {
        List<ScoreEntry> scores = new ArrayList<>();

        if (Files.notExists(filePath)) {
            return scores;
        }

        try {
            for (String line : Files.readAllLines(filePath)) {
                ScoreEntry scoreEntry = ScoreEntry.fromFileLine(line);
                if (scoreEntry != null) {
                    scores.add(scoreEntry);
                }
            }
        } catch (IOException ignored) {
            return new ArrayList<>();
        }

        sortScores(scores);
        return scores;
    }

    public boolean recordScore(ScoreEntry entry) {
        List<ScoreEntry> scores = loadScores();
        scores.add(entry);
        sortScores(scores);

        if (scores.size() > MAX_ENTRIES) {
            scores = new ArrayList<>(scores.subList(0, MAX_ENTRIES));
        }

        return saveScores(scores);
    }

    private boolean saveScores(List<ScoreEntry> scores) {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            List<String> lines = new ArrayList<>();
            for (ScoreEntry score : scores) {
                lines.add(score.toFileLine());
            }

            Files.write(filePath,
                    lines,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }

    public Path getFilePath() {
        return filePath;
    }

    private void sortScores(List<ScoreEntry> scores) {
        Collections.sort(scores, new Comparator<ScoreEntry>() {
            @Override
            public int compare(ScoreEntry first, ScoreEntry second) {
                return Integer.compare(second.getScore(), first.getScore());
            }
        });
    }
}

