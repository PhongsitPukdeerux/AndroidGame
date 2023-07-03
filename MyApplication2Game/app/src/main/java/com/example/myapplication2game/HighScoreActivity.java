package com.example.myapplication2game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.Comparator;


public class HighScoreActivity extends AppCompatActivity {
    private FirebaseDatabase database ;
    private DatabaseReference myRef ;
    private ConstraintLayout highScoreLayout;
    private RecyclerView recyclerView;
    ScoreAdapter adapter ;
    private AnimationDrawable animationDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        highScoreLayout  = findViewById(R.id.high_score_layout);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Scores");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Score score = dataSnapshot.getValue(Score.class);
                adapter.addScore(score);
                sortList();
                adapter.notifyDataSetChanged();
                Log.i("Added","onChildAdded : " + score.getPlayerName() + score.getScore());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Score score = dataSnapshot.getValue(Score.class);
                adapter.addScore(score);
                adapter.notifyDataSetChanged();
                Log.i("Removed","onChildRemoved : " + score.getPlayerName() + score.getScore());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        setRecyclerView();

        //animation for background
        animationDrawable = (AnimationDrawable) highScoreLayout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();
    }

    private void sortList() {
        Collections.sort( adapter.mDataSet, new Comparator<Score>() {
            @Override
            public int compare(Score s1, Score s2) {
                return s2.getScore() - s1.getScore();
            }
        });
    }

    private void setRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.score_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ScoreAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ScoreAdapter.ItemClickListener() {
        });
    }

    public void onClick(View view){
        finish();
    }

}


