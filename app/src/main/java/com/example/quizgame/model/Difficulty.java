package com.example.quizgame.model;

public enum Difficulty {
    EASY(1, "Easy"),
    MEDIUM(2, "Medium"),
    HARD(3, "Hard");

    private final int points;
    private final String label;

    Difficulty(int points, String label) {
        this.points = points;
        this.label = label;
    }

    public int getPoints() {
        return points;
    }

    public String getLabel() {
        return label;
    }
}
