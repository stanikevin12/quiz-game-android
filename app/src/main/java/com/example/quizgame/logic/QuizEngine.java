package com.example.quizgame.logic;

import java.util.List;

import com.example.quizgame.data.QuestionBank;
import com.example.quizgame.model.Difficulty;
import com.example.quizgame.model.Question;

public class QuizEngine {
    private static final int MAX_MISTAKES = 3;

    private Difficulty currentDifficulty;
    private List<Question> currentQuestions;
    private int questionIndex;
    private int score;
    private int mistakes;

    public QuizEngine() {
        this(Difficulty.EASY);
    }

    public QuizEngine(Difficulty startingDifficulty) {
        Difficulty resolved = startingDifficulty == null ? Difficulty.EASY : startingDifficulty;
        currentDifficulty = resolved;
        currentQuestions = getQuestionsForDifficulty(resolved);
        questionIndex = 0;
        score = 0;
        mistakes = 0;
    }

    public Question getCurrentQuestion() {
        if (currentQuestions.isEmpty() || questionIndex >= currentQuestions.size()) {
            return null;
        }
        return currentQuestions.get(questionIndex);
    }

    public boolean checkAnswer(String input) {
        Question question = getCurrentQuestion();
        if (question == null) {
            return false;
        }
        boolean correct = input != null && input.trim().equalsIgnoreCase(question.getAnswer());
        if (correct) {
            score += currentDifficulty.getPoints();
        } else {
            mistakes += 1;
        }
        return correct;
    }

    public void onTimeout() {
        mistakes += 1;
    }

    public boolean advance() {
        if (shouldEnd()) {
            return false;
        }
        questionIndex += 1;
        if (questionIndex < currentQuestions.size()) {
            return !shouldEnd();
        }
        if (currentDifficulty == Difficulty.EASY) {
            setDifficulty(Difficulty.MEDIUM);
            return true;
        }
        if (currentDifficulty == Difficulty.MEDIUM) {
            setDifficulty(Difficulty.HARD);
            return true;
        }
        return false;
    }

    public boolean shouldEnd() {
        return mistakes >= MAX_MISTAKES;
    }

    public int getScore() {
        return score;
    }

    public int getMistakes() {
        return mistakes;
    }

    public Difficulty getCurrentDifficulty() {
        return currentDifficulty;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }

    private void setDifficulty(Difficulty difficulty) {
        currentDifficulty = difficulty;
        questionIndex = 0;
        currentQuestions = getQuestionsForDifficulty(difficulty);
    }

    private List<Question> getQuestionsForDifficulty(Difficulty difficulty) {
        if (difficulty == Difficulty.MEDIUM) {
            return QuestionBank.getMediumQuestions();
        }
        if (difficulty == Difficulty.HARD) {
            return QuestionBank.getHardQuestions();
        }
        return QuestionBank.getEasyQuestions();
    }
}
