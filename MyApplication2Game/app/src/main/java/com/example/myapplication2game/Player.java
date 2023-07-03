package com.example.myapplication2game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Player {
    //Bitmap to get character from image
    private Bitmap bitmap;

    //coordinates of the character
    private int x;
    private int y;

    //speed of the character
    private int speed = 0;
    //life of the character
    private int life = 0;

    private boolean boosting;

    private final int GRAVITY = -10;

    //to make the ship to stay in the screen
    private int maxY, minY;

    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;
    //for implement item that increase life of the character
//    private final int MAX_LIFE = 5;

    //creating a rect object for collision detection
    private Rect detectCollision;

    //constructor
    public Player(Context context, int screenX, int screenY) {
        // init character coordinates, speed, life
        x = 75;//400;
        y = 50;//1500;
        speed = 1;
        life = 3;

        //this will get the image from res/drawable to draw it
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);

        //calculating maxY, minY both of them will use to cap the the min and max height of the character so it won't go of screen
        maxY = screenY - bitmap.getHeight();
        minY = 0;

        boosting = false;

        //initializing rect object of detect collision
        detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void setBoosting() {
        boosting = true;
    }

    public void stopBoosting() {
        boosting = false;
    }

    public void loseLife() {
        if (life > 0) {
            life -= 1;
        } else {
            life = 0;
        }
    }

    public void resetLife() {
        life = 3;
    }

    //Method to update coordinate of character
    public void update() {
        //if the ship is boosting
        if (boosting) {
            //speeding up the ship
            speed += 2;
        } else {
            //slowing down if not boosting
            speed -= 5;
        }
        //cap the max ans min speed of the character
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }

        //moving the ship down
        y -= speed + GRAVITY;

        //to make it won't go off the screen
        if (y < minY) {
            y = minY;
        }
        if (y > maxY) {
            y = maxY;
        }

        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();

    }

    public Rect getDetectCollision() {
        return detectCollision;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    public int getLife() {
        return life;
    }

}
