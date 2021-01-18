package com.example.task2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listWithTimers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addTimerButton = findViewById(R.id.addTimerButton);
        addTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenNewActivity();
            }
        });
        listWithTimers = findViewById(R.id.listWithTimers);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        DbAdapter adapter = new DbAdapter(this);
        adapter.Open();

        List<TimerSequence> sequences = adapter.GetItems();
        ListWithTimersAdapter listWithTimersAdapter = new ListWithTimersAdapter(this, R.layout.list_with_timers_item, sequences);
        listWithTimers.setAdapter(listWithTimersAdapter);
        adapter.Close();
    }
    public void OpenNewActivity(){
        startActivity(new Intent(MainActivity.this, AddTimer.class));
    }
}