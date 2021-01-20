package com.example.task2;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;

public class TimerActivity extends AppCompatActivity {
    private int id, preparation, workingTime, rest, cycles, sets, restBetweenSets, stage = 0, amountOfStages, soundIdBell;
    private TextView timeLeftTextView, stageNameTextView;
    private ListView stagesListView;
    private ImageButton pauseButton;
    private CountDownTimer countDownTimer;
    private boolean isPause;
    private long remainingTime = 0, endTime;
    private StageInfo stagesInfo;
    private SoundPool sp;
    private boolean bound = false,serviceNeeded;
    private ServiceConnection serviceConnection;
    private Intent intent;
    TimerAppService timerAppService;
    private ProgressBar progressBar;
    private LinearLayout timerButtonsLayout,timerTopLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);
        TextView blankTextView = findViewById(R.id.blankTextView);
        timeLeftTextView = findViewById(R.id.timeLeftTextView);
        stagesListView = findViewById(R.id.stagesListView);
        stageNameTextView = findViewById(R.id.stageNameTextView);
        pauseButton = findViewById(R.id.pauseButton);
        ImageButton previousStageButton = findViewById(R.id.previousStageButton);
        ImageButton nextStageButton = findViewById(R.id.nextStageButton);
        ImageButton backButton = findViewById(R.id.backButton);
        timerButtonsLayout = findViewById(R.id.timerButtonsLayout);
        timerTopLayout = findViewById(R.id.timerTopLayout);
        progressBar = findViewById(R.id.progress);
        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundIdBell = sp.load(this, R.raw.warning, 1);
        setDarkTheme();
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        try
        {
            float fontSize = Float.parseFloat(prefs.getString(
                    getString(R.string.pref_font_size), "0"));
            timeLeftTextView.setTextSize(80+fontSize);
            stageNameTextView.setTextSize(25+fontSize);
        }
        catch(Exception ignored){

        }
        isPause = false;
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPause) {
                    if (stage < amountOfStages) {
                        pauseButton.setImageResource(android.R.drawable.ic_media_pause);
                        isPause = false;
                        createTimer();
                    }
                } else {
                    pauseButton.setImageResource(android.R.drawable.ic_media_play);
                    isPause = true;
                    countDownTimer.cancel();
                }

            }
        });
        nextStageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextStage();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        previousStageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stage > 0) {
                    stage--;
                    countDownTimer.cancel();
                    startTimer();
                }
            }
        });

        DbAdapter adapter = new DbAdapter(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
        }
        if (id > 0) {
            adapter.Open();
            TimerSequence sequence = adapter.GetItem(id);
            preparation = sequence.preparation;
            workingTime = sequence.workingTime;
            rest = sequence.rest;
            cycles = sequence.cycles;
            sets = sequence.sets;
            restBetweenSets = sequence.restBetweenSets;
            blankTextView.setBackgroundColor(sequence.color);
            adapter.Close();
        }

        stagesInfo = new StageInfo();
        stagesInfo.setStageInfo(preparation,sets,cycles,workingTime,rest,restBetweenSets,getString(R.string.add_timer_preparations),
                getString(R.string.add_timer_working_time),getString(R.string.add_timer_rest),getString(R.string.add_timer_rest_between_sets));
        amountOfStages = sets * (2*cycles + 1)+1;

        StageAdapter stageAdapter = new StageAdapter(this,
                R.layout.stages_list_view_item, stagesInfo.getStagesInfo());
        stagesListView.setAdapter(stageAdapter);
        serviceNeeded = true;
        remainingTime = stagesInfo.getStageInfo(stage).second * 1000+1000;
        intent = new Intent(this, TimerAppService.class);
        progressBar.setMax(stagesInfo.getStageInfo(stage).second * 1000);
        progressBar.setProgress(0);
        ArrayList<Integer> seconds = new ArrayList<>();
        for (int i = 0; i < amountOfStages; i++) {
            seconds.add(stagesInfo.getStageInfo(i).second);
        }
        intent.putExtra("seconds", seconds);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                timerAppService = ((TimerAppService.TimerBinder) binder).getService();
                bound = true;
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                bound = false;
            }
        };
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();

        if (bound) {
            stage = timerAppService.getStage();
            remainingTime = timerAppService.getRemainingTime();
            unbindService(serviceConnection);
            stopService(intent);
            bound = false;
        }

        if (isPause) {
            pauseButton.setImageResource(android.R.drawable.ic_media_play);
        } else {
            pauseButton.setImageResource(android.R.drawable.ic_media_pause);
        }

        timeLeftTextView.setText(String.valueOf((int) (remainingTime / 1000)));
        stageNameTextView.setText(getString(R.string.now_playing)+"  "+stagesInfo.getStageInfo(stage).first);

        if (!isPause) {
            createTimer();
        }
        stagesListView.setSelection(stage);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (serviceNeeded && (!bound) && (!isPause)) {
            intent.putExtra("stage", stage);
            intent.putExtra("remainingTime", remainingTime);
            startService(intent);
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
            bound = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bound) {
            unbindService(serviceConnection);
            stopService(intent);
            bound = false;
        }
    }
    public void createTimer() {
        endTime = remainingTime + System.currentTimeMillis();
        progressBar.setProgress((int) (stagesInfo.getStageInfo(stage).second * 1000 - remainingTime));
        countDownTimer = new CountDownTimer(remainingTime, 1000) {
            @Override
            public void onTick(long l) {
                remainingTime = l;
                progressBar.incrementProgressBy(1000);
                timeLeftTextView.setText(String.valueOf((int) (l / 1000)));
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(stagesInfo.getStageInfo(stage).second * 1000+ 1000);
                sp.play(soundIdBell, 1, 1, 0, 0, 1);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                    }
                }, 1000);
                startNextStage();
            }
        }.start();
    }
    public void startTimer() {
        progressBar.setMax(stagesInfo.getStageInfo(stage).second * 1000);
        progressBar.setProgress(0);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        stageNameTextView.setText(getString(R.string.now_playing)+" "+stagesInfo.getStageInfo(stage).first);
        remainingTime = stagesInfo.getStageInfo(stage).second * 1000+1000;
        timeLeftTextView.setText(String.valueOf((int) (remainingTime / 1000)));
        if (!isPause) {
            createTimer();
        }
        stagesListView.setSelection(stage);
    }

    public void startNextStage() {

        if (stage == amountOfStages - 1) {
            stage++;
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            timeLeftTextView.setText(R.string.finish);
            stageNameTextView.setText(R.string.finish);
        } else if (stage != amountOfStages) {
            stage++;
            startTimer();
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.finishTraining)
                .setMessage(R.string.finishTrainingSure)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        countDownTimer.cancel();
                        serviceNeeded = false;
                        finish();
                        dialog.dismiss();
                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isPause", isPause);
        outState.putInt("stage", stage);
        outState.putLong("remainingTime", remainingTime);
        outState.putLong("endTime", endTime);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isPause = savedInstanceState.getBoolean("isPause");
        stage = savedInstanceState.getInt("stage");
        remainingTime = savedInstanceState.getLong("remainingTime");
        endTime = savedInstanceState.getLong("endTime");
        if (!isPause) {
            remainingTime = endTime - System.currentTimeMillis();
        }
    }
    private void setDarkTheme(){
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this) ;
        if (prefs.getBoolean(getString(R.string.pref_dark_theme), false))
        {
            timerButtonsLayout.setBackgroundResource(R.drawable.night_theme);
            timerTopLayout.setBackgroundResource(R.drawable.night_theme);
            int textColor = ContextCompat.getColor(this, R.color.colorWhite);
            timeLeftTextView.setBackgroundResource(R.drawable.night_theme);
            timeLeftTextView.setTextColor(textColor);
            stageNameTextView.setBackgroundResource(R.drawable.night_theme);
            stageNameTextView.setTextColor(textColor);
        }
    }
}