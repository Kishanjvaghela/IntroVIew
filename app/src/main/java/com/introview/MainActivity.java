package com.introview;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.introview.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.singleButtonDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSingleComponentActivity();
            }
        });
        mainBinding.multiButtonDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMultiComponentActivity();
            }
        });
    }

    private void openSingleComponentActivity() {
        Intent intent = new Intent(this, SingleActivity.class);
        startActivity(intent);
    }

    private void openMultiComponentActivity() {
        Intent intent = new Intent(this, MultiActivity.class);
        startActivity(intent);
    }
}
