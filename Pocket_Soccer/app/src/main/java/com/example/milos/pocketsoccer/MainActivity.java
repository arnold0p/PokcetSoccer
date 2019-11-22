package com.example.milos.pocketsoccer;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.milos.pocketsoccer.database.entity.Game;
import com.example.milos.pocketsoccer.database.entity.Stats;
import com.example.milos.pocketsoccer.fragments.ChooseFragment;
import com.example.milos.pocketsoccer.fragments.GameStats;
import com.example.milos.pocketsoccer.fragments.SettingsFragment;
import com.example.milos.pocketsoccer.fragments.StartFragment;
import com.example.milos.pocketsoccer.fragments.StatsFragment;
import com.example.milos.pocketsoccer.viewmodel.MyViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private MyViewModel mMyViewModel;

    private StartFragment mStartFragment;
    private StatsFragment mStatsFragment;
    private ChooseFragment mChooseFragment;

    private GameStats mGamesStatsFragment;

    private SettingsFragment mSettingsFragment;

    private List<Stats> mStats;

    private Sensor gravitySensor;
    private Sensor accelerometerSensor;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private int countUp = 0;
    private int countDown = 0;
    private int state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mMyViewModel = ViewModelProviders.of(this).get(MyViewModel.class);

        mStartFragment = new StartFragment();
        mStartFragment.setmActivity(this);

        mStatsFragment = new StatsFragment();
        mStatsFragment.setmActivity(this);

        mChooseFragment = new ChooseFragment();
        mChooseFragment.setmActivity(this);

        mGamesStatsFragment = new GameStats();
        mGamesStatsFragment.setmActivity(this);

        mSettingsFragment = new SettingsFragment();
        mSettingsFragment.setmActivity(this);
        mSettingsFragment.readFromRef();
        getStats();

        mainMenu();
    }

    private void getStats(){
        mMyViewModel.getAllStats().observe(this, new Observer<List<Stats>>() {
            @Override
            public void onChanged(@Nullable List<Stats> stats) {
                mStatsFragment.setStats(stats);
                mStats = stats;
            }
        });
    }

    public void statsShow() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentplace, mStatsFragment);
        fragmentTransaction.commit();
        getStats();
        state = 1;
    }

    public void mainMenu() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentplace, mStartFragment);
        fragmentTransaction.commit();
        state = 0;
        mGamesStatsFragment.setGames(null);
    }

    public void startChoose() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mChooseFragment.setTek();
        fragmentTransaction.replace(R.id.fragmentplace, mChooseFragment);
        fragmentTransaction.commit();
        state = 1;
    }

    public void showGames(final String p1, final String p2, int w1, int w2) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mGamesStatsFragment.setWins(p1, p2, w1, w2);
        mMyViewModel.getAllGamesForPlayers(p1, p2).observe(this, new Observer<List<Game>>() {
            @Override
            public void onChanged(@Nullable List<Game> games) {
                mGamesStatsFragment.setGames(games);
           //     Toast.makeText(MainActivity.this, p1 + " " + p2,Toast.LENGTH_LONG).show();
            }
        });
        fragmentTransaction.replace(R.id.fragmentplace, mGamesStatsFragment);
        fragmentTransaction.commit();
        state = 1;
    }


    public void score(int goal1, int goal2, String name1, String name2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(new Date());
        Game game = new Game(name1, name2, goal1, goal2, dateString);
        mMyViewModel.insertGame(game);
        int i;
        Stats s = null;
        for (i = 0; i < mStats.size(); i++) {
            s = mStats.get(i);
            if (name1.equals(s.getPlayer1()) && name2.equals(s.getPlayer2())) {
                break;
            }
            if (name2.equals(s.getPlayer1()) && name1.equals(s.getPlayer2())) {
                int tmp = goal1;
                goal1 = goal2;
                goal2 = tmp;
                break;
            }
        }
        int win1 = 0;
        int win2 = 0;

        if (goal1 > goal2) {
            win1++;
        }
        if (goal1 < goal2) {
            win2++;
        }
        if (i < mStats.size()) {
            win1 += s.getWin1();
            win2 += s.getWin2();
            mMyViewModel.updateStatsForPlayers(name1, name2, win1, win2);
        } else {
            Stats si = new Stats(name1, name2);
            si.setWin1(win1);
            si.setWin2(win2);
            mMyViewModel.insertStats(si);
        }

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("resume", 0);
        editor.commit();
        getStats();
        //Toast.makeText(this, name1 +" " + name2,Toast.LENGTH_LONG).show();
        showGames(name1, name2, win1, win2);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    public void deleteAllStats() {
        for (int i=0;i<mStats.size();i++){
            mMyViewModel.deleteGamesForPlayers(mStats.get(i).getPlayer1(), mStats.get(i).getPlayer2());
            mMyViewModel.deleteStatsForPlayers(mStats.get(i).getPlayer1(), mStats.get(i).getPlayer2());
        }
        //mMyViewModel.deleteAllStats();
        getStats();
    }

    public void deleteGames(String player1, String player2) {
        mMyViewModel.deleteGamesForPlayers(player1, player2);
        mMyViewModel.deleteStatsForPlayers(player1, player2);
        statsShow();
    }

    public void settings() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentplace, mSettingsFragment);
        fragmentTransaction.commit();
        state = 1;
    }

    public int getMode() {
        return mSettingsFragment.getMode();
    }

    public int getEndTreshold() {
        return mSettingsFragment.getEndTreshold();
    }

    public double getSpeed() {
        return mSettingsFragment.getSpeed();
    }

    public int getBackg() {
        return mSettingsFragment.getBackg();
    }

    public void violentBack() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("resume", 1);
        //Toast.makeText(this, "Game saved", Toast.LENGTH_SHORT).show();
        editor.commit();
        mainMenu();
    }

    public int getResume() {
        SharedPreferences editor = getPreferences(Context.MODE_PRIVATE);
        int r = editor.getInt("resume", 0);
        return r;
    }

    @Override
    public void onBackPressed() {
        if (state == 0) {
            super.onBackPressed();
        } else {
            mainMenu();
        }
    }







    private void activate() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocation();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == gravitySensor) {
            if (event.values.length > 2) {
                //setXY(values[0], values[1]);
                // -x, +y
            }
        }
        if (event.sensor == accelerometerSensor) {
            float value = (float) Math.sqrt(
                    event.values[0] * event.values[0] +
                            event.values[1] * event.values[1] +
                            event.values[2] * event.values[2]);

            if (value > accelerometerSensor.getMaximumRange() * 0.2f) {
                //Log.i("Test1","xxxxxxx");
                countUp++;
            } else {
                //Log.i("Test1","-------");
                countDown++;
            }
            if (countUp > 40) {
                //Log.i("Test1","Shaked");
                Toast.makeText(this, "SHAKED", Toast.LENGTH_SHORT).show();
                countDown = countUp = 0;
            }
            if (countDown > 5) {
                countUp = 0;
                countDown = 0;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            requestLocation();
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                List<Address> addressList = null;
                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                try {
                    addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addressList != null) {
                    Toast.makeText(MainActivity.this, addressList.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
