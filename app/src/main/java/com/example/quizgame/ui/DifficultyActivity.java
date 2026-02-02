package com.example.quizgame.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizgame.R;
import com.example.quizgame.model.Difficulty;

public class DifficultyActivity extends AppCompatActivity {
    public static final String EXTRA_DIFFICULTY = "com.example.quizgame.extra.DIFFICULTY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_difficulty);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        View.OnClickListener listener = this::handleDifficultyClick;
        Button easyButton = findViewById(R.id.button_easy);
        Button mediumButton = findViewById(R.id.button_medium);
        Button hardButton = findViewById(R.id.button_hard);
        easyButton.setOnClickListener(listener);
        mediumButton.setOnClickListener(listener);
        hardButton.setOnClickListener(listener);
    }

    private void handleDifficultyClick(View view) {
        Object tag = view.getTag();
        if (tag == null) {
            return;
        }
        Difficulty difficulty = Difficulty.valueOf(tag.toString());
        Intent intent = new Intent();
        intent.setClassName(this, "com.example.quizgame.ui.QuizActivity");
        intent.putExtra(EXTRA_DIFFICULTY, difficulty.name());
        startActivity(intent);
    }
}
