package com.minewbeacon.blescan.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yuliwuli.blescan.demo.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("state","launch");
        startActivity(intent);
        finish();
    }
}
