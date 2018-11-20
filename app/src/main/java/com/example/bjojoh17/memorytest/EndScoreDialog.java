package com.example.bjojoh17.memorytest;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class EndScoreDialog extends DialogFragment {

    public Button buttonQuit;
    public Button buttonPlayAgain;
    public TextView endText;

    public String endMessage;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Fullscreen
        this.getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View endScoreView = inflater.inflate(R.layout.end_score_dialog, null);

        builder.setView(endScoreView);

        setCancelable(false);

        buttonQuit = endScoreView.findViewById(R.id.button_quit);
        buttonPlayAgain = endScoreView.findViewById(R.id.button_play_again);

        endText = endScoreView.findViewById(R.id.text_game_end);
        endText.setText(endMessage);

        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        buttonPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                getActivity().recreate();
            }
        });


        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void setMessage(String endText) {
        endMessage = endText;
    }

}

