package com.example.quizgame.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizgame.R;
import com.example.quizgame.logic.QuizEngine;
import com.example.quizgame.model.Difficulty;
import com.example.quizgame.model.Question;
import com.example.quizgame.ui.ResultsActivity;

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_DIFFICULTY = "com.example.quizgame.extra.DIFFICULTY";
    private static final long TOTAL_TIME_MS = 10_000L;
    private static final long INTERVAL_MS = 1_000L;
    private TextView difficultyLabel;
    private TextView questionText;
    private TextView timerText;
    private TextView mistakesText;
    private EditText answerInput;
    private Button submitButton;
    private Button nextButton;
    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private boolean transitionLocked;
    private QuizEngine quizEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        difficultyLabel = findViewById(R.id.difficulty_label);
        questionText = findViewById(R.id.question_text);
        timerText = findViewById(R.id.timer_text);
        mistakesText = findViewById(R.id.mistakes_text);
        answerInput = findViewById(R.id.answer_input);
        submitButton = findViewById(R.id.submit_button);
        nextButton = findViewById(R.id.next_button);

        quizEngine = new QuizEngine(resolveDifficulty());
        nextButton.setEnabled(false);
        updateQuestionDisplay();

        submitButton.setOnClickListener(view -> handleSubmit());
        nextButton.setOnClickListener(view -> handleNext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        transitionLocked = false;
        enableInputs(true);
        nextButton.setEnabled(false);
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelTimer();
    }

    @Override
    protected void onDestroy() {
        cancelTimer();
        super.onDestroy();
    }

    private void startTimer() {
        cancelTimer();
        timerText.setText("10");
        transitionLocked = false;
        countDownTimer = new CountDownTimer(TOTAL_TIME_MS, INTERVAL_MS) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = Math.max(0L, millisUntilFinished / 1000L);
                timerText.setText(String.valueOf(secondsRemaining));
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                timerText.setText("0");
                handleTimeout();
                cancel();
            }
        };
        timerRunning = true;
        countDownTimer.start();
    }

    private void cancelTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        timerRunning = false;
    }

    private void handleTimeout() {
        if (transitionLocked) {
            return;
        }
        transitionLocked = true;
        quizEngine.onTimeout();
        updateMistakesDisplay();
        if (finishIfQuizEnded()) {
            return;
        }
        enableInputs(false);
        nextButton.setEnabled(true);
        questionText.setText("Time is up. Tap Next to continue.");
    }

    private void enableInputs(boolean enabled) {
        answerInput.setEnabled(enabled);
        submitButton.setEnabled(enabled);
    }

    private void updateQuestionDisplay() {
        Question question = quizEngine.getCurrentQuestion();
        if (question == null) {
            questionText.setText("No more questions available.");
            enableInputs(false);
            nextButton.setEnabled(false);
            return;
        }
        difficultyLabel.setText("Difficulty: " + quizEngine.getCurrentDifficulty().getLabel());
        questionText.setText(question.getText());
        updateMistakesDisplay();
    }

    private void updateMistakesDisplay() {
        mistakesText.setText("Mistakes: " + quizEngine.getMistakes() + " / 3");
    }

    private Difficulty resolveDifficulty() {
        String difficultyName = getIntent().getStringExtra(EXTRA_DIFFICULTY);
        if (TextUtils.isEmpty(difficultyName)) {
            return Difficulty.EASY;
        }
        try {
            return Difficulty.valueOf(difficultyName);
        } catch (IllegalArgumentException ex) {
            return Difficulty.EASY;
        }
    }

    private void resetForNextQuestion() {
        answerInput.setText("");
        enableInputs(true);
        nextButton.setEnabled(false);
        updateQuestionDisplay();
        startTimer();
    }

    private void handleSubmit() {
        if (transitionLocked) {
            return;
        }
        String answer = answerInput.getText().toString().trim();
        if (TextUtils.isEmpty(answer)) {
            answerInput.setError("Please enter an answer.");
            return;
        }
        transitionLocked = true;
        cancelTimer();
        boolean correct = quizEngine.checkAnswer(answer);
        updateMistakesDisplay();
        if (finishIfQuizEnded()) {
            return;
        }
        enableInputs(false);
        nextButton.setEnabled(true);
        if (correct) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrect. Try the next one.", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleNext() {
        cancelTimer();
        if (finishIfQuizEnded()) {
            return;
        }
        if (!quizEngine.advance()) {
            enableInputs(false);
            nextButton.setEnabled(false);
            questionText.setText("Quiz complete.");
            return;
        }
        resetForNextQuestion();
    }

    private boolean finishIfQuizEnded() {
        if (!quizEngine.shouldEnd()) {
            return false;
        }
        cancelTimer();
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra(ResultsActivity.EXTRA_FINAL_SCORE, quizEngine.getScore());
        startActivity(intent);
        finish();
        return true;
    }
}
