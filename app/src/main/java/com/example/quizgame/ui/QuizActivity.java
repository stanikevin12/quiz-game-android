package com.example.quizgame.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizgame.R;

public class QuizActivity extends AppCompatActivity {
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        transitionLocked = false;
        enableInputs(true);
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
        enableInputs(false);
        questionText.setText("Time is up. Tap Next to continue.");
    }

    private void enableInputs(boolean enabled) {
        answerInput.setEnabled(enabled);
        submitButton.setEnabled(enabled);
        nextButton.setEnabled(enabled);
    }
}
