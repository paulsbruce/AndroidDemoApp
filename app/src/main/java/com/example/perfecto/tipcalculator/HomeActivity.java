package com.example.perfecto.tipcalculator;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void btn_home_calc_click(View view) {
        startActivity(new Intent(this, CalcTipActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void btn_home_manage_click(View view) {
        startActivity(new Intent(this, FlexViewActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
