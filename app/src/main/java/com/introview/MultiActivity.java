package com.introview;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.introlayout.IntroView;
import com.introview.databinding.ActivityMultiBinding;

public class MultiActivity extends AppCompatActivity {

    private ActivityMultiBinding componentBinding;
    private static final String MULTI_INTRO = "multi_intro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        componentBinding = DataBindingUtil.setContentView(this, R.layout.activity_multi);
        new IntroView.Builder(this)
                .setDelayMillis(500)
                .addTarget(componentBinding.titleView, "Hi There! Click this card and see what happens.")
                .addTarget(componentBinding.leftView, "Hi There! Click this card and see what happens.")
                .addTarget(componentBinding.bottomView, "Hi There! Click this card and see what happens.")
                .addTarget(componentBinding.rightView, "Hi There! Click this card and see what happens.")
                .addTarget(componentBinding.topView, "Hi There! Click this card and see what happens.")
                .show(MULTI_INTRO);
    }
}
