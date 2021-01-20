package com.example.task2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class AppSettings extends AppCompatActivity {
    private LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        layout = findViewById(R.id.settingsLayout);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_fragment, new AppSettingsFragment())
                .commit();
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        if (prefs.getBoolean(getString(R.string.pref_dark_theme), false))
        {
            layout.setBackgroundResource(R.drawable.night_theme);
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
