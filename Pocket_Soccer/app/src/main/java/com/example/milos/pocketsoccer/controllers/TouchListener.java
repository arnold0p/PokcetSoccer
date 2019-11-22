package com.example.milos.pocketsoccer.controllers;

import android.view.MotionEvent;
import android.view.View;

import com.example.milos.pocketsoccer.game.GameView;

public class TouchListener implements View.OnTouchListener {


    private GameView mGameView;
    private Controller mController;

    public TouchListener(GameView mGameView, Controller mController) {
        this.mGameView = mGameView;
        this.mController = mController;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                mController.down(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                mController.move(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_UP:
                mController.up(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mController.pointerDown();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mController.pointerUp();
                break;

        }
        return true;
    }
}
