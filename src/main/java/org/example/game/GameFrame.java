package org.example.game;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;

public class GameFrame extends JFrame {
    private static final int ROUND_SECONDS = 15;
    private static final Color GOOD_COLOR = new Color(34, 139, 34);
    private static final Color BAD_COLOR = new Color(178, 34, 34);
    private static final Color INFO_COLOR = new Color(30, 70, 130);

    private final GameSession session = new GameSession();
    private final ScoreManager scoreManager = new ScoreManager();
    private final AudioManager audioManager = new AudioManager();
    private final PrizeManager prizeManager = new PrizeManager();
    private AnimationPanel animationPanel;

    // Required element: Swing elements - JButton, JLabel, JTextField, JComboBox, JOptionPane, JScrollPane, JProgressBar
    private final JTextField nameField = new JTextField(12);
    private final JComboBox<String> themeCombo = new JComboBox<>(new String[]{"Mixed Game", "Space", "Food", "Animals"});
    private final JButton startButton = new JButton("Start / Restart Game");
    private final JLabel scoreLabel = new JLabel("Score: 0");
    private final JLabel livesLabel = new JLabel("Lives: 3");
    private final JLabel roundLabel = new JLabel("Round: 0 / 0");
    private final JLabel themeLabel = new JLabel("Theme: None");
    private final JLabel roundTitleLabel = new JLabel("Word Sprint Game", SwingConstants.CENTER);
    private final JTextArea promptArea = new JTextArea(6, 30);
    private final JLabel assistLabel = new JLabel("Choose a theme and press Start.");
    private final JTextField guessField = new JTextField(16);
    private final JButton submitButton = new JButton("Submit Guess");
    private final JButton hintButton = new JButton("Use Hint");
    private final JButton skipButton = new JButton("Skip Round");
    private final JProgressBar timerBar = new JProgressBar(0, ROUND_SECONDS * 10);
    private final JLabel timerLabel = new JLabel("Time: 15.0");
    private final JLabel statusLabel = new JLabel("Enter your name, choose a theme, and start the show.");
    private final JTextArea leaderboardArea = new JTextArea(14, 26);
    private final JTextArea prizesArea = new JTextArea(8, 22);

    private Timer countdownTimer;
    private Timer flashTimer;
    private Timer advanceTimer;
    private Timer prizeRevealTimer;
    private int decisecondsRemaining = ROUND_SECONDS * 10;
    private boolean instructionsShown;

    public GameFrame() {
        super("Word Sprint Game");
        buildInterface();
        wireActions();
        refreshLeaderboard();
        updateRoundControls(false);
    }

    private void buildInterface() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 700));

        // Required element: use a Layout Manager with a JFrame
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        JPanel setupPanel = new JPanel(new GridBagLayout());
        setupPanel.setBorder(BorderFactory.createTitledBorder("Player Setup"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        setupPanel.add(new JLabel("Player Name:"), gbc);

        gbc.gridx = 1;
        nameField.setToolTipText("Enter the name that will appear on the high score board.");
        setupPanel.add(nameField, gbc);

        gbc.gridx = 2;
        setupPanel.add(new JLabel("Theme:"), gbc);

        gbc.gridx = 3;
        themeCombo.setToolTipText("Choose a topic or use Mixed Game for a variety of puzzles.");
        setupPanel.add(themeCombo, gbc);

        gbc.gridx = 4;
        startButton.setToolTipText("Starts a new game and resets your score and lives.");
        setupPanel.add(startButton, gbc);

        root.add(setupPanel, BorderLayout.NORTH);

        JPanel gamePanel = new JPanel(new BorderLayout(10, 10));

        JPanel hudPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        hudPanel.setBorder(BorderFactory.createTitledBorder("Game Status"));
        hudPanel.add(scoreLabel);
        hudPanel.add(livesLabel);
        hudPanel.add(roundLabel);
        hudPanel.add(themeLabel);
        gamePanel.add(hudPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(8, 8));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Puzzle Board"));
        roundTitleLabel.setFont(roundTitleLabel.getFont().deriveFont(Font.BOLD, 22f));
        centerPanel.add(roundTitleLabel, BorderLayout.NORTH);

        // Required element: animation with AnimationPanel
        animationPanel = new AnimationPanel();
        centerPanel.add(animationPanel, BorderLayout.WEST);

        promptArea.setEditable(false);
        promptArea.setLineWrap(true);
        promptArea.setWrapStyleWord(true);
        promptArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        promptArea.setBackground(new Color(248, 248, 255));
        promptArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        centerPanel.add(promptArea, BorderLayout.CENTER);

        assistLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(assistLabel, BorderLayout.SOUTH);
        gamePanel.add(centerPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new GridBagLayout());
        actionPanel.setBorder(BorderFactory.createTitledBorder("Controls"));
        GridBagConstraints actionGbc = new GridBagConstraints();
        actionGbc.insets = new Insets(6, 6, 6, 6);
        actionGbc.fill = GridBagConstraints.HORIZONTAL;

        actionGbc.gridx = 0;
        actionGbc.gridy = 0;
        actionPanel.add(new JLabel("Your Guess:"), actionGbc);

        actionGbc.gridx = 1;
        actionGbc.weightx = 1.0;
        guessField.setToolTipText("Type the word you think is correct and press Enter or Submit Guess.");
        actionPanel.add(guessField, actionGbc);

        actionGbc.weightx = 0;
        actionGbc.gridx = 2;
        actionPanel.add(submitButton, actionGbc);

        actionGbc.gridx = 0;
        actionGbc.gridy = 1;
        actionGbc.gridwidth = 3;
        timerBar.setStringPainted(false);
        actionPanel.add(timerBar, actionGbc);

        actionGbc.gridy = 2;
        JPanel lowerButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        lowerButtons.add(timerLabel);
        lowerButtons.add(hintButton);
        lowerButtons.add(skipButton);
        actionPanel.add(lowerButtons, actionGbc);

        actionGbc.gridy = 3;
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        actionPanel.add(statusLabel, actionGbc);

        gamePanel.add(actionPanel, BorderLayout.SOUTH);
        root.add(gamePanel, BorderLayout.CENTER);

        JPanel sidebar = new JPanel(new BorderLayout(8, 8));

        JPanel prizesPanel = new JPanel(new BorderLayout(8, 8));
        prizesPanel.setBorder(BorderFactory.createTitledBorder("Prizes Won"));
        prizesArea.setEditable(false);
        prizesArea.setText("No prizes won yet.");
        prizesArea.setLineWrap(true);
        prizesArea.setWrapStyleWord(true);
        prizesArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        JScrollPane prizesScrollPane = new JScrollPane(prizesArea);
        prizesPanel.add(prizesScrollPane, BorderLayout.CENTER);
        sidebar.add(prizesPanel, BorderLayout.NORTH);

        JPanel leaderboardPanel = new JPanel(new BorderLayout(8, 8));
        leaderboardPanel.setBorder(BorderFactory.createTitledBorder("High Scores"));
        leaderboardArea.setEditable(false);
        leaderboardArea.setLineWrap(true);
        leaderboardArea.setWrapStyleWord(true);
        leaderboardArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane leaderboardScrollPane = new JScrollPane(leaderboardArea);
        leaderboardPanel.add(leaderboardScrollPane, BorderLayout.CENTER);
        sidebar.add(leaderboardPanel, BorderLayout.CENTER);

        root.add(sidebar, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void wireActions() {
        startButton.addActionListener(event -> startGame());
        submitButton.addActionListener(event -> handleSubmit());
        hintButton.addActionListener(event -> handleHint());
        skipButton.addActionListener(event -> handleSkip());
        guessField.addActionListener(event -> handleSubmit());
    }

    private void startGame() {
        stopTimers();
        prizeManager.resetPrizes();
        refreshPrizesDisplay();
        session.startNewGame(nameField.getText(), (String) themeCombo.getSelectedItem());
        updateHud();
        loadRound();
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setText("Game started. Solve the word before the timer reaches zero.");
        audioManager.playSound("jeopardy.wav");

        if (!instructionsShown) {
            instructionsShown = true;
            JOptionPane.showMessageDialog(
                    this,
                    "Welcome to Word Sprint Game!\n\n"
                            + "1. Enter a name and pick a theme.\n"
                            + "2. Solve each puzzle before time runs out.\n"
                            + "3. A wrong answer, skip, or timeout costs one life.\n"
                            + "4. Hints are allowed, but they reduce your points.\n"
                            + "5. Finish the show with as many points as you can!\n"
                            + "6. Win prizes for correct answers!",
                    "How to Play",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void loadRound() {
        PuzzleRound round = session.getCurrentRound();
        if (round == null) {
            finishGame();
            return;
        }

        roundTitleLabel.setText(round.getRoundTitle());
        promptArea.setText(round.buildPromptText());

        if (round instanceof Hintable) {
            Hintable hintableRound = (Hintable) round;
            assistLabel.setText(hintableRound.getAssistText());
        } else {
            assistLabel.setText("Solve the puzzle.");
        }

        guessField.setText("");
        updateHud();
        updateRoundControls(true);
        hintButton.setEnabled(session.canUseHint());
        startRoundTimer();
        guessField.requestFocusInWindow();
    }

    // Required element: animation using a Swing Timer to animate the countdown bar and status flashes
    private void startRoundTimer() {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }

        decisecondsRemaining = ROUND_SECONDS * 10;
        timerBar.setMaximum(decisecondsRemaining);
        timerBar.setValue(decisecondsRemaining);
        timerBar.setForeground(GOOD_COLOR);
        timerLabel.setText(String.format("Time: %.1f", decisecondsRemaining / 10.0));

        countdownTimer = new Timer(100, event -> {
            decisecondsRemaining--;
            timerBar.setValue(Math.max(decisecondsRemaining, 0));
            timerLabel.setText(String.format("Time: %.1f", Math.max(decisecondsRemaining, 0) / 10.0));
            updateTimerBarColor();

            if (decisecondsRemaining <= 0) {
                countdownTimer.stop();
                handleTimeout();
            }
        });
        countdownTimer.start();
    }

    private void updateTimerBarColor() {
        if (decisecondsRemaining > 80) {
            timerBar.setForeground(GOOD_COLOR);
        } else if (decisecondsRemaining > 40) {
            timerBar.setForeground(new Color(218, 165, 32));
        } else {
            timerBar.setForeground(BAD_COLOR);
        }
    }

    private void handleSubmit() {
        if (session.getCurrentRound() == null) {
            return;
        }

        try {
            TurnResult result = session.submitGuess(guessField.getText(), Math.max(decisecondsRemaining, 0) / 10);
            resolveRound(result);
        } catch (InvalidGuessException exception) {
            // Required element: custom exception is caught and handled without the game crashing
            statusLabel.setText(exception.getMessage());
            flashStatus(new Color(205, 133, 63));
            JOptionPane.showMessageDialog(this, exception.getMessage(), "Invalid Guess", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleHint() {
        String hint = session.useHint();
        statusLabel.setText("Hint: " + hint);
        flashStatus(INFO_COLOR);
        hintButton.setEnabled(session.canUseHint());
    }

    private void handleSkip() {
        resolveRound(session.forfeitCurrentRound("Round skipped."));
    }

    private void handleTimeout() {
        resolveRound(session.forfeitCurrentRound("Time's up."));
    }

    private void resolveRound(TurnResult result) {
        // Stop the countdown timer
        stopCountdown();

        // Disable the guess input controls
        updateRoundControls(false);

        // Create a message based on whether the answer was correct
        String message;

        if (result.isCorrect()) {
            // Player got it right!
            message = result.getMessage() + " +" + result.getPointsEarned() + " points!";

            // Show the check mark image
            animationPanel.showImage("Check mark.jpg");

            // Play the victory sound
            audioManager.playSound("jeopardy.wav");

            // Award a random prize from the available prizes
            List<String> availablePrizes = prizeManager.getAvailablePrizes();
            String prizeImageFileName = null;

            if (!availablePrizes.isEmpty()) {
                // Pick a random prize
                int randomIndex = (int)(Math.random() * availablePrizes.size());
                String prize = availablePrizes.get(randomIndex);

                // Add it to the won prizes
                prizeManager.addPrize(prize);

                // Update the prize list shown on the screen
                refreshPrizesDisplay();

                // Get the image file name from the prize name
                prizeImageFileName = prizeManager.getPrizeImageFileName(prize);

                // Tell the player which prize they won
                message = result.getMessage() + " +" + result.getPointsEarned() + " points! You won a " + formatPrizeName(prize) + "!";

                // Show the prize image after the check mark appears
                if (prizeRevealTimer != null) {
                    prizeRevealTimer.stop();
                }

                String finalPrizeImageFileName = prizeImageFileName;
                String prizeCaption = formatPrizeName(prize);
                prizeRevealTimer = new Timer(650, event -> animationPanel.showImage(finalPrizeImageFileName, prizeCaption));
                prizeRevealTimer.setRepeats(false);
                prizeRevealTimer.start();
            }
        } else {
            // Player got it wrong
            message = result.getMessage() + " The answer was " + result.getCorrectAnswer() + ".";

            // Show the X mark image
            animationPanel.showImage("X mark.jpg");
        }

        // Display the message to the player
        statusLabel.setText(message);

        // Flash the status label with appropriate color
        if (result.isCorrect()) {
            // Flash green for correct
            flashStatus(GOOD_COLOR);
        } else {
            // Flash red for incorrect
            flashStatus(BAD_COLOR);
        }

        // Update the score display
        updateHud();

        // Start a timer to animate the feedback image
        Timer animationUpdateTimer = new Timer(50, event -> {
            // Update the animation frame
            animationPanel.updateAnimation();

            // If animation is done, stop this timer
            if (!animationPanel.isAnimating()) {
                Timer timer = (Timer) event.getSource();
                timer.stop();
            }
        });
        animationUpdateTimer.start();

        // Stop any previous advance timer
        if (advanceTimer != null) {
            advanceTimer.stop();
        }

        // Create a timer to move to the next round after showing the result
        advanceTimer = new Timer(1400, event -> {
            // Stop this timer
            advanceTimer.stop();

            // Clear the feedback image
            animationPanel.clearImage();

            // Check if the player is out of lives
            if (session.getLives() <= 0) {
                // Game over - end the game
                finishGame();
                return;
            }

            // Move to the next round
            session.advanceToNextRound();

            // Check if there are more rounds
            if (session.getCurrentRound() == null) {
                // No more rounds - end the game
                finishGame();
            } else {
                // Load the next round
                loadRound();
            }
        });
        advanceTimer.setRepeats(false);
        advanceTimer.start();
    }

    private void finishGame() {
        stopTimers();
        updateRoundControls(false);

        if (!session.hasStarted()) {
            return;
        }

        String summary = session.getLives() > 0
                ? "Show complete!"
                : "Game over! You ran out of lives.";

        boolean saved = scoreManager.recordScore(new ScoreEntry(session.getPlayerName(), session.getScore(), session.getTheme()));
        refreshLeaderboard();
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setText(summary + " Final score: " + session.getScore());

        JOptionPane.showMessageDialog(
                this,
                summary + "\nFinal score: " + session.getScore()
                        + (saved ? "\nHigh score saved." : "\nHigh score could not be saved."),
                "Game Finished",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void refreshLeaderboard() {
        StringBuilder builder = new StringBuilder();
        builder.append("Top 10 Scores\n");
        builder.append("------------------------------------------\n");

        int placement = 1;
        for (ScoreEntry scoreEntry : scoreManager.loadScores()) {
            builder.append(String.format("%2d. %s%n", placement++, scoreEntry.toDisplayLine()));
        }

        if (placement == 1) {
            builder.append("No scores yet. Be the first to finish a show!\n");
        }

        leaderboardArea.setText(builder.toString());
        leaderboardArea.setCaretPosition(0);
    }

    private void updateHud() {
        scoreLabel.setText("Score: " + session.getScore());
        livesLabel.setText("Lives: " + session.getLives());
        roundLabel.setText("Round: " + session.getRoundNumber() + " / " + session.getTotalRounds());
        themeLabel.setText("Theme: " + session.getTheme());
    }

    private void updateRoundControls(boolean enabled) {
        guessField.setEnabled(enabled);
        submitButton.setEnabled(enabled);
        hintButton.setEnabled(enabled && session.canUseHint());
        skipButton.setEnabled(enabled);
    }

    private void flashStatus(Color flashColor) {
        if (flashTimer != null) {
            flashTimer.stop();
        }

        Color baseColor = statusLabel.getForeground();
        final int[] count = {0};
        flashTimer = new Timer(120, event -> {
            if (count[0] >= 6) {
                flashTimer.stop();
                statusLabel.setForeground(Color.BLACK);
                return;
            }

            statusLabel.setForeground(count[0] % 2 == 0 ? flashColor : baseColor);
            count[0]++;
        });
        flashTimer.start();
    }

    private void stopCountdown() {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
    }

    private void stopTimers() {
        stopCountdown();

        if (flashTimer != null) {
            flashTimer.stop();
        }

        if (advanceTimer != null) {
            advanceTimer.stop();
        }

        if (prizeRevealTimer != null) {
            prizeRevealTimer.stop();
        }
    }

    private void refreshPrizesDisplay() {
        List<String> prizesWon = prizeManager.getPrizesList();
        StringBuilder builder = new StringBuilder();

        if (prizesWon.isEmpty()) {
            builder.append("No prizes won yet.");
        } else {
            for (String prize : prizesWon) {
                builder.append(formatPrizeName(prize));
                builder.append(" (image: ");
                builder.append(prizeManager.getPrizeImageFileName(prize));
                builder.append(")");
                builder.append(System.lineSeparator());
            }
        }

        prizesArea.setText(builder.toString());
        prizesArea.setCaretPosition(0);
    }

    private String formatPrizeName(String prizeName) {
        if (prizeName == null || prizeName.isEmpty()) {
            return "Prize";
        }

        return Character.toUpperCase(prizeName.charAt(0)) + prizeName.substring(1);
    }
}


