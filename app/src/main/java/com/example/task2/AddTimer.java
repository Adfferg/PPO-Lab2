package com.example.task2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import java.sql.Time;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddTimer extends AppCompatActivity {
    private int color;
    private int id;
    private DbAdapter dbAdapter;
    private ScrollView addTimerScrollView;
    EditText nameEditText,preparationEditText,workingTimeEditText,restEditText,cyclesEditText,setsEditText,restBetweenSetsEditText;
    TextView addTimerTitle,add_timer_textView1,add_timer_textView2,add_timer_textView3,add_timer_textView4,add_timer_textView5,
            add_timer_textView6,add_timer_textView7;
    Button colorButton,saveTimerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_timer);
        nameEditText = findViewById(R.id.nameEditText);
        preparationEditText = findViewById(R.id.preparationEditText);
        workingTimeEditText = findViewById(R.id.workingTimeEditText);
        restEditText = findViewById(R.id.restEditText);
        cyclesEditText = findViewById(R.id.cyclesEditText);
        setsEditText = findViewById(R.id.setsEditText);
        restBetweenSetsEditText = findViewById(R.id.restBetweenSetsEditText);
         colorButton = findViewById(R.id.colorButton);
         saveTimerButton = findViewById(R.id.saveTimerButton);
         addTimerTitle = findViewById(R.id.addTimerTitle);
         add_timer_textView1 = findViewById(R.id.add_timer_textView1);
         add_timer_textView2 = findViewById(R.id.add_timer_textView2);
         add_timer_textView3 = findViewById(R.id.add_timer_textView3);
         add_timer_textView4 = findViewById(R.id.add_timer_textView4);
         add_timer_textView5 = findViewById(R.id.add_timer_textView5);
         add_timer_textView6 = findViewById(R.id.add_timer_textView6);
         add_timer_textView7 = findViewById(R.id.add_timer_textView7);
        addTimerScrollView = findViewById(R.id.addTimerScrollView);
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        try
        {
            float fontSize = Float.parseFloat(prefs.getString(
                    getString(R.string.pref_font_size), "0"));
            changeTextFontSize(fontSize);
        }
        catch (Exception ignored)
        {

        }
        if (prefs.getBoolean(getString(R.string.pref_dark_theme), false))
        {
            setDarkTheme();
        }
        dbAdapter = new DbAdapter(this);
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPiker();
            }
        });
        saveTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                String name = nameEditText.getText().toString();
                int preparations = Integer.parseInt(preparationEditText.getText().toString());
                int workingTime = Integer.parseInt(workingTimeEditText.getText().toString());
                int rest = Integer.parseInt(restEditText.getText().toString());
                int cycles = Integer.parseInt(cyclesEditText.getText().toString());
                int sets = Integer.parseInt(setsEditText.getText().toString());
                int restBetweenSets = Integer.parseInt(restBetweenSetsEditText.getText().toString());
                TimerSequence sequence = new TimerSequence(id, name, preparations, workingTime, rest, cycles, sets, restBetweenSets, color);
                dbAdapter.Open();
                if (id > 0)
                {
                    dbAdapter.UpdateDb(sequence);
                } else {
                    dbAdapter.InsertIntoDb(sequence);
                }
                dbAdapter.Close();
                OpenMainActivity();}
                catch (Exception exception){
                    new AlertDialog.Builder(AddTimer.this)
                            .setTitle(R.string.error)
                            .setMessage(R.string.error_message)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }
        });
        Bundle item = getIntent().getExtras();
        if (item != null)
        {
            id = item.getInt("id");
        }
        if (id > 0)
        {
            dbAdapter.Open();
            TimerSequence sequence = dbAdapter.GetItem(id);
            nameEditText.setText(sequence.name);
            preparationEditText.setText(String.valueOf(sequence.preparation));
            workingTimeEditText.setText(String.valueOf(sequence.workingTime));
            restEditText.setText(String.valueOf(sequence.rest));
            cyclesEditText.setText(String.valueOf(sequence.cycles));
            setsEditText.setText(String.valueOf(sequence.sets));
            restBetweenSetsEditText.setText(String.valueOf(sequence.restBetweenSets));
            color = sequence.color;
            dbAdapter.Close();
            saveTimerButton.setText(R.string.list_with_timers_item_change);
            addTimerTitle.setText(R.string.add_timer_changing_title);

        }
    }
    public void OpenMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
    public void openColorPiker(){
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, color, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int newColor) {
                color=newColor;
            }
        });
        colorPicker.show();
    }
    private void setDarkTheme(){
        addTimerScrollView.setBackgroundResource(R.drawable.night_theme);
        int textColor = ContextCompat.getColor(this, R.color.colorWhite);
        nameEditText.setTextColor(textColor);
        preparationEditText.setTextColor(textColor);
        workingTimeEditText.setTextColor(textColor);
        restEditText.setTextColor(textColor);
        cyclesEditText.setTextColor(textColor);
        setsEditText.setTextColor(textColor);
        restBetweenSetsEditText.setTextColor(textColor);
        addTimerTitle.setTextColor(textColor);
        add_timer_textView1.setTextColor(textColor);
        add_timer_textView2.setTextColor(textColor);
        add_timer_textView3.setTextColor(textColor);
        add_timer_textView4.setTextColor(textColor);
        add_timer_textView5.setTextColor(textColor);
        add_timer_textView6.setTextColor(textColor);
        add_timer_textView7.setTextColor(textColor);
    }
    private void changeTextFontSize(float fontSize){
        nameEditText.setTextSize(14+fontSize);
        preparationEditText.setTextSize(14+fontSize);
        workingTimeEditText.setTextSize(14+fontSize);
        restEditText.setTextSize(14+fontSize);
        cyclesEditText.setTextSize(14+fontSize);
        setsEditText.setTextSize(14+fontSize);
        restBetweenSetsEditText.setTextSize(14+fontSize);
        colorButton.setTextSize(14+fontSize);
        saveTimerButton.setTextSize(14+fontSize);
        addTimerTitle.setTextSize(14+fontSize);
        add_timer_textView1.setTextSize(14+fontSize);
        add_timer_textView2.setTextSize(14+fontSize);
        add_timer_textView3.setTextSize(14+fontSize);
        add_timer_textView4.setTextSize(14+fontSize);
        add_timer_textView5.setTextSize(14+fontSize);
        add_timer_textView6.setTextSize(14+fontSize);
        add_timer_textView7.setTextSize(14+fontSize);
    }

}