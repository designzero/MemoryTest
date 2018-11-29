package com.example.bjojoh17.memorytest;

import android.media.MediaPlayer;
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

    public MediaPlayer clickSound;
    public MediaPlayer flipSound;
    public MediaPlayer matchedSound;
    public MediaPlayer startSound;
    public MediaPlayer winSound;
    public MediaPlayer placeSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        menuFragment = new MenuFragment();
        menuFragmentDifficulty = new MenuFragmentDifficulty();
        gameFragment = new GameFragment();

        clickSound = MediaPlayer.create(this, R.raw.click_std);
        clickSound.setVolume(0.5f,0.5f);
        flipSound = MediaPlayer.create(this, R.raw.flip);
        flipSound.setVolume(0.3f,0.3f);
        matchedSound = MediaPlayer.create(this, R.raw.par1);
        winSound = MediaPlayer.create(this, R.raw.winning);
        winSound.setVolume(0.30f,0.30f);
        startSound = MediaPlayer.create(this, R.raw.cardfan1);
        startSound.setVolume(0.30f,0.30f);
        placeSound = MediaPlayer.create(this, R.raw.cardplace3);
        placeSound.setVolume(0.50f,0.50f);

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
        //ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        //ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
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

