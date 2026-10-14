package org.example.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public final class RoundFactory {
    private RoundFactory() {
    }

    public static Queue<PuzzleRound> createRounds(String theme) {
        // Required element: incorporate a Queue using a LinkedList
        Queue<PuzzleRound> rounds = new LinkedList<>();
        List<PuzzleRound> choices = new ArrayList<>();
        String cleanTheme;

        if (theme == null) {
            cleanTheme = "MIXED SHOWDOWN";
        } else {
            cleanTheme = theme.trim().toUpperCase();
        }

        if (cleanTheme.equals("SPACE")) {
            choices.addAll(buildSpaceRounds());
        } else if (cleanTheme.equals("FOOD")) {
            choices.addAll(buildFoodRounds());
        } else if (cleanTheme.equals("ANIMALS")) {
            choices.addAll(buildAnimalRounds());
        } else {
            choices.addAll(buildSpaceRounds());
            choices.addAll(buildFoodRounds());
            choices.addAll(buildAnimalRounds());
        }

        Collections.shuffle(choices);
        int roundLimit = Math.min(6, choices.size());
        for (int index = 0; index < roundLimit; index++) {
            rounds.offer(choices.get(index));
        }
        return rounds;
    }

    private static List<PuzzleRound> buildSpaceRounds() {
        List<PuzzleRound> rounds = new ArrayList<>();
        rounds.add(new UnscrambleRound("orbit", 120));
        rounds.add(new UnscrambleRound("comet", 110));
        rounds.add(new UnscrambleRound("galaxy", 140));
        rounds.add(new CategoryRound("rocket", "A vehicle launched into space", 125));
        rounds.add(new CategoryRound("moon", "Earth's natural satellite", 100));
        rounds.add(new CategoryRound("mars", "The red planet", 105));
        return rounds;
    }

    private static List<PuzzleRound> buildFoodRounds() {
        List<PuzzleRound> rounds = new ArrayList<>();
        rounds.add(new UnscrambleRound("pasta", 110));
        rounds.add(new UnscrambleRound("pepper", 130));
        rounds.add(new UnscrambleRound("cookie", 135));
        rounds.add(new CategoryRound("banana", "A yellow fruit often found in lunch boxes", 115));
        rounds.add(new CategoryRound("soup", "A warm dish you eat with a spoon", 100));
        rounds.add(new CategoryRound("cheese", "Dairy food that can be sliced or shredded", 120));
        return rounds;
    }

    private static List<PuzzleRound> buildAnimalRounds() {
        List<PuzzleRound> rounds = new ArrayList<>();
        rounds.add(new UnscrambleRound("tiger", 120));
        rounds.add(new UnscrambleRound("rabbit", 130));
        rounds.add(new UnscrambleRound("dolphin", 145));
        rounds.add(new CategoryRound("zebra", "Striped animal from Africa", 115));
        rounds.add(new CategoryRound("eagle", "Bird known for sharp eyesight", 110));
        rounds.add(new CategoryRound("otter", "Playful animal that loves the water", 120));
        return rounds;
    }
}

