package com.zero.zerobyte;

import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.TrafficStats;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private EditText date, time, totalData, remainingData;
    private SharedPreferences.Editor editor;

    private Button activation;

    private SharedPreferences preferences;

    private boolean update, update1, activated;

    private int totalInt, remainingInt;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.context = this;

        preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        update = preferences.getBoolean("updated", false);
        update1 = preferences.getBoolean("updated1", false);
        activated = preferences.getBoolean("activated", false);

        totalInt = preferences.getInt("totalInt", -1);
        remainingInt = preferences.getInt("remainingInt", -1);

        date = (EditText) findViewById(R.id.resetDate);
        time = (EditText) findViewById(R.id.resetTime);
        totalData = (EditText) findViewById(R.id.totalData);
        remainingData = (EditText) findViewById(R.id.remainingData);
        activation = (Button) findViewById(R.id.activate);

        if(totalInt != -1 || remainingInt != -1) {
            totalData.setHint("Total quota : " + totalInt + " mb");
            remainingData.setHint("Remaining quota : " + remainingInt + " mb");
        }

        if(update) {
            date.setText("every " + preferences.getString("resetDate", "1") + "th of the month");
            time.setText("at " + preferences.getString("resetTime", "00:00"));
        }

        if(update1) {
            totalData.setText("Total quota : " + totalInt + " mb");
            remainingData.setText("Remaining quota : " + remainingInt + " mb");
        }

        if(update && update1) {
            Button button = (Button) findViewById(R.id.activate);
            button.setVisibility(View.VISIBLE);
        }

        if(activated) {
            activation.setText(" STOP WORKING ");
        }

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker datePicker = new DatePicker();
                datePicker.setView(v);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                datePicker.show(fragmentTransaction, "Date Picker");
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker timePicker = new TimePicker();
                timePicker.setView(v);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                timePicker.show(fragmentTransaction, "Time Picker");
            }
        });

        totalData.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!totalData.getText().toString().equals("")) {
                    totalInt = Integer.parseInt(totalData.getText().toString());
                    totalData.setHint("Total quota : " + totalData.getText().toString() + " mb");
                    totalData.setText("");
                }
            }
        });

        remainingData.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!remainingData.getText().toString().equals("")) {
                    remainingInt = Integer.parseInt(remainingData.getText().toString());
                    remainingData.setHint("Total quota : " + remainingData.getText().toString() + " mb");
                    remainingData.setText("");
                }
            }
        });
    }

    // update date and time
    public void onClick(View view) {

        if(date.getText().toString().equals("") || time.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "update failed! fill both fields", Toast.LENGTH_LONG).show();
        } else {
            String tempDate = "", tempTime = "";

            char[] dateArr = date.getText().toString().toCharArray();
            int i = 6;
            while(dateArr[i] != 't') {
                tempDate += dateArr[i];
                i++;
            }

            tempTime = time.getText().toString().substring(3);

            editor = getSharedPreferences("MyPreferences", MODE_PRIVATE).edit();
            editor.putBoolean("updated", true);
            editor.putString("resetDate", tempDate);
            editor.putString("resetTime", tempTime);
            editor.commit();

            update = true;
            if(update1) {
                activation.setVisibility(View.VISIBLE);
            }

            Toast.makeText(getBaseContext(), "succesfully updated", Toast.LENGTH_LONG).show();
        }
    }

    // update internet quota and remaining quota
    public void onClick1(View view) {
        int tmp;
        try {
            tmp = Integer.parseInt(totalData.getText().toString());
            totalData.setHint("Total quota : " + tmp + " mb");
            totalInt = tmp;
        } catch (NumberFormatException e) {
        }
        totalData.setText("");

        try {
            tmp = Integer.parseInt(remainingData.getText().toString());
            remainingData.setHint("Remaining quota : " + tmp + " mb");
            remainingInt = tmp;
        } catch (NumberFormatException e) {
        }
        remainingData.setText("");

        if(totalInt == -1 || remainingInt == -1) {
            Toast.makeText(getBaseContext(), "update failed! fill both fields", Toast.LENGTH_LONG).show();
        } else {

            if(totalInt < remainingInt) {
                Toast.makeText(getBaseContext(), "Internet quota exceeded", Toast.LENGTH_LONG).show();
            }

            long txmb = (TrafficStats.getTotalTxBytes()) / 1048576;
            long rxmb = (TrafficStats.getTotalRxBytes()) / 1048576;

            editor.putString("rxmb", rxmb + "");
            editor.putString("txmb", txmb + "");

            editor = getSharedPreferences("MyPreferences", MODE_PRIVATE).edit();
            editor.putBoolean("updated1", true);
            editor.putInt("totalInt", totalInt);
            editor.putInt("remainingInt", remainingInt);
            editor.commit();

            update1 = true;
            if(update) {
                activation.setVisibility(View.VISIBLE);
            }

            Toast.makeText(getBaseContext(), "succesfully updated", Toast.LENGTH_LONG).show();

        }
    }

    // start or stop backgroud service
    public void onClick2(View view) {
        boolean temp = preferences.getBoolean("activated", false);
        editor = getSharedPreferences("MyPreferences", MODE_PRIVATE).edit();

        if(temp) {
            editor.putBoolean("activated", false);
            activation.setText(" START WORKING ");

            Toast.makeText(getBaseContext(), "succesfully stopped working", Toast.LENGTH_LONG).show();

            Intent alarm = new Intent(this.context, AlarmReceiver.class);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 0, alarm, 0);
            alarmManager.cancel(pendingIntent);
            //Intent background = new Intent(context, BackgroundService.class);
            //context.stopService(background);
        } else {
            long a =TrafficStats.getTotalTxBytes();
            long txmb = (TrafficStats.getTotalTxBytes()) / 1048576;
            long rxmb = (TrafficStats.getTotalRxBytes()) / 1048576;

            editor.putString("rxmb", rxmb + "");
            editor.putString("txmb", txmb + "");
            editor.putBoolean("activated", true);

            long z = Long.parseLong(preferences.getString("rxmb", "0000")) + Long.parseLong(preferences.getString("txmb", "0000"));
            //createNotification(z + "");

            activation.setText(" STOP WORKING ");
            Toast.makeText(getBaseContext(), "succesfully started working", Toast.LENGTH_LONG).show();

            Intent alarm = new Intent(this.context, AlarmReceiver.class);
            boolean alarmBool = (PendingIntent.getBroadcast(this.context, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);

            if(!alarmBool) {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 0, alarm, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 15000, pendingIntent);
            }

        }
        editor.commit();
    }

    public void createNotification(String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Internet Usage");
        builder.setContentText("So far " + text + "mb (%5) internet used!");
        builder.setSmallIcon(R.drawable.yuz);
        builder.setOngoing(true);

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(1, notification);
    }

}
