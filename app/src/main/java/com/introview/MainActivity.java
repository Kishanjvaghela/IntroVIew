package com.introview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.introlayout.IntroView;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titleView = (TextView) findViewById(R.id.titleView);

        new IntroView.Builder(this)
                .setDelayMillis(500)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText("Hi There! Click this card and see what happens.")
                .setTarget(titleView)
                .setUsageId(UUID.randomUUID().toString()) //THIS SHOULD BE UNIQUE ID
                .show();

    }
}
