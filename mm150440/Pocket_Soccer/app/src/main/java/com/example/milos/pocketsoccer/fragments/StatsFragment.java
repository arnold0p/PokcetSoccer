package com.example.milos.pocketsoccer.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.milos.pocketsoccer.MainActivity;
import com.example.milos.pocketsoccer.R;
import com.example.milos.pocketsoccer.database.entity.Stats;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatsFragment extends Fragment {


    private RecyclerView mRecyclerView;

    private TextView mTextBack, mTextDelete;

    private MyAdapterStats mAdapter;

    private List<Stats> stats;

    public void setStats(List<Stats> stats) {
        this.stats = stats;
        if (mAdapter != null)
            mAdapter.setStats(stats);
    }
    private static MainActivity mActivity;

    public void setmActivity(Activity mActivity) {
        this.mActivity = (MainActivity) mActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerview);
        mAdapter = new MyAdapterStats(container.getContext());

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        mTextBack = view.findViewById(R.id.bbackfromstats);

        mTextBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.mainMenu();
            }
        });

        mTextDelete = view.findViewById(R.id.deletestats);

        mTextDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.deleteAllStats();
            }
        });

        if (stats != null) {
            mAdapter.setStats(stats);
        }

        return view;
    }

    public static void showGames(String p1, String p2, int w1, int w2) {
        mActivity.showGames(p1, p2, w1, w2);
    }

}
