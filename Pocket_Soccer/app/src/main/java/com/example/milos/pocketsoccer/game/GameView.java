package com.example.milos.pocketsoccer.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import com.example.milos.pocketsoccer.R;

import java.util.ArrayList;

public class GameView extends AppCompatImageView {

    private int mFlags[] = {R.drawable.t1,
            R.drawable.t2,
            R.drawable.t3,
            R.drawable.t4,
            R.drawable.t5,
            R.drawable.t6,
            R.drawable.t7,
            R.drawable.t8,
            R.drawable.t9,
            R.drawable.t10,
            R.drawable.t11,
            R.drawable.t12,
            R.drawable.t13,
            R.drawable.t14,
            R.drawable.t15,
            R.drawable.t16,
            R.drawable.t17,
            R.drawable.t18,
            R.drawable.t19,
            R.drawable.t20,
            R.drawable.t21,
            R.drawable.t22,
            R.drawable.t23,
            R.drawable.t24,
            R.drawable.t26,
            R.drawable.t27,
            R.drawable.t28,
            R.drawable.t29,
            R.drawable.t30,
            R.drawable.t31,
            R.drawable.t32,
            R.drawable.t33,
            R.drawable.t34,
            R.drawable.t35,
            R.drawable.t36,
            R.drawable.t37,
            R.drawable.t38,
            R.drawable.t39,
            R.drawable.t60,
            R.drawable.t61,
            R.drawable.t62,
            R.drawable.t63,
            R.drawable.t64,
            R.drawable.t65,
            R.drawable.t66,
            R.drawable.t67,
            R.drawable.t68,
            R.drawable.t69,
            R.drawable.t70,
    };
    private int mBacks[] = {R.drawable.field1, R.drawable.field2, R.drawable.field3, R.drawable.field4};

    private final int smallr = 70;
    private final int bigr = 150;

    private Paint mPaint;
    private Paint[] mPaintp;

    private Bitmap background;

    private Bitmap flag1, flag2, ballpic;

    private Ball ball;

    private PointF stativaLeftDown, stativaLeftUp, stativaRightDown, stativaRightUp;

    private int goal[];
    boolean showResult = false;

    ArrayList<Ball> team1, team2;

    private int player = 0;
    private boolean resume = false;


    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public ArrayList<Ball> getTeam1() {
        return team1;
    }

    public void setTeam1(ArrayList<Ball> team1) {
        this.team1 = team1;
    }

    public ArrayList<Ball> getTeam2() {
        return team2;
    }

    public void setTeam2(ArrayList<Ball> team2) {
        this.team2 = team2;
    }

    private int time = -1;

    private int f1, f2, b;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        mPaint = new Paint();
        mPaintp = new Paint[2];
        mPaintp[0] = new Paint();
        mPaintp[1] = new Paint();
        // background = BitmapFactory.decodeResource(getResources(), R.drawable.field1);
        //flag1 = BitmapFactory.decodeResource(getResources(), R.drawable.brazil);
        //flag2 = BitmapFactory.decodeResource(getResources(), R.drawable.canada);
        //ballpic = BitmapFactory.decodeResource(getResources(), R.drawable.ball);

        team1 = new ArrayList<>(3);
        team2 = new ArrayList<>(3);

        goal = new int[2];
        goal[0] = goal[1] = 0;
        mPaintp[1].setAlpha(170);
        ballpic = BitmapFactory.decodeResource(getResources(), R.drawable.soccerball);
        ballpic = Bitmap.createScaledBitmap(ballpic, smallr, smallr, false);
    }

    public void setFlags(int f1, int f2) {
        this.f1 = f1;
        this.f2 = f2;
        flag1 = BitmapFactory.decodeResource(getResources(), mFlags[f1]);
        flag2 = BitmapFactory.decodeResource(getResources(), mFlags[f2]);
        flag1 = Bitmap.createScaledBitmap(flag1, bigr, bigr, false);
        flag2 = Bitmap.createScaledBitmap(flag2, bigr, bigr, false);

    }

    public void setBackg(int b) {
        this.b = b;
        background = BitmapFactory.decodeResource(getResources(), mBacks[b]);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(background, 0, 0, mPaint);
        float x, y;
        int r;
        for (int i = 0; i < 3; i++) {
            x = team1.get(i).getCenter().x;
            y = team1.get(i).getCenter().y;
            r = team1.get(i).getR();
            canvas.drawBitmap(flag1, x - r / 2, y - r / 2, mPaintp[0]);

            x = team2.get(i).getCenter().x;
            y = team2.get(i).getCenter().y;
            r = team2.get(i).getR();
            canvas.drawBitmap(flag2, x - r / 2, y - r / 2, mPaintp[1]);
        }

        x = ball.getCenter().x;
        y = ball.getCenter().y;
        r = ball.getR();
        canvas.drawBitmap(ballpic, x - r / 2, y - r / 2, mPaint);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, stativaLeftDown.y, stativaLeftDown.x, stativaLeftDown.y + 30, paint);
        canvas.drawRect(0, stativaLeftUp.y, stativaLeftUp.x, stativaLeftUp.y + 30, paint);

        canvas.drawRect(stativaRightDown.x, stativaRightDown.y, getWidth(), stativaRightDown.y + 30, paint);
        canvas.drawRect(stativaRightUp.x, stativaRightUp.y, getWidth(), stativaRightUp.y + 30, paint);


        if (showResult) {

            paint = new Paint();
            paint.setColor(Color.argb(200, 0, 0, 0));
            canvas.drawRect(getWidth() / 4, getHeight() / 4, getWidth() - getWidth() / 4, getHeight() - getHeight() / 4, paint);

            paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(300);
            canvas.drawText(goal[0] + " : " + goal[1], getWidth() / 2 - 277, getHeight() / 2 + 100, paint);
        }

        if (time >= 0) {
            paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.BLACK);
            paint.setAlpha(150);
            canvas.drawText(" Time: " + time, getWidth() / 2 - 250, 100, paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        background = Bitmap.createScaledBitmap(background, getWidth(), getHeight(), false);

        flag1 = Bitmap.createScaledBitmap(flag1, bigr, bigr, false);
        flag2 = Bitmap.createScaledBitmap(flag2, bigr, bigr, false);
        ballpic = Bitmap.createScaledBitmap(ballpic, smallr, smallr, false);

        if (!resume) {
            ball = new Ball(new PointF(getWidth() / 2, getHeight() / 2), smallr);
            team1.clear();
            team2.clear();
            team1.add(new Ball(new PointF(getWidth() / 4, getHeight() / 4), bigr));
            team1.add(new Ball(new PointF(getWidth() / 4, getHeight() / 4 + getHeight() / 2), bigr));
            team1.add(new Ball(new PointF(getWidth() / 2 - getWidth() / 8, getHeight() / 2), bigr));

            team2.add(new Ball(new PointF(getWidth() / 2 + getWidth() / 4, getHeight() / 2 + getHeight() / 4), bigr));
            team2.add(new Ball(new PointF(getWidth() / 2 + getWidth() / 4, getHeight() / 4), bigr));
            team2.add(new Ball(new PointF(getWidth() / 2 + getWidth() / 8, getHeight() / 2), bigr));
        }

        stativaLeftDown = new PointF(0 + 200, getHeight() / 2 + 200);
        stativaLeftUp = new PointF(0 + 200, getHeight() / 2 - 200);

        stativaRightDown = new PointF(getWidth() - 200, getHeight() / 2 + 200);
        stativaRightUp = new PointF(getWidth() - 200, getHeight() / 2 - 200);
    }

    public void reset() {
        team1.clear();
        team2.clear();

        team1.add(new Ball(new PointF(getWidth() / 4, getHeight() / 4), bigr));
        team1.add(new Ball(new PointF(getWidth() / 4, getHeight() / 4 + getHeight() / 2), bigr));
        team1.add(new Ball(new PointF(getWidth() / 2 - getWidth() / 8, getHeight() / 2), bigr));

        team2.add(new Ball(new PointF(getWidth() / 2 + getWidth() / 4, getHeight() / 2 + getHeight() / 4), bigr));
        team2.add(new Ball(new PointF(getWidth() / 2 + getWidth() / 4, getHeight() / 4), bigr));
        team2.add(new Ball(new PointF(getWidth() / 2 + getWidth() / 8, getHeight() / 2), bigr));


        ball = new Ball(new PointF(getWidth() / 2, getHeight() / 2), smallr);
    }


    public void goal(int i) {
        goal[i]++;
        reset();
        showResult = true;
        invalidate();
    }

    public void goalUnpause() {
        showResult = false;
    }

    public void togglePlayer() {
        mPaintp[player].setAlpha(170);
        player = (player + 1) % 2;
        mPaintp[player].setAlpha(250);
    }

    public int getPlayer() {
        return player;
    }

    public void setTime(int t) {
        time = t;
    }

    public int[] goals() {
        return goal;
    }

    public void setPlayer(int p) {
        player = p;
        togglePlayer();
        togglePlayer();
    }

    public void setGoals(int[] g) {
        goal = g;
        this.resume = true;
    }

}
