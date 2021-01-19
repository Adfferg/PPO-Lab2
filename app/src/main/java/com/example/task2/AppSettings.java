package com.example.task2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AppSettings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_fragment, new AppSettingsFragment())
                .commit();
    }
}
