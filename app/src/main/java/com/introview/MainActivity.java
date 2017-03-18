package com.introview;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.introlayout.IntroView;
import com.introview.databinding.ActivityMainBinding;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        new IntroView.Builder(this)
                .setDelayMillis(500)
                .performClick(true)
                .addTarget(mainBinding.titleView, "Hi There! Click this card and see what happens.")
                .addTarget(mainBinding.leftView, "Hi There! Click this card and see what happens.")
                .addTarget(mainBinding.bottomView, "Hi There! Click this card and see what happens.")
                .addTarget(mainBinding.rightView, "Hi There! Click this card and see what happens.")
                .addTarget(mainBinding.topView, "Hi There! Click this card and see what happens.")
                .show(UUID.randomUUID().toString());

    }
}
