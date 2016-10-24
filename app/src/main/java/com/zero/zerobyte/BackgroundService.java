package com.zero.zerobyte;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.TrafficStats;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by FKRT on 16.10.2016.
 */
public class BackgroundService extends Service {

    private boolean isRunning;
    private Context context;
    private Thread thread;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private int remainingInt, difference;
    private String rxmb, txmb;

    @Override
    public void onCreate() {
        super.onCreate();

        this.context = this;
        this.isRunning = false;
        this.thread = new Thread(myTask);

        preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        remainingInt = 0;
        difference = 0;
        rxmb = "";
        txmb = "";
    }

    private Runnable myTask = new Runnable() {
        @Override
        public void run() {
            remainingInt = preferences.getInt("remainingInt", -1);
            rxmb = preferences.getString("rxmb", "0000");
            txmb = preferences.getString("txmb", "0000");

            long newRxmb = (TrafficStats.getTotalRxBytes()) / 1048576;
            long newTxmb = (TrafficStats.getTotalTxBytes()) / 1048576;

            difference += (newRxmb + newTxmb);
            difference -= Integer.parseInt(rxmb) + Integer.parseInt(txmb);

            editor = getSharedPreferences("MyPreferences", MODE_PRIVATE).edit();
            editor.putInt("remainingInt", remainingInt - difference);
            editor.putString("rxmb", newRxmb + "");
            editor.putString("txmb", newTxmb + "");
            editor.commit();

            stopSelf();
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(!isRunning) {
            this.isRunning = true;
            this.thread.start();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.isRunning = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
