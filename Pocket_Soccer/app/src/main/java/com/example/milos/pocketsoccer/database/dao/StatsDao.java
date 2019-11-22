package com.example.milos.pocketsoccer.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.milos.pocketsoccer.database.entity.Stats;

import java.util.List;

@Dao
public interface StatsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertStats(Stats stats);

    @Query("SELECT * FROM stats_table")
    LiveData<List<Stats>> getAllStats();

    @Query("DELETE FROM stats_table")
    void deleteAllStats();

    @Query("UPDATE stats_table SET win1=:win1, win2=:win2 WHERE (player1=:player1 AND player2=:player2) or (player1=:player2 and player2=:player1)")
    void updateStats(int win1, int win2, String player1, String player2);

    @Query("DELETE FROM stats_table WHERE (player1=:player1 AND player2=:player2) or (player1=:player2 and player2=:player1)")
    void deleteStatsForPlayers(String player1,String player2);


}
