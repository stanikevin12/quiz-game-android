package com.example.quizgame.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizgame.R;
import com.example.quizgame.data.Prefs;

public class ResultsActivity extends AppCompatActivity {
    public static final String EXTRA_FINAL_SCORE = "com.example.quizgame.extra.FINAL_SCORE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_results);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView finalScoreText = findViewById(R.id.final_score_text);
        TextView highScoreText = findViewById(R.id.high_score_text);
        TextView highScoreNameText = findViewById(R.id.high_score_name_text);
        Button restartButton = findViewById(R.id.restart_button);

        Intent intent = getIntent();
        int finalScore = intent != null ? intent.getIntExtra(EXTRA_FINAL_SCORE, 0) : 0;
        Prefs prefs = new Prefs(this);
        int storedHighScore = prefs.getHighScore();
        String storedHighScoreName = prefs.getHighScoreName();
        String playerName = prefs.getUsername();
        if (finalScore > storedHighScore) {
            String resolvedName = TextUtils.isEmpty(playerName) ? "Player" : playerName;
            prefs.saveHighScore(resolvedName, finalScore);
            storedHighScore = finalScore;
            storedHighScoreName = resolvedName;
        }
        finalScoreText.setText("Final Score: " + finalScore);
        highScoreText.setText("High Score: " + storedHighScore);
        highScoreNameText.setText("High Score Holder: " + storedHighScoreName);
        restartButton.setEnabled(true);
        restartButton.setOnClickListener(view -> restartQuiz());
    }

    private void restartQuiz() {
        Intent intent = new Intent(this, DifficultyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
