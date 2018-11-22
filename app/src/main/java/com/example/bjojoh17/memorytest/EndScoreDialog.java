package com.example.bjojoh17.memorytest;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class EndScoreDialog extends Fragment {

    public Button buttonQuit;
    public Button buttonPlayAgain;
    public TextView endText;

    public String endMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.end_score_dialog, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        buttonQuit = getActivity().findViewById(R.id.button_quit);
        buttonPlayAgain = getActivity().findViewById(R.id.button_play_again);

        endText = getActivity().findViewById(R.id.text_game_end);
        endText.setText(endMessage);

        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).gotoMenu();
            }
        });

        buttonPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).gotoMenu();
                ((MainActivity)getActivity()).gotoGame(GameFragment.isDuo());
            }
        });

    }

    public void setMessage(String endText) {
        endMessage = endText;
    }

}

