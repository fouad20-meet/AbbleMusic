package com.example.abblemusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class IntroActivity extends AppCompatActivity {

    private ImageView bg,logo;
    private TextView text;
    private LottieAnimationView loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        bg = findViewById(R.id.img);
        loader = findViewById(R.id.loader);
        logo = findViewById(R.id.logo);
        text = findViewById(R.id.abble);
        bg.animate().translationY(-1600).setDuration(1000).setStartDelay(4400);
        text.animate().translationY(1800).setDuration(1000).setStartDelay(4400);
        logo.animate().translationY(1800).setDuration(1000).setStartDelay(4400);
        loader.animate().translationY(1600).setDuration(1000).setStartDelay(4400);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(intent);
            }
        }, 5505);

    }
}