package com.example.milos.pocketsoccer.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.milos.pocketsoccer.R;
import com.example.milos.pocketsoccer.controllers.Controller;
import com.example.milos.pocketsoccer.controllers.TouchListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameActivity extends AppCompatActivity {


    private Controller mController;

    private GameView mGameView;

    private int mode, endval, p1, p2, flag1, flag2, backg;

    private double speed;

    private String name1, name2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mGameView = findViewById(R.id.gameview);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int resume = getIntent().getIntExtra("resume", 0);

        if (resume == 1) {
            restoreValues();
        } else {

            mode = getIntent().getIntExtra("mode", 0);
            endval = getIntent().getIntExtra("endval", 0);
            speed = getIntent().getDoubleExtra("speed", 0);

            p1 = getIntent().getIntExtra("p1", 0);
            p2 = getIntent().getIntExtra("p2", 0);

            mController = new Controller(mGameView, this, mode, endval, speed, p1, p2);

            mGameView.setOnTouchListener(new TouchListener(mGameView, mController));
            flag1 = getIntent().getIntExtra("flag1", 0);
            flag2 = getIntent().getIntExtra("flag2", 0);

            mGameView.setFlags(flag1, flag2);

            backg = getIntent().getIntExtra("backg", 0);

            mGameView.setBackg(backg);

            name1 = getIntent().getStringExtra("n1");
            name2 = getIntent().getStringExtra("n2");

            mController.whistle();
            mController.start();
        }

    }

    public void end() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("goal1", mGameView.goals()[0]);
        returnIntent.putExtra("goal2", mGameView.goals()[1]);

        returnIntent.putExtra("name1", name1);
        returnIntent.putExtra("name2", name2);

        returnIntent.putExtra("p1", p1);
        returnIntent.putExtra("p2", p2);

        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void saveGame() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        //Log.i("eee", "back button pressed");

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("mode", mode);
        editor.putInt("endval", mController.getEndval());
        editor.putFloat("speed", (float) speed);

        editor.putLong("t", mController.getT());
        editor.putInt("ti", mController.getTime());
        editor.putInt("sl", mController.getSleep());
        editor.putBoolean("go", mController.isGoal());

        editor.putInt("p1", p1);
        editor.putInt("p2", p2);
        editor.putInt("f1", flag1);
        editor.putInt("f2", flag2);
        editor.putInt("backg", backg);
        editor.putString("n1", name1);
        editor.putString("n2", name2);
        int[] goals = mGameView.goals();
        editor.putInt("g1", goals[0]);
        editor.putInt("g2", goals[1]);

        editor.putInt("player", mGameView.getPlayer());

        Gson gson = new Gson();
        String json = gson.toJson(mGameView.getBall());
        editor.putString("ball", json);

        gson = new Gson();
        json = gson.toJson(mGameView.getTeam1());
        editor.putString("team1", json);

        gson = new Gson();
        json = gson.toJson(mGameView.getTeam2());
        editor.putString("team2", json);

        //   Log.i("EEE", mGameView.getBall().getSpeed() + "");
        editor.commit();

        mController.end();
        finish();
    }

    private void restoreValues() {

        SharedPreferences editor = getPreferences(Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = editor.getString("ball", "");
        Ball ball = gson.fromJson(json, Ball.class);

        Type type = new TypeToken<List<Ball>>() {
        }.getType();
        json = editor.getString("team1", "");
        ArrayList<Ball> team1 = gson.fromJson(json, type);

        json = editor.getString("team2", "");
        ArrayList<Ball> team2 = gson.fromJson(json, type);


        mGameView.setBall(ball);
        //    Log.i("EEE", mGameView.getBall().getSpeed()+ "");
        mGameView.setTeam1(team1);
        mGameView.setTeam2(team2);

        mGameView.setPlayer(editor.getInt("player", 0));

        int goals[] = new int[2];

        goals[0] = editor.getInt("g1", 0);
        goals[1] = editor.getInt("g2", 0);

        mGameView.setGoals(goals);

        name1 = editor.getString("n1", "");
        name2 = editor.getString("n2", "");

        mGameView.setBackg(backg = editor.getInt("backg", 0));

        flag1 = editor.getInt("f1", 0);
        flag2 = editor.getInt("f2", 0);

        mGameView.setFlags(flag1, flag2);

        int p[] = new int[2];


        p[0] = p1 = editor.getInt("p1", 0);
        p[1] = p2 = editor.getInt("p2", 0);

        endval = editor.getInt("endval", 0);
        mode = editor.getInt("mode", 0);
        speed = editor.getFloat("speed", 0);

        mController = new Controller(mGameView, this, mode, endval, speed, p1, p2);

        mController.setT(editor.getLong("t", 0));
        mController.setTime(editor.getInt("ti", 0));
        mController.setSleep(editor.getInt("sl", 0));
        mController.setGoal(editor.getBoolean("go", false));

        mGameView.setOnTouchListener(new TouchListener(mGameView, mController));

        mController.start();
    }

    @Override
    public void onBackPressed() {
        saveGame();
        super.onBackPressed();
    }

    @Override
    protected void onUserLeaveHint() {
        saveGame();
        super.onUserLeaveHint();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
}
