package com.example.milos.pocketsoccer.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.milos.pocketsoccer.database.MyDatabase;
import com.example.milos.pocketsoccer.database.dao.GameDao;
import com.example.milos.pocketsoccer.database.dao.StatsDao;
import com.example.milos.pocketsoccer.database.entity.Game;
import com.example.milos.pocketsoccer.database.entity.Stats;

import java.util.List;

public class Repository {

    private MyDatabase mMyDatabase;
    private StatsDao mStatsDao;
    private GameDao mGameDao;

    public Repository(Application application) {
        mMyDatabase = MyDatabase.getInstance(application);
        mStatsDao = mMyDatabase.getStatsDao();
        mGameDao = mMyDatabase.getGameDao();
    }

    public void insertStats(final Stats stats) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mStatsDao.insertStats(stats);
            }
        }).start();
    }

    public void updateStatsForPlayers(final String player1, final String player2, final int win1, final int win2) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mStatsDao.updateStats(win1, win2, player1, player2);
            }
        }).start();
    }

    public LiveData<List<Stats>> getAllStats() {
        return mStatsDao.getAllStats();
    }

    public void deleteAllStats() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mStatsDao.deleteAllStats();
            }
        }).start();
    }

    public void insertGame(final Game game) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mGameDao.insertGame(game);
            }
        }).start();
    }

    public LiveData<List<Game>> getGamesForPlayers(String player1, String player2) {
        return mGameDao.getGamesForPlayers(player1, player2);
    }


    public void deleteGamesForPlayers(final String player1, final String player2) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mGameDao.deleteGamesForPlayers(player1, player2);
            }
        }).start();
    }

    public void deleteStatsForPlayers(final String player1, final String player2) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mStatsDao.deleteStatsForPlayers(player1, player2);
            }
        }).start();
    }


}
