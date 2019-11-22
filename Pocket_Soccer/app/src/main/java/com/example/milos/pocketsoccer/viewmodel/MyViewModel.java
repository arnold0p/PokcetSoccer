package com.example.milos.pocketsoccer.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.example.milos.pocketsoccer.database.entity.Game;
import com.example.milos.pocketsoccer.database.entity.Stats;

import java.util.List;

public class MyViewModel extends AndroidViewModel {

    private Repository mRepository;


    public MyViewModel(@NonNull Application application) {
        super(application);
        this.mRepository = new Repository(application);
    }

    public void insertStats(Stats stats) {
        mRepository.insertStats(stats);
    }

    public LiveData<List<Stats>> getAllStats() {
        return mRepository.getAllStats();
    }

    public void deleteAllStats() {
        mRepository.deleteAllStats();
    }

    public void updateStatsForPlayers(final String player1, final String player2, final int win1, final int win2) {
        mRepository.updateStatsForPlayers(player1,player2,win1,win2);
    }

    public void insertGame(Game game) {
        mRepository.insertGame(game);
    }

    public LiveData<List<Game>> getAllGamesForPlayers(String player1, String player2) {
        return mRepository.getGamesForPlayers(player1, player2);
    }

    public void deleteGamesForPlayers(String player1, String player2) {
        mRepository.deleteGamesForPlayers(player1, player2);
    }
    public void deleteStatsForPlayers(String player1, String player2) {
        mRepository.deleteStatsForPlayers(player1, player2);
    }
}
