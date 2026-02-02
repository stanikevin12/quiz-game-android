package com.example.quizgame.data;

import java.util.Arrays;
import java.util.List;

import com.example.quizgame.model.Question;

public final class QuestionBank {
    private QuestionBank() {
    }

    public static List<Question> getEasyQuestions() {
        return Arrays.asList(
                new Question("What color is the sky on a clear day?", "Blue"),
                new Question("How many days are in a week?", "7"),
                new Question("What is 2 + 2?", "4")
        );
    }

    public static List<Question> getMediumQuestions() {
        return Arrays.asList(
                new Question("What planet is known as the Red Planet?", "Mars"),
                new Question("What is the boiling point of water in Celsius?", "100"),
                new Question("Who wrote 'Romeo and Juliet'?", "Shakespeare")
        );
    }

    public static List<Question> getHardQuestions() {
        return Arrays.asList(
                new Question("What is the square root of 144?", "12"),
                new Question("What gas do plants absorb from the atmosphere?", "Carbon dioxide"),
                new Question("In what year did the first moon landing occur?", "1969")
        );
    }
}
