package com.example.task2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ListView listWithTimers;
    private  Button addTimerButton;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this) ;
        loadLocale(prefs.getString(getString(R.string.pref_language), "Русский язык"));
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.main_layout);


        addTimerButton = findViewById(R.id.addTimerButton);
        addTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenNewActivity();
            }
        });
        listWithTimers = findViewById(R.id.listWithTimers);
        if (prefs.getBoolean(getString(R.string.pref_dark_theme), false))
        {
            layout.setBackgroundResource(R.drawable.night_theme);
            listWithTimers.setBackgroundResource(R.drawable.night_theme);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

    }

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_button, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, AppSettings.class);
            startActivity(intent);
            finish();
        }
        return true;
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

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this) ;
        try
        {
            float fontSize = Float.parseFloat(prefs.getString(
                    getString(R.string.pref_font_size), "0"));
            addTimerButton.setTextSize(14+fontSize);
        }
        catch (Exception ignored)
        {

        }

    }
    public void loadLocale(String language){
        switch (language)
        {
            case "Русский язык":
                setLocale("ru");
                break;
            case "Беларуская мова":
                setLocale("be");
                break;
            case "English":
                setLocale("en");
                break;
            default:
                break;
        }
    }
    public void setLocale(String language)
    {
        Locale myLocale = new Locale(language);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.locale = myLocale;
        resources.updateConfiguration(config, null);
    }
    public void OpenNewActivity(){
        startActivity(new Intent(MainActivity.this, AddTimer.class));
    }
}