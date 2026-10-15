# Word Sprint Game

`Word Sprint Game` is a Java Swing word game inspired by classroom word-game projects. The player enters a name, chooses a theme, and solves a series of timed word rounds before running out of lives. The game features audio playback, visual animations, and a prize system.

## How the game works

1. Enter your player name.
2. Choose a theme: `Mixed Game`, `Space`, `Food`, or `Animals`.
3. Press **Start / Restart Game**.
4. Each round shows either:
   - an **Unscramble Round**, or
   - a **Clue Round**.
5. Type your answer and press **Submit Guess**.
6. You may use **one hint per round**, but hints reduce the points you can earn.
7. A wrong answer, a skipped round, or a timeout costs one life.
8. When you answer correctly, you win a prize from the available prize pool (Cruise, Gas card, or Spa).
9. The game ends when you finish all rounds or lose all lives.
10. High scores are saved to `high_scores.txt`.

## Features included for the project requirements

- GUI application built with Swing
- Multiple logical Java classes
- Overloaded constructors
- Parent class with multiple child classes
- Overridden methods
- Interface implemented by multiple classes
- Custom exception that is thrown, caught, and handled
- File read/write for persistent high scores
- `JFrame` with layout managers
- Multiple Swing widgets including `JButton`, `JLabel`, `JTextField`, `JComboBox`, `JOptionPane`, `JScrollPane`, and `JProgressBar`
- **Audio playback** of "jeopardy.wav" sound comes from https://freesound.org/people/1000kcirtap/sounds/554635
- **Animation** showing check mark (correct) and X mark (incorrect)
- **Prize images:**
    - cruise - https://pixabay.com//?utm_source=link-attribution&utm_medium=referral&utm_campaign=image&utm_content=4460493
    - gas - https://pixabay.com/photos/fuel-gas-station-refueling-gas-6999650
    - spa - https://pixabay.com/illustrations/ai-generated-spa-massage-meditation-8970246
- **Prize system** that awards random prizes when you answer correctly
- Queue functionality using `LinkedList`

## Audio and Animations

This project integrates resources from Lesson 10:

- **Audio:** `jeopardy.wav` plays when the game starts and when you answer correctly
- **Images:** 
  - `Check mark.jpg` displays when you answer correctly (animated fade-out)
  - `X mark.jpg` displays when you answer incorrectly (animated fade-out)
  - Prize images (cruise.png, gas.png, spa.png) are available in the resources folder
- **Animation:** Uses a custom `AnimationPanel` class to fade out feedback images over time

## Resource files

All images and audio are located in `src/main/resources/`:
- `images/`: Contains all prize and feedback images
- `sounds/`: Contains the jeopardy.wav audio file

## Where the required commented elements are marked

The required code comments are placed directly in the source files so the instructor can quickly spot them:

- `src/main/java/org/example/game/PuzzleRound.java`
- `src/main/java/org/example/game/Hintable.java`
- `src/main/java/org/example/game/InvalidGuessException.java`
- `src/main/java/org/example/game/RoundFactory.java`
- `src/main/java/org/example/game/ScoreManager.java`
- `src/main/java/org/example/game/ScoreEntry.java`
- `src/main/java/org/example/game/GameSession.java`
- `src/main/java/org/example/game/GameFrame.java`
- `src/main/java/org/example/game/UnscrambleRound.java`
- `src/main/java/org/example/game/AnimationPanel.java`
- `src/main/java/org/example/game/AudioManager.java`
- `src/main/java/org/example/game/PrizeManager.java`

## Build and run

### Compile the project

```
mvn test-compile
```

### Run the self-checks used for verification

```
java -cp "target/classes;target/test-classes" org.example.game.GameSessionTest
java -cp "target/classes;target/test-classes" org.example.game.ScoreManagerTest
```

### Start the game

```
java -cp target/classes org.example.Main
```

