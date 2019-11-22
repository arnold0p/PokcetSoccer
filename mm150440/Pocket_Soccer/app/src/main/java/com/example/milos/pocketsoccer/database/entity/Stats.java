package com.example.milos.pocketsoccer.database.entity;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "stats_table")
public class Stats {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String player1;

    private String player2;

    private int win1;

    private int win2;

    public Stats(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
        win1=0;
        win2=0;
    }

    public int getWin1() {
        return win1;
    }

    public void setWin1(int win1) {
        this.win1 = win1;
    }

    public int getWin2() {
        return win2;
    }

    public void setWin2(int win2) {
        this.win2 = win2;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
