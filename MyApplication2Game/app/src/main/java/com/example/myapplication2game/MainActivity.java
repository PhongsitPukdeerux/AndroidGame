package com.example.myapplication2game;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout mainLayout;
    private ImageButton buttonPlay, buttonHighScore;

    static MediaPlayer gameStartSound;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mainLayout = findViewById(R.id.main_layout);
        gameStartSound = MediaPlayer.create(MainActivity.this, R.raw.friends);
        gameStartSound.start();

        //animation for background
        animationDrawable = (AnimationDrawable) mainLayout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

        buttonPlay = findViewById(R.id.buttonPlay);
        buttonHighScore = findViewById(R.id.buttonScore);

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("ken", "You clicked play button");
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });
        buttonHighScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("ken", "You clicked high score button");
                //startActivity
                startActivityForResult(new Intent(MainActivity.this, HighScoreActivity.class),0);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        gameStartSound.stop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        gameStartSound = MediaPlayer.create(MainActivity.this, R.raw.friends);
        gameStartSound.start();
    }
}
