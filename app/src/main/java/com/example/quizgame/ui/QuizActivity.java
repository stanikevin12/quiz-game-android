package com.example.quizgame.ui;

import android.os.Bundle;
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
    private TextView difficultyLabel;
    private TextView questionText;
    private TextView timerText;
    private TextView mistakesText;
    private EditText answerInput;
    private Button submitButton;
    private Button nextButton;

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
}
