package com.example.myapplication2game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameEndActivity extends AppCompatActivity {
    private FirebaseDatabase database ;
    private DatabaseReference myRef ;
    private String s;
    EditText nameEditText;
    private LinearLayout gameEndLayout;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);

        gameEndLayout = findViewById(R.id.game_end_layout);
        s = getIntent().getStringExtra("result");
        nameEditText = findViewById(R.id.nameEditText);
        TextView scoreTextView = findViewById(R.id.text_score);
        scoreTextView.setText(s);

        //animation for background
        animationDrawable = (AnimationDrawable) gameEndLayout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();
    }

    public void back(View view){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Scores");
        Score score;
        if(nameEditText.getText().toString().equals("") || nameEditText.getText()==null){
            score = new Score("player",Integer.parseInt(s));
        }else{
            score = new Score(nameEditText.getText().toString(),Integer.parseInt(s));
        }
        myRef.push().setValue(score);
        startActivity(new Intent(this,MainActivity.class));
    }
}
