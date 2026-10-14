package org.example;

import org.example.game.GameFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameFrame::new);
    }
}