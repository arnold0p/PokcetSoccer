package com.example.milos.pocketsoccer.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.milos.pocketsoccer.database.entity.Game;

import java.util.List;

@Dao
public interface GameDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertGame(Game game);

    @Query("SELECT * FROM game_table WHERE (player1=:player1 AND player2=:player2) or (player1=:player2 and player2=:player1)")
    LiveData<List<Game>> getGamesForPlayers(String player1,String player2);

    @Query("DELETE FROM game_table WHERE (player1=:player1 AND player2=:player2) or (player1=:player2 and player2=:player1)")
    void deleteGamesForPlayers(String player1,String player2);

    @Query("DELETE FROM game_table")
    void deleteAllGames();


}
