package com.example.milos.pocketsoccer.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.milos.pocketsoccer.MainActivity;
import com.example.milos.pocketsoccer.R;
import com.example.milos.pocketsoccer.game.GameActivity;


public class ChooseFragment extends Fragment {

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

    private ImageButton mLeft1, mRight1, mLeft2, mRight2;

    private View mView1, mView2;

    private EditText et1, et2;

    private TextView mPlay;

    private MainActivity mActivity;

    private int mTek1, mTek2;
    private String name1, name2;

    private RadioButton rb1, rb2, rb3, rb4;

    private int p1 = 0, p2 = 0;

    public void setTek() {
        mTek1 = mTek2 = 20;
        if (mView1 != null) {
            drawView(mView1, mTek1);
            drawView(mView2, mTek2);
        }
    }

    public void setmActivity(Activity mActivity) {
        this.mActivity = (MainActivity) mActivity;
    }

    private void drawView(View view, int tek) {
        view.setBackgroundResource(mFlags[tek]);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose, container, false);

        mLeft1 = view.findViewById(R.id.left1);
        mView1 = view.findViewById(R.id.view1);
        mRight1 = view.findViewById(R.id.right1);

        mLeft2 = view.findViewById(R.id.left2);
        mView2 = view.findViewById(R.id.view2);
        mRight2 = view.findViewById(R.id.right2);

        mPlay = view.findViewById(R.id.playtv);

        et1 = view.findViewById(R.id.et1);
        et2 = view.findViewById(R.id.et2);

        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name1 = et1.getText().toString();
                name2 = et2.getText().toString();
                Intent intent = new Intent(mActivity, GameActivity.class);
                intent.putExtra("flag1", mTek1);
                intent.putExtra("flag2", mTek2);
                int mode = mActivity.getMode();
                int endval = mActivity.getEndTreshold();
                double speed = mActivity.getSpeed();
                int backg = mActivity.getBackg();
                intent.putExtra("mode", mode);
                intent.putExtra("endval", endval);
                intent.putExtra("speed", speed);
                intent.putExtra("backg", backg);
                intent.putExtra("p1", p1);
                intent.putExtra("p2", p2);
                intent.putExtra("n1", name1);
                intent.putExtra("n2", name2);
                intent.putExtra("resume", 0);
                startActivityForResult(intent, 1);
            }
        });

        mLeft1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTek1--;
                if (mTek1 == -1)
                    mTek1 = mFlags.length - 1;

                drawView(mView1, mTek1);

            }
        });

        mRight1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTek1++;
                mTek1 %= mFlags.length;
                drawView(mView1, mTek1);
            }
        });

        mLeft2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTek2--;
                if (mTek2 == -1)
                    mTek2 = mFlags.length - 1;

                drawView(mView2, mTek2);

            }
        });

        mRight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTek2++;
                mTek2 %= mFlags.length;

                drawView(mView2, mTek2);
            }
        });

        rb1 = view.findViewById(R.id.radio11);
        rb2 = view.findViewById(R.id.radio12);

        rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    p1 = 0;
                }
            }
        });

        rb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    p1 = 1;
                }
            }
        });

        rb3 = view.findViewById(R.id.radio21);
        rb4 = view.findViewById(R.id.radio22);

        rb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    p2 = 0;
                }
            }
        });

        rb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    p2 = 1;
                }

            }
        });

        rb1.setChecked(true);
        rb3.setChecked(true);


        mTek1 = mTek2 = 20;
        drawView(mView1, mTek1);
        drawView(mView2, mTek2);

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                int goal1 = data.getIntExtra("goal1", 0);
                int goal2 = data.getIntExtra("goal2", 0);
                int p1 = data.getIntExtra("p1", 0);
                int p2 = data.getIntExtra("p2", 0);
                String name1 = data.getStringExtra("name1");
                String name2 = data.getStringExtra("name2");

                if (p1 == 1) {
                    name1 = "!Robo1";
                }
                if (p2 == 1) {
                    name2 = "!Robo2";
                }
                mActivity.score(goal1, goal2, name1, name2);
                Log.i("EEE","kraj");
            } else {
                mActivity.violentBack();
            }
        }
    }
}