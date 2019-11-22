package com.example.milos.pocketsoccer.controllers;

import android.graphics.PointF;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.milos.pocketsoccer.R;
import com.example.milos.pocketsoccer.database.entity.Game;
import com.example.milos.pocketsoccer.game.Ball;
import com.example.milos.pocketsoccer.game.GameActivity;
import com.example.milos.pocketsoccer.game.GameView;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    private GameView mGameView;
    private Nit nit;
    private GameActivity gameActivity;

    private int time = 3;
    private long t = 0;

    private int mode, endval;

    private double speed;
    private int p[];

    private int sleep = -1;
    private int fcnt = 0;

    static MediaPlayer mediaPlayerCrowd;
    static MediaPlayer mediaPlayerWhistle;
    static MediaPlayer mediaPlayerBounce;

    public Controller(final GameView mGameView, final GameActivity gameActivity, int mode, int endval, double speed
            , int p1, int p2) {
        this.gameActivity = gameActivity;
        this.mode = mode;
        this.endval = endval;
        this.speed = speed;
        this.p = new int[2];
        this.p[0] = p1;
        this.p[1] = p2;
        this.mGameView = mGameView;
        nit = new Nit("nit", new Hand(mGameView, this));
        //NitPost nit = new NitPost();
        //nit.setGameView(mGameView);
        //nit.start();

        if (mediaPlayerBounce == null) {
            mediaPlayerCrowd = MediaPlayer.create(gameActivity, R.raw.crowd);
            mediaPlayerBounce = MediaPlayer.create(gameActivity, R.raw.bounce);
            mediaPlayerWhistle = MediaPlayer.create(gameActivity, R.raw.whistle);
        }

    }

    public void end() {
        nit.interrupt();
        nit.prekini();
//        mediaPlayerCrowd.release();
//        mediaPlayerBounce.release();
//        mediaPlayerWhistle.release();
//
//        mediaPlayerCrowd = mediaPlayerBounce = mediaPlayerWhistle = null;
    }

    public void start() {
        nit.start();
    }

    public static class Nit extends HandlerThread {
        private Handler handler;

        private boolean work;

        public Nit(String name, Handler handler) {
            super(name);
            this.handler = handler;
            work = true;
        }

        @Override
        public void run() {
            long time = System.currentTimeMillis();
            while (work) {
                if (System.currentTimeMillis() - time > 20) {

                    handler.sendEmptyMessage(0);
                    time = System.currentTimeMillis();
                    //Log.i("Test", "eeee");
                }
            }
        }

        public void prekini() {
            work = false;
        }
    }

    public static class Hand extends Handler {
        private GameView gameView;
        private Controller controller;
        private long t;

        public Hand(GameView gameView, Controller controller) {
            this.gameView = gameView;
            this.controller = controller;
        }

        private boolean checkCollision(Ball ball, Ball ball1) {
            float ballx = ball.getCenter().x;
            float bally = ball.getCenter().y;
            float ballr = ball.getR() / 2;

            float ball1x = ball1.getCenter().x;
            float ball1y = ball1.getCenter().y;
            float ball1r = ball1.getR() / 2;

            float xd = ballx - ball1x;
            float yd = bally - ball1y;

            float sumRadius = ballr + ball1r;
            float distSqrt = (float) Math.sqrt((xd * xd) + (yd * yd));

            if (distSqrt > sumRadius) {
                return false;
            }

            float dx = ball.getDx();
            float dy = ball.getDy();
            float dx1 = ball1.getDx();
            float dy1 = ball1.getDy();

            dx = dx * ball.getSpeed();
            dy = dy * ball.getSpeed();

            dx1 = dx1 * ball1.getSpeed();
            dy1 = dy1 * ball1.getSpeed();

            float d = (float) Math.sqrt(Math.pow(ballx - ball1x, 2) + Math.pow(bally - ball1y, 2));
            float nx = (ball1x - ballx) / d;
            float ny = (ball1y - bally) / d;
            float p = 2 * (dx * nx + dy * ny - dx1 * nx - dy1 * ny) /
                    (ballr + ball1r);

            if (ball1r == 15) {
                ball1r = ballr;
            }

            dx = dx - p * ball1r * nx;
            dy = dy - p * ball1r * ny;
            dx1 = dx1 + p * ballr * nx;
            dy1 = dy1 + p * ballr * ny;


            ball.setDx(dx);
            ball.setDy(dy);
            ball.scaleSpeed();

            ball1.setDx(dx1);
            ball1.setDy(dy1);
            ball1.scaleSpeed();

            float vecx = ballx - ball1x;
            float vecy = bally - ball1y;

            float scaledDistance = 1.01f - (distSqrt / sumRadius);

            ball.getCenter().x += vecx * scaledDistance;
            ball.getCenter().y += vecy * scaledDistance;
            return true;
        }

        @Override
        public void handleMessage(Message msg) {

            if (System.currentTimeMillis() - controller.t > 1000 && !controller.goal) {
                controller.time--;
                if (controller.sleep > 0) {
                    controller.sleep--;
                }
                if (controller.sleep == 0) {
                    controller.launch();
                    controller.sleep = -1;
                    gameView.togglePlayer();
                    controller.checkComputer();
                    controller.time = 3;
                } else if (controller.time == 0) {
                    gameView.togglePlayer();
                    controller.checkComputer();
                    controller.time = 3;
                    controller.downplayer = -1;
                }
                controller.t = System.currentTimeMillis();

                if (controller.mode == 1) {
                    controller.endval--;
                    gameView.setTime(controller.endval);
                    if (controller.endval == 0) {
                        controller.gameActivity.end();
                        controller.end();
                    }
                }


            }
            List<Ball> team1 = gameView.getTeam1();
            List<Ball> team2 = gameView.getTeam2();

            ArrayList<Ball> allteams = new ArrayList<>();
            for (int k = 0; k < team1.size(); k++) {
                allteams.add(team1.get(k));
            }

            for (int k = 0; k < team2.size(); k++) {
                allteams.add(team2.get(k));
            }

            Ball ball = gameView.getBall();

            if (ball == null)
                return;

            if (checkGoal()) {
                controller.mediaPlayerCrowd.seekTo(0);
                controller.mediaPlayerCrowd.start();
                controller.goal = true;
                t = System.currentTimeMillis();
                return;
            }
            if (controller.goal) {
                if (System.currentTimeMillis() - t < 2000) {
                    return;
                }
                controller.mediaPlayerWhistle.seekTo(0);
                controller.mediaPlayerWhistle.start();
                controller.goal = false;
                gameView.goalUnpause();
            }

            //move balls
            for (int i = 0; i < allteams.size(); i++) {
                allteams.get(i).move(controller.speed);
            }

            ball.move(controller.speed);

            //checking balls and walls
            for (int i = 0; i < allteams.size(); i++) {
                checkBallWall(allteams.get(i));
                checkBallStativa(allteams.get(i));
            }

            checkBallWall(ball);
            checkBallStativa(ball);

            for (int i = 0; i < allteams.size(); i++) {
                // left down
                checkCollision(allteams.get(i), new Ball(new PointF(185, gameView.getHeight() / 2 + 215), 30));
                // left up
                checkCollision(allteams.get(i), new Ball(new PointF(185, gameView.getHeight() / 2 - 185), 30));
                // right down
                checkCollision(allteams.get(i), new Ball(new PointF(gameView.getWidth() - 185, gameView.getHeight() / 2 + 215), 30));
                // right u[
                checkCollision(allteams.get(i), new Ball(new PointF(gameView.getWidth() - 185, gameView.getHeight() / 2 - 185), 30));
            }

            // left down
            checkCollision(ball, new Ball(new PointF(185, gameView.getHeight() / 2 + 215), 30));
            // left up
            checkCollision(ball, new Ball(new PointF(185, gameView.getHeight() / 2 - 185), 30));
            // right down
            checkCollision(ball, new Ball(new PointF(gameView.getWidth() - 185, gameView.getHeight() / 2 + 215), 30));
            // right u[
            checkCollision(ball, new Ball(new PointF(gameView.getWidth() - 185, gameView.getHeight() / 2 - 185), 30));


            for (int i = 0; i < allteams.size() - 1; i++) {
                for (int j = i + 1; j < allteams.size(); j++) {
                    if (checkCollision(allteams.get(i), allteams.get(j))) {
                        controller.mediaPlayerBounce.seekTo(0);
                        controller.mediaPlayerBounce.start();
                    }
                }
            }
            for (int i = 0; i < allteams.size(); i++) {
                if (checkCollision(allteams.get(i), ball)) {
                    controller.mediaPlayerBounce.seekTo(0);
                    controller.mediaPlayerBounce.start();
                }
            }

            // slow speed
            for (int i = 0; i < allteams.size(); i++) {
                allteams.get(i).reduceSpeed();
            }
            ball.reduceSpeed();

            gameView.invalidate();
        }

        private boolean checkGoal() {
            Ball ball = gameView.getBall();

            // left goal
            if (ball.getCenter().x + ball.getR() / 2 < 200) {
                if (ball.getCenter().y < gameView.getHeight() / 2 + 200 && ball.getCenter().y > gameView.getHeight() / 2 - 170) {
                    gameView.goal(1);
                    if (controller.mode == 0 && controller.mGameView.goals()[1] == controller.endval) {
                        controller.gameActivity.end();
                        controller.end();
                    }
                    return true;
                }
            }

            // right goal
            if (ball.getCenter().x - ball.getR() / 2 > gameView.getWidth() - 200) {
                if (ball.getCenter().y < gameView.getHeight() / 2 + 200 && ball.getCenter().y > gameView.getHeight() / 2 - 170) {
                    gameView.goal(0);
                    if (controller.mode == 0 && controller.mGameView.goals()[0] == controller.endval) {
                        controller.gameActivity.end();
                        controller.end();
                    }
                    return true;
                }
            }
            return false;
        }

        private void checkBallStativa(Ball ball) {
            float x = gameView.getWidth();
            float y = gameView.getHeight();

            float ballx = ball.getCenter().x;
            float bally = ball.getCenter().y;
            float ballr = ball.getR() / 2;
            float dx = ball.getDx();
            float dy = ball.getDy();

            float speed = ball.getSpeed();
            PointF newcenter;

            if ((ballx) <= 200) {    //left
                if ((bally - ballr) <= y / 2 + 230 && (bally - ballr - dy * speed) > y / 2 + 230) {    //down
                    bally = y / 2 + 230 + ballr + 1;
                    ball.setDy(-ball.getDy());

                    newcenter = new PointF(ballx, bally);
                    ball.setCenter(newcenter);
                }

                if ((bally + ballr - dy * speed) < y / 2 + 200 && (bally + ballr) >= y / 2 + 200) {    //down
                    bally = y / 2 + 200 - ballr - 1;
                    ball.setDy(-ball.getDy());

                    newcenter = new PointF(ballx, bally);
                    ball.setCenter(newcenter);
                }


                if ((bally - ballr) <= y / 2 - 170 && (bally - ballr - dy * speed) > y / 2 - 170) {    //up
                    bally = y / 2 - 170 + ballr + 1;
                    ball.setDy(-ball.getDy());

                    newcenter = new PointF(ballx, bally);
                    ball.setCenter(newcenter);
                }

                if ((bally + ballr - dy * speed) < y / 2 - 200 && (bally + ballr) >= y / 2 - 200) {    //up
                    bally = y / 2 - 200 - ballr - 1;
                    ball.setDy(-ball.getDy());

                    newcenter = new PointF(ballx, bally);
                    ball.setCenter(newcenter);
                }

            }

            if ((ballx) >= x - 200) {    //right
                if ((bally - ballr) <= y / 2 + 230 && (bally - ballr - dy * speed) > y / 2 + 230) {    //down
                    bally = y / 2 + 230 + ballr + 1;
                    ball.setDy(-ball.getDy());

                    newcenter = new PointF(ballx, bally);
                    ball.setCenter(newcenter);
                }

                if ((bally + ballr - dy * speed) < y / 2 + 200 && (bally + ballr) >= y / 2 + 200) {    //down
                    bally = y / 2 + 200 - ballr - 1;
                    ball.setDy(-ball.getDy());

                    newcenter = new PointF(ballx, bally);
                    ball.setCenter(newcenter);
                }


                if ((bally - ballr) <= y / 2 - 170 && (bally - ballr - dy * speed) > y / 2 - 170) {    //up
                    bally = y / 2 - 170 + ballr + 1;
                    ball.setDy(-ball.getDy());

                    newcenter = new PointF(ballx, bally);
                    ball.setCenter(newcenter);
                }

                if ((bally + ballr - dy * speed) < y / 2 - 200 && (bally + ballr) >= y / 2 - 200) {    //up
                    bally = y / 2 - 200 - ballr - 1;
                    ball.setDy(-ball.getDy());

                    newcenter = new PointF(ballx, bally);
                    ball.setCenter(newcenter);
                }
            }
        }

        private void checkBallWall(Ball ball) {
            float x = gameView.getWidth();
            float y = gameView.getHeight();

            float ballx = ball.getCenter().x;
            float bally = ball.getCenter().y;
            float ballr = ball.getR() / 2;

            PointF newcenter;

            if ((ballx - ballr) <= 0f) {    //left
                ballx = ballr + 1;
                ball.setDx(-ball.getDx());

                newcenter = new PointF(ballx, bally);
                ball.setCenter(newcenter);
            }

            if ((bally - ballr) <= 0f) {    //top
                bally = ballr + 1;
                ball.setDy(-ball.getDy());

                newcenter = new PointF(ballx, bally);
                ball.setCenter(newcenter);
            }

            if ((ballx + ballr) >= x) {    //right
                ballx = x - ballr - 1;
                ball.setDx(-ball.getDx());

                newcenter = new PointF(ballx, bally);
                ball.setCenter(newcenter);
            }

            if ((bally + ballr) >= y) {    //bottom
                bally = y - ballr - 1;
                ball.setDy(-ball.getDy());

                newcenter = new PointF(ballx, bally);
                ball.setCenter(newcenter);
            }
        }
    }


    public boolean goal = false;
    int downplayer = -1;
    float downx, downy;

    public void down(float x, float y) {

        if (goal) {
            return;
        }
        ArrayList<Ball> team1 = mGameView.getTeam1();
        ArrayList<Ball> team2 = mGameView.getTeam2();

        ArrayList<Ball> allteams = new ArrayList<>();

        if (mGameView.getPlayer() == 0 && p[0] == 0) {
            for (int k = 0; k < team1.size(); k++) {
                allteams.add(team1.get(k));
            }
        }

        if (mGameView.getPlayer() == 1 && p[1] == 0) {
            for (int k = 0; k < team2.size(); k++) {
                allteams.add(team2.get(k));
            }
        }

        for (int i = 0; i < allteams.size(); i++) {
            if (Math.sqrt(Math.pow(allteams.get(i).getCenter().x - x, 2) + Math.pow(allteams.get(i).getCenter().y - y, 2)) < allteams.get(i).getR() / 2) {
                downplayer = i;
                downx = x;
                downy = y;
                break;
            }
        }

    }

    public void move(float x, float y) {
    }

    public void up(float x, float y) {
        if (downplayer == -1) {
            return;
        }
        ArrayList<Ball> team1 = mGameView.getTeam1();
        ArrayList<Ball> team2 = mGameView.getTeam2();

        ArrayList<Ball> allteams = new ArrayList<>();

        if (mGameView.getPlayer() == 0) {
            for (int k = 0; k < team1.size(); k++) {
                allteams.add(team1.get(k));
            }
        }
        if (mGameView.getPlayer() == 1) {
            for (int k = 0; k < team2.size(); k++) {
                allteams.add(team2.get(k));
            }
        }

        Ball player = allteams.get(downplayer);

        player.setDx((x - player.getCenter().x));
        player.setDy((y - player.getCenter().y));
        player.scaleSpeed();

        downplayer = -1;

        mGameView.togglePlayer();
        checkComputer();
        time = 3;
    }

    private void checkComputer() {
        if (p[mGameView.getPlayer()] == 1) {
            sleep = (int) (Math.random() * 2);
        } else {
            sleep = -1;
        }
    }


    private void launch() {

        int randball = (int) (Math.random() * 3);

        Ball player = null;
        if (mGameView.getPlayer() == 0 && p[0] == 1) {
            player = mGameView.getTeam1().get(randball);
        }
        if (mGameView.getPlayer() == 1 && p[1] == 1) {
            player = mGameView.getTeam2().get(randball);
        }

        //    Log.i("Test",p[0] + " " + p[1]);
        Ball ball = mGameView.getBall();

        float x = ball.getCenter().x;
        float y = ball.getCenter().y;

        player.setDx((x - player.getCenter().x));
        player.setDy((y - player.getCenter().y));
        player.scaleSpeed();

    }

    public int[] getP() {
        return p;
    }

    public void setP(int[] p) {
        this.p = p;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public long getT() {
        return t;
    }

    public void setT(long t) {
        this.t = t;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getEndval() {
        return endval;
    }

    public void setEndval(int endval) {
        this.endval = endval;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getSleep() {
        return sleep;
    }

    public void setSleep(int sleep) {
        this.sleep = sleep;
    }

    public boolean isGoal() {
        return goal;
    }

    public void setGoal(boolean goal) {
        this.goal = goal;
    }


    public void whistle() {
        mediaPlayerWhistle.start();
    }

    public void pointerDown() {
        fcnt++;
        if (fcnt == 2) {
     //       Toast.makeText(gameActivity, fcnt + 1 + " Fingers", Toast.LENGTH_SHORT).show();
        }
    }

    public void pointerUp() {
        fcnt--;
    }

    //    private static class NitPost extends Thread {
//
//        private GameView gameView;
//
//        public void setGameView(GameView gameView) {
//            this.gameView = gameView;
//        }
//
//        @Override
//        public void run() {
//            while (true) {
//                try {
//                    sleep(50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                long time = System.currentTimeMillis();
//
//                while (System.currentTimeMillis() - time < 100) ;
//
//                ArrayList<Ball> team1 = gameView.getTeam1();
//                ArrayList<Ball> team2 = gameView.getTeam2();
//
//                ArrayList<Ball> allteams = new ArrayList<>();
//                for (int k = 0; k < team1.size(); k++) {
//                    allteams.add(team1.get(k));
//                }
//
//                for (int k = 0; k < team2.size(); k++) {
//                    allteams.add(team2.get(k));
//                }
//
//                Ball ball = gameView.getBall();
//
//                if (ball == null)
//                    return;
//
//                //move balls
//                for (int i = 0; i < allteams.size(); i++) {
//                    allteams.get(i).move();
//                }
//
//                ball.move();
//                //checking balls and walls
//                for (int i = 0; i < allteams.size(); i++) {
//                    checkBallWall(allteams.get(i));
//                }
//
//                // slow speed
//                for (int i = 0; i < allteams.size(); i++) {
//                    allteams.get(i).reduceSpeed();
//                }
//
//                ball.reduceSpeed();
//
//                gameView.postInvalidate();
//            }
//        }
//
//
//        private void checkBallWall(Ball ball) {
//            float x = gameView.getWidth();
//            float y = gameView.getHeight();
//
//            float ballx = ball.getCenter().x;
//            float bally = ball.getCenter().y;
//            float ballr = ball.getR() / 2;
//            float dx = ball.getDx();
//            float dy = ball.getDy();
//            PointF newcenter;
//
//            if ((ballx - ballr + dx) < 0) {    //left
//                ballx = ballr + 1;
//                ball.setDx(-ball.getDx());
//
//                newcenter = new PointF(ballx, bally);
//                ball.setCenter(newcenter);
//            }
//
//            if ((bally - ballr + dy) < 0) {    //top
//                bally = ballr + 1;
//                ball.setDy(-ball.getDy());
//
//                newcenter = new PointF(ballx, bally);
//                ball.setCenter(newcenter);
//            }
//
//            if ((ballx + ballr + dx) > x) {    //right
//                ballx = x - ballr - 1;
//                ball.setDx(-ball.getDx());
//
//                newcenter = new PointF(ballx, bally);
//                ball.setCenter(newcenter);
//            }
//
//            if ((bally + ballr + dy) > y) {    //bottom
//                bally = y - ballr - 1;
//                ball.setDy(-ball.getDy());
//
//                newcenter = new PointF(ballx, bally);
//                ball.setCenter(newcenter);
//            }
//        }
//    }

}
