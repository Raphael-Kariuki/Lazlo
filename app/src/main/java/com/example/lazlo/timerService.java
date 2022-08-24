package com.example.lazlo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.material.textview.MaterialTextView;

public class timerService extends Service {
    public static final String EXTRA_RECEIVER = "EXTRA_RECEIVER";
    public static final String EXTRA_DATA = "EXTRA_DATA";
    public static final int RESULT_OK = 1;
    public static final String ACTION_GET_DATA = "ACTION_GET_DATA";
    int seconds;
    boolean running;
    String stopWatch;
    @Override
    public int onStartCommand(Intent intent, int flags, int startTd){
        runTimer();
        return super.onStartCommand(intent, flags, startTd);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void runTimer(){
        final Handler handler = new Handler();
        // Call the post() method, passing in a new Runnable.
        // The post() method processes code without a delay,so the code in the Runnable will run almost immediately.
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                stopWatch = String.format(HouseOfCommons.locale,"%d:%02d:%02d",hours, minutes, secs);
                if (running){
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }
    private final BroadcastReceiver onGetDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //send dat to activity
            ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RECEIVER);
            Bundle resultData = new Bundle();
            resultData.putString(EXTRA_DATA,stopWatch);
            receiver.send(RESULT_OK,resultData);
        }
    };
    @Override
    public void onCreate(){
        super.onCreate();
        registerReceiver(onGetDataReceiver, new IntentFilter(ACTION_GET_DATA));
    }
}
