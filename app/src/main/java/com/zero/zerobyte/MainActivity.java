package com.zero.zerobyte;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private Intent intent;

    private SharedPreferences preferences;

    private boolean tempUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        tempUpdate = preferences.getBoolean("updated", false);

        if(tempUpdate) {
            intent = new Intent(getBaseContext(), SettingsActivity.class);
            startActivity(intent);
            finish();
        }*/

    }

    public void onClick(View view) {
        intent = new Intent(getBaseContext(), SettingsActivity.class);
        startActivity(intent);
    }


}
