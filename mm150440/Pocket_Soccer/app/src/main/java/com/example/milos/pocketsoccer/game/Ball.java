package com.example.milos.pocketsoccer.game;

import android.graphics.PointF;
import android.util.Log;

public class Ball {

    private PointF center;

    private int r;

    private float speed;

    private float dx, dy;

    public Ball(PointF center, int r) {
        this.center = center;
        this.r = r;
        dx = dy = speed = 0;
    }


    public PointF getCenter() {
        return center;
    }

    public void setCenter(PointF center) {
        this.center = center;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {

        this.dy = dy;

    }

    public void move(double gamespeed) {
        center.x += (dx * speed / 2) * gamespeed;
        center.y += (dy * speed / 2) * gamespeed;
    }

    public void reduceSpeed() {
        if (speed > 0f) {
            speed -= 0.01f * speed;
        } else {
            speed = 0;
        }
    }

    public void scaleSpeed() {
        float max = Math.max(Math.abs(dx), Math.abs(dy));
        speed = max;

        if (max == 0)
            return;
        dx /= max;
        this.dy /= max;
        speed = Math.min(70, speed);

    }
}
