package com.example.milos.pocketsoccer.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.milos.pocketsoccer.game.GameActivity;
import com.example.milos.pocketsoccer.MainActivity;
import com.example.milos.pocketsoccer.R;

public class StartFragment extends Fragment {

    private TextView mTextStart;
    private TextView mTextContinue;
    private TextView mTextStats;
    private TextView mTextSettings;

    private MainActivity mActivity;

    public void setmActivity(Activity mActivity) {
        this.mActivity = (MainActivity) mActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.start_fragment,container,false);

        mTextStart = view.findViewById(R.id.starttv);
        mTextContinue = view.findViewById(R.id.continuetv);
        mTextStats = view.findViewById(R.id.statstv);
        mTextSettings = view.findViewById(R.id.settingstv);


        mTextStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.statsShow();
            }
        });

        mTextStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startChoose();
            }
        });


        mTextSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.settings();
            }
        });

        mTextContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivity.getResume() == 1){
                    Intent intent = new Intent(mActivity, GameActivity.class);
                    intent.putExtra("resume", 1);
                    startActivityForResult(intent, 1);
                }
            }
        });
        return  view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                int goal1 = data.getIntExtra("goal1", 0);
                int goal2 = data.getIntExtra("goal2", 0);
                String name1 = data.getStringExtra("name1");
                String name2 = data.getStringExtra("name2");
                int p1 = data.getIntExtra("p1", 0);;
                int p2 = data.getIntExtra("p2", 0);;
                if (p1 == 1) {
                    name1 = "!Robo1";
                }
                if (p2 == 1) {
                    name2 = "!Robo2";
                }
                mActivity.score(goal1, goal2, name1, name2);
            } else {
                mActivity.violentBack();
            }
        }
    }
}
