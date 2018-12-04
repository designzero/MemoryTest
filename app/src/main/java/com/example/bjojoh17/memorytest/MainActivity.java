package com.example.bjojoh17.memorytest;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    FragmentTransaction ft;

    public Fragment menuFragment;
    public Fragment menuFragmentDifficulty;
    public Fragment gameFragment;

    SoundPool sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    public int soundIds[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        menuFragment = new MenuFragment();
        menuFragmentDifficulty = new MenuFragmentDifficulty();
        gameFragment = new GameFragment();

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        soundIds = new int[6];

        soundIds[0] = sp.load(this, R.raw.click_std, 1);
        soundIds[1] = sp.load(this, R.raw.flip, 1);
        soundIds[2] = sp.load(this, R.raw.par1, 1);
        soundIds[3] = sp.load(this, R.raw.cardplace3, 1);
        soundIds[4] = sp.load(this, R.raw.winning, 1);
        soundIds[5] = sp.load(this, R.raw.cardfan1, 1);

        gotoMenu();
    }

    public void gotoMenu() {
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, menuFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void gotoDiffMenu() {
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, menuFragmentDifficulty);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void gotoGame() {
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, gameFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void setDifficulty(int columns, int rows) {
        GameFragment.setGameRows(rows);
        GameFragment.setGameColumns(columns);
    }


    //Fullscreen mode
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}

