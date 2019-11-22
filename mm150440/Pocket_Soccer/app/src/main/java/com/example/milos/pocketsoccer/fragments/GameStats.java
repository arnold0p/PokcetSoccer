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
import com.example.milos.pocketsoccer.database.entity.Game;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameStats extends Fragment {


    private RecyclerView mRecyclerView;

    private TextView mTextBack, mTextGame;

    private MyAdapterGame mAdapter;

    private List<Game> games;

    private TextView mTextDelete;

    private String t = "All Duels";
    private String n1, n2;

    public void setGames(List<Game> games) {
        if (games == null) {
            return;
        }
        if (games.size() > 0 && ((games.get(0).getPlayer1().equals(n1) && games.get(0).getPlayer2().equals(n2)) ||
                (games.get(0).getPlayer1().equals(n2) && games.get(0).getPlayer2().equals(n1)))) {
            this.games = games;
            if (mAdapter != null && games != null) {
                mAdapter.setGames(games);
            }
        }
    }

    private MainActivity mActivity;

    public void setmActivity(Activity mActivity) {
        this.mActivity = (MainActivity) mActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game_stats, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerview);
        mAdapter = new MyAdapterGame(container.getContext());

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        mTextGame = view.findViewById(R.id.gamestats);
        mTextGame.setText(t);

        mTextBack = view.findViewById(R.id.bbackfromstats);

        mTextBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.statsShow();
                games = null;
            }
        });

        mTextDelete = view.findViewById(R.id.deletegames);

        mTextDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.deleteGames(games.get(0).getPlayer1(), games.get(0).getPlayer2());
            }
        });

        if (games != null) {
            mAdapter.setGames(games);
        }

        return view;
    }

    public void setWins(String n1, String n2, int w1, int w2) {
        this.n1 = n1;
        this.n2 = n2;
        t = "            All duels" + "\n" + n1 + "    " + w1 + " : " + w2 + "    " + n2;
    }

}
