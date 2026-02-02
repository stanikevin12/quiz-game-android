package com.example.quizgame.data;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private static final String PREFS_NAME = "quiz_game_prefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_HIGH_SCORE_NAME = "high_score_name";
    private static final String KEY_HIGH_SCORE = "high_score";

    private final SharedPreferences sharedPreferences;

    public Prefs(Context context) {
        sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveUser(String username, String password) {
        sharedPreferences.edit()
                .putString(KEY_USERNAME, username == null ? "" : username)
                .putString(KEY_PASSWORD, password == null ? "" : password)
                .apply();
    }

    public boolean validateUser(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        String storedUsername = sharedPreferences.getString(KEY_USERNAME, "");
        String storedPassword = sharedPreferences.getString(KEY_PASSWORD, "");
        return username.equals(storedUsername) && password.equals(storedPassword);
    }

    public void saveHighScore(String name, int score) {
        sharedPreferences.edit()
                .putString(KEY_HIGH_SCORE_NAME, name == null ? "" : name)
                .putInt(KEY_HIGH_SCORE, score)
                .apply();
    }

    public int getHighScore() {
        return sharedPreferences.getInt(KEY_HIGH_SCORE, 0);
    }

    public String getHighScoreName() {
        return sharedPreferences.getString(KEY_HIGH_SCORE_NAME, "");
    }
}
