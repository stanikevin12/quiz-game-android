package com.example.quizgame.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizgame.R;

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
        Button restartButton = findViewById(R.id.restart_button);

        Intent intent = getIntent();
        int finalScore = intent != null ? intent.getIntExtra(EXTRA_FINAL_SCORE, 0) : 0;
        finalScoreText.setText("Final Score: " + finalScore);
        highScoreText.setText("High Score: --");
        restartButton.setEnabled(true);
    }
}
