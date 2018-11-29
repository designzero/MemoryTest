package com.example.bjojoh17.memorytest;

import android.os.Bundle;
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


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).clickSound.start();
                ((MainActivity)getActivity()).gotoMenu();
            }
        });

        buttonEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).clickSound.start();
                ((MainActivity)getActivity()).setDifficulty(4,2);
                ((MainActivity)getActivity()).gotoGame();
            }
        });

        buttonMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).clickSound.start();
                ((MainActivity)getActivity()).setDifficulty(4,3);
                ((MainActivity)getActivity()).gotoGame();
            }
        });
        buttonHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).clickSound.start();
                ((MainActivity)getActivity()).setDifficulty(4,4);
                ((MainActivity)getActivity()).gotoGame();
            }
        });
    }
}
