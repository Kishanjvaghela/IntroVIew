package com.introview;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.introlayout.IntroView;
import com.introview.databinding.ActivitySingleBinding;

public class SingleActivity extends AppCompatActivity {

    private ActivitySingleBinding componentBinding;
    private static final String SINGLE_INTRO = "single intro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        componentBinding = DataBindingUtil.setContentView(this, R.layout.activity_single);
        new IntroView.Builder(this)
                .setDelayMillis(500)
                .addTarget(componentBinding.titleView, "Hi There! Click this card and see what happens.")
                .show(SINGLE_INTRO);
    }
}
