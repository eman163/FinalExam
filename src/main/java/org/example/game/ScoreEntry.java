package org.example.game;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScoreEntry {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final String playerName;
    private final int score;
    private final String theme;
    private final LocalDateTime timestamp;

    // Required element: overloaded constructor
    public ScoreEntry(String playerName, int score, String theme) {
        this(playerName, score, theme, LocalDateTime.now());
    }

    // Required element: overloaded constructor
    public ScoreEntry(String playerName, int score, String theme, LocalDateTime timestamp) {
        this.playerName = sanitize(playerName, "Player");
        this.score = Math.max(score, 0);
        this.theme = sanitize(theme, "Mixed Game");
        this.timestamp = timestamp == null ? LocalDateTime.now() : timestamp;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public String getTheme() {
        return theme;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String toFileLine() {
        return playerName + "|" + score + "|" + theme + "|" + timestamp.format(FORMATTER);
    }

    public String toDisplayLine() {
        return String.format("%-14s %4d pts  %-14s %s",
                playerName,
                score,
                theme,
                timestamp.format(FORMATTER));
    }

    // Beginner-friendly file reader helper: return null if the line is invalid
    public static ScoreEntry fromFileLine(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }

        String[] pieces = line.split("\\|", -1);
        if (pieces.length != 4) {
            return null;
        }

        try {
            LocalDateTime timestamp = LocalDateTime.parse(pieces[3], FORMATTER);
            return new ScoreEntry(pieces[0], Integer.parseInt(pieces[1]), pieces[2], timestamp);
        } catch (Exception ignored) {
            return null;
        }
    }

    private String sanitize(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value.trim().replace('|', '/');
    }
}

