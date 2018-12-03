package com.example.bjojoh17.memorytest;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class EndScoreDialog extends Fragment {

    public Button buttonQuit;
    public Button buttonPlayAgain;
    public TextView endText;

    public String endMessage;

    Vibrator vibrator;

    private int vibrateShort = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.end_score_dialog, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        YoYo.with(Techniques.BounceInUp)
                .duration(1500)
                .repeat(0)
                .playOn(view);

        buttonQuit = getActivity().findViewById(R.id.button_quit);
        buttonPlayAgain = getActivity().findViewById(R.id.button_play_again);

        endText = getActivity().findViewById(R.id.text_game_end);
        endText.setText(endMessage);

        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        final Handler handler = new Handler();

        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonQuit.setTranslationX(4);
                buttonQuit.setTranslationY(4);
                vibrator.vibrate(vibrateShort);
                ((MainActivity)getActivity()).clickSound.start();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)getActivity()).gotoMenu();
                    }
                }, 200);
            }
        });

        buttonPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameFragment.setBusy(false);
                buttonPlayAgain.setTranslationX(4);
                buttonPlayAgain.setTranslationY(4);
                vibrator.vibrate(vibrateShort);
                ((MainActivity)getActivity()).clickSound.start();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        GameFragment fragment = (GameFragment)
                                getFragmentManager().findFragmentById(R.id.fragment_container);

                        getFragmentManager().beginTransaction()
                                .detach(fragment)
                                .attach(fragment)
                                .commit();
                    }
                }, 200);

            }
        });

    }

    public void setMessage(String endText) {
        endMessage = endText;
    }

}

