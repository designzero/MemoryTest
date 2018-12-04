package com.example.bjojoh17.memorytest;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MenuFragment extends Fragment {

    private Button buttonBack;
    private Button buttonSolo;
    private Button buttonDuo;

    Vibrator vibrator;

    private int vibrateShort = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_menu_main, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buttonBack = getActivity().findViewById(R.id.button_back2);
        buttonSolo = getActivity().findViewById(R.id.button_solo);
        buttonDuo = getActivity().findViewById(R.id.button_duo);

        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        final Handler handler = new Handler();

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(vibrateShort);
                ((MainActivity)getActivity()).sp.play(((MainActivity)getActivity()).soundIds[0], 1, 1, 1, 0, 1.0f);
                getActivity().finish();
            }
        });

        buttonSolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameFragment.setDuo(false);
                buttonSolo.setTranslationX(4);
                buttonSolo.setTranslationY(4);
                vibrator.vibrate(vibrateShort);
                ((MainActivity)getActivity()).sp.play(((MainActivity)getActivity()).soundIds[0], 1, 1, 1, 0, 1.0f);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)getActivity()).gotoDiffMenu();
                    }
                }, 200);
            }
        });

        buttonDuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameFragment.setDuo(true);
                buttonDuo.setTranslationX(4);
                buttonDuo.setTranslationY(4);
                vibrator.vibrate(vibrateShort);
                ((MainActivity)getActivity()).sp.play(((MainActivity)getActivity()).soundIds[0], 1, 1, 1, 0, 1.0f);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)getActivity()).gotoDiffMenu();
                    }
                }, 200);
            }
        });
    }
}
