package com.example.milos.pocketsoccer.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.milos.pocketsoccer.MainActivity;
import com.example.milos.pocketsoccer.R;
import com.example.milos.pocketsoccer.game.GameActivity;


public class SettingsFragment extends Fragment {

    private int mBacks[] = {R.drawable.field1,R.drawable.field2,R.drawable.field3,R.drawable.field4};

    private ImageButton mLeft1, mRight1;

    private View mView1;

    private MainActivity mActivity;

    private SeekBar seekBarSpeed;
    private SeekBar seekBarEnd;

    private TextView texSpeed;
    private TextView textEnd;
    private TextView back;
    private TextView reset;

    private RadioButton rb1, rb2;
    private int tek = 0;

    private int mode = 0;
    private int time = 60;
    private int score = 3;
    private double speed = 1;
    private int mProgress = 50;
    private int mPrgoressSp = 50;


    public void setmActivity(Activity mActivity) {
        this.mActivity = (MainActivity) mActivity;
    }

    private void drawView(View view, int tek) {
        view.setBackgroundResource(mBacks[tek]);
    }

    public void readFromRef() {
        SharedPreferences editor = mActivity.getPreferences(Context.MODE_PRIVATE);
        mode = editor.getInt("Mode", mode);
        mProgress = editor.getInt("Prog", mProgress);
        mPrgoressSp = editor.getInt("Speed", mPrgoressSp);
        tek = editor.getInt("Back", tek);

        score = (int) ((mProgress / 100.0) * 4.0) + 1;
        time = (int) ((mProgress / 100.0) * 90.0) + 15;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings_fragment, container, false);

        mLeft1 = view.findViewById(R.id.left1);
        mView1 = view.findViewById(R.id.view1);
        mRight1 = view.findViewById(R.id.right1);


        readFromRef();
        drawView(mView1, tek);

        mLeft1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tek--;
                if (tek == -1)
                    tek = mBacks.length - 1;

                drawView(mView1, tek);
                writeToSharedRef();

            }
        });

        mRight1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tek++;
                tek %= mBacks.length;
                drawView(mView1, tek);
                writeToSharedRef();
            }
        });

        back = view.findViewById(R.id.backset);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.mainMenu();
            }
        });

        reset = view.findViewById(R.id.resettv);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences editor = getActivity().getPreferences(Context.MODE_PRIVATE);

                mode = editor.getInt("defMode", mode);
                mProgress = editor.getInt("defProg", mProgress);
                mPrgoressSp = editor.getInt("defSpeed", mPrgoressSp);
                tek = editor.getInt("defBack", tek);
                drawView(mView1, tek);

                if (mode == 0) {
                    rb1.setChecked(true);
                } else {
                    rb2.setChecked(true);
                }

                seekBarSpeed.setProgress(mPrgoressSp);
                seekBarEnd.setProgress(mProgress);

                score = (int) ((mProgress / 100.0) * 4.0) + 1;
                time = (int) ((mProgress / 100.0) * 90.0) + 15;

                if (mode == 0) {
                    textEnd.setText("Score: " + score + " goals");
                } else {
                    textEnd.setText("Time: " + time + " seconds");
                }

                speed = mPrgoressSp / 100.0 + 0.5;
                texSpeed.setText("Speed: " + speed);


            }
        });

        textEnd = view.findViewById(R.id.tvend);

        seekBarEnd = view.findViewById(R.id.seekBarend);

        seekBarEnd.setProgress(mProgress);
        seekBarEnd.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                score = (int) ((progress / 100.0) * 4.0) + 1;
                time = (int) ((progress / 100.0) * 90.0) + 15;

                if (mode == 0) {
                    textEnd.setText("Score: " + score + " goals");
                } else {
                    textEnd.setText("Time: " + time + " seconds");
                }
                mProgress = progress;
                writeToSharedRef();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        texSpeed = view.findViewById(R.id.tvsp);
        seekBarSpeed = view.findViewById(R.id.seekBarsp);
        seekBarSpeed.setProgress(mPrgoressSp);
        seekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speed = progress / 100.0 + 0.5;
                texSpeed.setText("Speed: " + speed);
                mPrgoressSp = progress;
                writeToSharedRef();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        rb1 = view.findViewById(R.id.radioscore);
        rb2 = view.findViewById(R.id.radiotime);


        rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mode = 0;
                }
                score = (int) ((mProgress / 100.0) * 4.0) + 1;
                time = (int) ((mProgress / 100.0) * 90.0) + 15;

                if (mode == 0) {
                    textEnd.setText("Score: " + score + " goals");
                } else {
                    textEnd.setText("Time: " + time + " seconds");
                }
                writeToSharedRef();
            }
        });

        rb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mode = 1;
                }
                score = (int) ((mProgress / 100.0) * 4.0) + 1;
                time = (int) ((mProgress / 100.0) * 90.0) + 15;

                if (mode == 0) {
                    textEnd.setText("Score: " + score + " goals");
                } else {
                    textEnd.setText("Time: " + time + " seconds");
                }

                writeToSharedRef();
            }
        });


        if (mode == 0) {
            rb1.setChecked(true);
        } else {
            rb2.setChecked(true);
        }

        score = (int) ((mProgress / 100.0) * 4.0) + 1;
        time = (int) ((mProgress / 100.0) * 90.0) + 15;

        if (mode == 0) {
            textEnd.setText("Score: " + score + " goals");
        } else {
            textEnd.setText("Time: " + time + " seconds");
        }

        speed = mPrgoressSp / 100.0 + 0.5;
        texSpeed.setText("Speed: " + speed);

        return view;
    }

    public int getMode() {
        return mode;
    }

    public int getEndTreshold() {
        if (mode == 0) {
            return score;
        }
        return time;
    }

    public double getSpeed() {
        return speed;
    }

    public void writeToSharedRef() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("defMode", 0);
        editor.putInt("defProg", 50);
        editor.putInt("defSpeed", 50);
        editor.putInt("defBack", 0);
        editor.putInt("Mode", mode);
        editor.putInt("Prog", mProgress);
        editor.putInt("Speed", mPrgoressSp);
        editor.putInt("Back", tek);
        editor.commit();
    }


    public int getBackg() {
        return tek;
    }
}

