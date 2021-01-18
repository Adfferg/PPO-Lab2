package com.example.task2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Time;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddTimer extends AppCompatActivity {
    private int color;
    private int id;
    private DbAdapter dbAdapter;
    EditText nameEditText,preparationEditText,workingTimeEditText,restEditText,cyclesEditText,setsEditText,restBetweenSetsEditText;
    @SuppressLint("SetTextI18n")
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
        Button colorButton = findViewById(R.id.colorButton);
        Button saveTimerButton = findViewById(R.id.saveTimerButton);
        TextView addTimerTitle = findViewById(R.id.addTimerTitle);
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
                OpenMainActivity();
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

}