package com.example.task2;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;


import java.util.ArrayList;

public class TimerAppService extends Service {
    TimerBinder timerBinder = new TimerBinder();
    private CountDownTimer timer;
    private ArrayList<Integer> seconds;
    private int stage, amountOfStages;
    private SoundPool sp;
    private int warningSound;
    private long remainingTime;

    public IBinder onBind(Intent intent) {
        return timerBinder;
    }

    public void onCreate() {
        super.onCreate();
        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        warningSound = sp.load(this, R.raw.warning, 1);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        seconds = intent.getIntegerArrayListExtra("seconds");
        amountOfStages = seconds.size();
        remainingTime = intent.getLongExtra("remainingTime", 0);
        stage = intent.getIntExtra("stage", 0);
        createTimer();
        return super.onStartCommand(intent, flags, startId);
    }

    public void createTimer() {
        timer = new CountDownTimer(remainingTime, 1000) {
            @Override
            public void onFinish() {
                sp.play(warningSound, 1, 1, 0, 0, 1);
                startNextStage();
            }
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
            }
        }.start();
    }

    public void startNextStage() {

        if (stage == amountOfStages - 1) {
            stage++;
            if (timer != null) {
                timer.cancel();
            }
        } else if (stage != amountOfStages) {
            stage++;
            if (timer != null) {
                timer.cancel();
            }
            remainingTime = seconds.get(stage) * 1000;
            createTimer();
        }
    }

    public int getStage() {
        return stage;
    }
    public boolean onUnbind(Intent intent)
    {
        return super.onUnbind(intent);
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    class TimerBinder extends Binder {
        TimerAppService getService() {
            return TimerAppService.this;
        }
    }
}
