package com.example.bjojoh17.memorytest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MenuFragmentDifficulty extends Fragment {

    private Button backButton;
    private Button buttonEasy;
    private Button buttonMedium;
    private Button buttonHard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_menu_difficulty, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        backButton = getActivity().findViewById(R.id.button_back3);
        buttonEasy = getActivity().findViewById(R.id.button_easy);
        buttonMedium = getActivity().findViewById(R.id.button_medium);
        buttonHard = getActivity().findViewById(R.id.button_hard);

        final Handler handler = new Handler();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).clickSound.start();
                backButton.setTranslationX(4);
                backButton.setTranslationY(4);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    ((MainActivity)getActivity()).gotoMenu();
                    }
                }, 200);
            }
        });

        buttonEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).clickSound.start();
                ((MainActivity)getActivity()).setDifficulty(4,2);
                buttonEasy.setTranslationX(4);
                buttonEasy.setTranslationY(4);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)getActivity()).gotoGame();
                    }
                }, 200);
            }
        });

        buttonMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).clickSound.start();
                ((MainActivity)getActivity()).setDifficulty(4,3);
                buttonMedium.setTranslationX(4);
                buttonMedium.setTranslationY(4);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)getActivity()).gotoGame();
                    }
                }, 200);
            }
        });
        buttonHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).clickSound.start();
                ((MainActivity)getActivity()).setDifficulty(4,4);
                buttonHard.setTranslationX(4);
                buttonHard.setTranslationY(4);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)getActivity()).gotoGame();
                    }
                }, 200);
            }
        });
    }
}
