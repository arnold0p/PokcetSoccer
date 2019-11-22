package com.example.milos.pocketsoccer.database;


import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;

import com.example.milos.pocketsoccer.database.dao.GameDao;
import com.example.milos.pocketsoccer.database.dao.StatsDao;
import com.example.milos.pocketsoccer.database.entity.Game;
import com.example.milos.pocketsoccer.database.entity.Stats;


@Database(entities = {Stats.class,Game.class}, version = 2, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {

    private static MyDatabase instance;

    public abstract GameDao getGameDao();
    public abstract StatsDao getStatsDao();

    public static MyDatabase getInstance(Application application){

        synchronized (MyDatabase.class){
            if (instance==null){
                instance = Room.databaseBuilder(application,MyDatabase.class,"my_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(myCallback).build();
            }
        }

        return instance;
    }

    private static RoomDatabase.Callback myCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) { super.onOpen(db);
            new Thread(new Runnable() {
                @Override
                public void run() {
                   // instance.getStatsDao().deleteAllStats();
                  //  instance.getGameDao().deleteAllGames();
                  //  instance.getStatsDao().insertStats(new Stats("Matke","Jakov"));
//                    instance.getStatsDao().insertStats(new Stats("Cira","Hudi"));
//                    instance.getStatsDao().insertStats(new Stats("AAAAAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa","EEEEE"));
//                    instance.getStatsDao().insertStats(new Stats("Matke1","Jakov1"));
//                    instance.getStatsDao().insertStats(new Stats("Cira1","Hudi1"));
//                    instance.getStatsDao().insertStats(new Stats("Matke2","Jakov2"));
//                    instance.getStatsDao().insertStats(new Stats("Cira2","Hudi2"));
//                    instance.getStatsDao().insertStats(new Stats("dasd","das"));
//                    instance.getStatsDao().insertStats(new Stats("faf","Jakdsaov2"));
//                    instance.getStatsDao().insertStats(new Stats("Cirdsda2","Hudddi2"));
                }
            }).start();
        }
    };
}
