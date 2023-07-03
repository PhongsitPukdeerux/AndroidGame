package com.example.myapplication2game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable {

    volatile boolean playing;

    private Thread gameThread = null;

    private Player player;

    private Enemy[] enemies;

    private ArrayList<Star> stars = new ArrayList<>();

    private Boom boom;

    private int enemyCount = 2;
    private int score = 0;
    private boolean isGameEnd;

    Context context;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    static MediaPlayer gameStartSound;
    final MediaPlayer hitEnemySound;
    final MediaPlayer gameEndSound;

    EditText ed;



    public GameView(Context context, int screenX, int screenY) {
        super(context);
        player = new Player(context, screenX, screenY);
        surfaceHolder = getHolder();
        paint = new Paint();
        this.context = context;
        gameStartSound = MediaPlayer.create(context, R.raw.game_start);
        hitEnemySound = MediaPlayer.create(context, R.raw.impact);
        gameEndSound = MediaPlayer.create(context, R.raw.game_end);
        gameStartSound.start();

        //initializing or reset isEndGame
        isGameEnd = false;

        //initializing star object array
        int starNums = 100;
        for (int i = 0; i < starNums; i++) {
            Star s = new Star(screenX, screenY);
            stars.add(s);
        }

        //initializing enemy object array
        enemies = new Enemy[enemyCount];
        for (int i = 0; i < enemyCount; i++) {
            enemies[i] = new Enemy(context, screenX, screenY);
        }

        //initializing boom (effect)
        boom = new Boom(context);

    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    //update the coordinate of our characters.
    private void update() {

        //setting boom outside the screen
        boom.setX(-250);
        boom.setY(-250);

        player.update();
        //Updating the stars with player speed
        for (Star s : stars) {
            s.update(player.getSpeed());
        }

        for (int i = 0; i < enemyCount; i++) {
            enemies[i].update(player.getSpeed());

            //if collision occurs with player
            if (Rect.intersects(player.getDetectCollision(), enemies[i].getDetectCollision())) {

                //displaying boom at that location
                boom.setX(enemies[i].getX());
                boom.setY(enemies[i].getY());

                //moving enemy outside the left edge
                enemies[i].setX(-2000);

                //player lost their life if hit the enemy
                player.loseLife();

                hitEnemySound.start();
                //game end back to main activity
                if (player.getLife() == 0) {
                    Log.d("ken", "your dead");
                    isGameEnd = true;
                    playing = false;
                    gameStartSound.stop();
                    gameEndSound.start();



                }
            }
        }

        //Updating the enemy with player speed
        for (int i = 0; i < enemyCount; i++) {
            enemies[i].update(player.getSpeed());
        }

    }

    //draw the characters to the canvas.
    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            //drawing all stars
            paint.setColor(Color.WHITE);
            for (Star s : stars) {
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }

            //Drawing the player
            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);

            //drawing the enemies
            for (int i = 0; i < enemyCount; i++) {
                canvas.drawBitmap(
                        enemies[i].getBitmap(),
                        enemies[i].getX(),
                        enemies[i].getY(),
                        paint
                );
            }

            //drawing boom image
            canvas.drawBitmap(
                    boom.getBitmap(),
                    boom.getX(),
                    boom.getY(),
                    paint
            );

            //drawing score
            addScore(10);
            paint.setTextSize(40);
            canvas.drawText(getResources().getString(R.string.score) + " " + Integer.toString(getScore()), 50, 50, paint);

            //drawing life
            paint.setTextSize(60);
            canvas.drawText(getResources().getString(R.string.life) + " " + Integer.toString(player.getLife()), 50, 150, paint);

            //draw game Over when the game is over
            if (isGameEnd()) {
//                paint.setTextSize(150);
//                paint.setTextAlign(Paint.Align.CENTER);
//                int position = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
//                canvas.drawText("Game Over", canvas.getWidth() / 2, position, paint);
                //send to fire base
                Intent intent = new Intent(context,GameEndActivity.class);
                intent.putExtra("result",Integer.toString(getScore()));
                context.startActivity(intent);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                player.setBoosting();
                break;
        }
        //Tap on the screen to return Main Screen

        return true;
    }

    public boolean isGameEnd() {
        return isGameEnd;
    }

    public int getScore() {
        score += 1;
        return score;
    }

    public void addScore(int score) {
        this.score += score;
    }
}
