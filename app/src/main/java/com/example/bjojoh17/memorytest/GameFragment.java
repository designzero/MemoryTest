package com.example.bjojoh17.memorytest;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Random;

public class GameFragment extends Fragment implements View.OnClickListener {

    private static int gameRows;
    private static int gameColumns;

    public static int getGameRows() {
        return gameRows;
    }

    public static void setGameRows(int nRgameRows) {
        gameRows = nRgameRows;
    }

    public static void setGameColumns(int nRgameColumns) {
        gameColumns = nRgameColumns;
    }

    private int numberMatched;
    private int numberOfElements;

    private MemoryButton[] buttons;
    public MemoryButton animatedButton;

    private int[] buttonGraphicIndexes;
    private int[] buttonGraphics;

    private MemoryButton selectedButton1;
    private MemoryButton selectedButton2;

    private ConstraintLayout pl1Bg;
    private ConstraintLayout pl2Bg;
    private TextView pl1ScoreText;
    private TextView pl2ScoreText;
    private TextView pl1Name;
    private TextView pl2Name;
    private int pl1Score = 0;
    private int pl2Score = 0;
    private int turn = 0;

    private int flipDelay = 1500;
    private int matchedShowDuration = 1000;
    private int zoomInDuration = 1500;
    private int zoomOutDuration = 1500;
    private int showZoomedDuration= 2000;

    private static boolean duo;

    public static boolean isDuo() {
        return duo;
    }

    public static void setDuo(boolean playDuo) {
        duo = playDuo;
    }

    private static boolean isBusy = false;

    public static void setIsZooming(boolean isZooming) {
        GameFragment.isZooming = isZooming;
    }

    private static boolean isZooming = false;

    private static boolean isShowingPhoto = false;

    public static void setBusy(boolean busy) {
        isBusy = busy;
    }

    public static boolean skipped = false;


    Vibrator vibrator;
    private int vibrateShort = 100;
    private int vibrateLong = 500;

    public LinearLayout buttonContainer;
    public FrameLayout gameFragmentContainer;
    public GridLayout gridLayout;

    public ObjectAnimator animator;
    public ObjectAnimator animator2;
    public ObjectAnimator animator3;

    final Handler animHandler = new Handler();
    Handler endHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        if (duo) {
            return inflater.inflate(R.layout.fragment_game_multi, parent, false);
        }
        else {
            return inflater.inflate(R.layout.fragment_game, parent, false);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initGame();
    }

    public void initGame() {
        isBusy = true;

        selectedButton1 = null;

        numberMatched = 0;

        pl1Score = 0;
        pl2Score = 0;

        gameFragmentContainer = getActivity().findViewById(R.id.game_fragment_container);

        buttonContainer = getActivity().findViewById(R.id.button_container);

        gridLayout = getActivity().findViewById(R.id.grid_layout);

        GridLayout gridLayout = getActivity().findViewById(R.id.grid_layout);
        gridLayout.setRowCount(gameRows);
        gridLayout.setColumnCount(gameColumns);

        numberOfElements = gameColumns * gameRows;

        pl1ScoreText = getActivity().findViewById(R.id.pl1_score);

        if (duo) {
            pl2ScoreText = getActivity().findViewById(R.id.pl2_score);
            pl1Name = getActivity().findViewById(R.id.pl1_name);
            pl2Name = getActivity().findViewById(R.id.pl2_name);
            pl1ScoreText.setText("0/" + numberOfElements/2);
            pl2ScoreText.setText("0/" + numberOfElements/2);
            pl1Bg = getActivity().findViewById(R.id.pl1_bg);
            pl2Bg = getActivity().findViewById(R.id.pl2_bg);
            turn = new Random().nextInt(2) + 1;
            System.out.println("XX " + turn);
            switchSides();
        }

        else {
            turn = 0;
            //pl1ScoreText.setVisibility(View.INVISIBLE);
            pl1ScoreText.setText("0/" + numberOfElements/2);
        }

        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        final Button backButton = getActivity().findViewById(R.id.button_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberMatched = 0;
                vibrator.vibrate(vibrateShort);
                ((MainActivity)getActivity()).sp.play(((MainActivity)getActivity()).soundIds[0], 1, 1, 1, 0, 1.0f);
                cancelMatchedAnimation();
                backButton.setTranslationX(4);
                backButton.setTranslationY(4);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)getActivity()).gotoMenu();
                    }
                }, 200);
            }
        });

        gameFragmentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isZooming)
                    cancelMatchedAnimation();
            }
        });


        buttons = new MemoryButton[numberOfElements];

        buttonGraphics = new int[numberOfElements / 2];

        if (gameColumns * gameRows == 8) {
            buttonGraphics[0] = R.drawable.button_11;
            buttonGraphics[1] = R.drawable.button_12;
            buttonGraphics[2] = R.drawable.button_13;
            buttonGraphics[3] = R.drawable.button_14;
        }

        else if (gameColumns * gameRows == 12) {
            buttonGraphics[0] = R.drawable.button_11;
            buttonGraphics[1] = R.drawable.button_12;
            buttonGraphics[2] = R.drawable.button_13;
            buttonGraphics[3] = R.drawable.button_14;
            buttonGraphics[4] = R.drawable.button_15;
            buttonGraphics[5] = R.drawable.button_16;
        }

        else if (gameColumns * gameRows == 16) {
            buttonGraphics[0] = R.drawable.button_11;
            buttonGraphics[1] = R.drawable.button_12;
            buttonGraphics[2] = R.drawable.button_13;
            buttonGraphics[3] = R.drawable.button_14;
            buttonGraphics[4] = R.drawable.button_15;
            buttonGraphics[5] = R.drawable.button_16;
            buttonGraphics[6] = R.drawable.button_17;
            buttonGraphics[7] = R.drawable.button_18;
        }

        buttonGraphicIndexes = new int[numberOfElements];

        shuffleButtonGraphics();

        for(int r = 0; r < gameRows; r++){
            for(int c = 0; c < gameColumns; c++){
                MemoryButton tempButton = new MemoryButton(getContext(), r, c, buttonGraphics[buttonGraphicIndexes[r * gameColumns + c]]);
                tempButton.setId(View.generateViewId());
                tempButton.setOnClickListener(this);
                tempButton.setSoundEffectsEnabled(false);

                buttons[r * gameColumns + c] = tempButton;
                gridLayout.addView(tempButton);
            }
        }
        ((MainActivity)getActivity()).sp.play(((MainActivity)getActivity()).soundIds[5], 1, 1, 1, 0, 1);
        YoYo.with(Techniques.BounceInUp)
                .duration(1150)
                .repeat(0)
                .playOn(gridLayout);
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isBusy = false;
            }
        },750);
    }

    protected void switchSides() {
        if (turn == 2) {
            pl1Bg.setBackground(getResources().getDrawable(R.drawable.player_bg_radius));
            pl2Bg.setBackground(null);
            turn = 1;
        }
        else if (turn == 1) {
            pl2Bg.setBackground(getResources().getDrawable(R.drawable.player_bg_radius));
            pl1Bg.setBackground(null);
            turn = 2;
        }
    }

    public int getViewCoords(View v, String value){
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (value == "x") {
            return x;
        }
        if (value == "y") {
            return y;
        }
        else {
            return 0;
        }
    }

    protected void cancelMatchedAnimation() {
        skipped = true;
        if(animator != null)
            animator.cancel();
        if(animator2 != null)
            animator2.cancel();
        if(animator3 != null)
            animator3.cancel();
        animHandler.removeCallbacksAndMessages(null);
        endHandler.removeCallbacksAndMessages(null);
        if(isShowingPhoto) {
            isShowingPhoto = false;
            animatedButton.setVisibility(View.INVISIBLE);
            addScore();
        }
        if (numberMatched == numberOfElements / 2) {
            showEndScore("Bra jobbat!");
        }
        isBusy = false;
        isZooming = false;
    }

    protected void animateMatched(final MemoryButton button2) {

        isBusy = true;
        isZooming = true;

        final DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

        final MemoryButton button1 = selectedButton1;

        animatedButton = button2;

        button1.bringToFront();
        button1.setClickable(false);
        button2.setClickable(false);


        PropertyValuesHolder pvhX2 = PropertyValuesHolder.ofFloat("translationX", metrics.widthPixels / 2 - getViewCoords(button1, "x") - button1.getWidth() / 2);
        PropertyValuesHolder pvhY2 = PropertyValuesHolder.ofFloat("translationY",  gameFragmentContainer.getHeight() / 2 + gameFragmentContainer.getY() - getViewCoords(button1, "y") - button1.getHeight() / 2);
        PropertyValuesHolder pvhSX2 = PropertyValuesHolder.ofFloat("scaleX", 3.7f);
        PropertyValuesHolder pvhSY2 = PropertyValuesHolder.ofFloat("scaleY", 3.7f);
        animator2 = ObjectAnimator.ofPropertyValuesHolder(button1, pvhX2, pvhY2, pvhSX2, pvhSY2);
        animator2.setInterpolator(new DecelerateInterpolator());
        animator2.setDuration(zoomInDuration);
        animator2.start();


        animator2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator2) {

            }

            @Override
            public void onAnimationEnd(Animator animator2) {
                button1.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator2) {
            }

            @Override
            public void onAnimationRepeat(Animator animator2) {

            }
        });

        button2.bringToFront();

        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("translationX", metrics.widthPixels / 2 - getViewCoords(button2, "x") - button2.getWidth() /2);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationY",  gameFragmentContainer.getHeight() / 2 + gameFragmentContainer.getY() - getViewCoords(button2, "y") - button2.getHeight() / 2);
        PropertyValuesHolder pvhSX = PropertyValuesHolder.ofFloat("scaleX", 4);
        PropertyValuesHolder pvhSY = PropertyValuesHolder.ofFloat("scaleY", 4);
        animator = ObjectAnimator.ofPropertyValuesHolder(button2, pvhX, pvhY, pvhSX, pvhSY);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(zoomInDuration);
        animator.start();

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                    isShowingPhoto = true;
                    animHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isShowingPhoto = false;
                            PropertyValuesHolder pvhX3 = PropertyValuesHolder.ofFloat("x", (button2.getWidth() * button2.getScaleX()) / 2 + (metrics.widthPixels / 2) - gridLayout.getX() - 37);
                            PropertyValuesHolder pvhY3 = PropertyValuesHolder.ofFloat("y", (button2.getHeight() * button2.getScaleY()) / 2 - (metrics.heightPixels / 2) - gridLayout.getY() - (gameFragmentContainer.getY() / 2));
                            PropertyValuesHolder pvhSX3 = PropertyValuesHolder.ofFloat("scaleX", 0.32f / metrics.density);
                            PropertyValuesHolder pvhSY3 = PropertyValuesHolder.ofFloat("scaleY", 0.32f / metrics.density);
                            PropertyValuesHolder pvhR3 = PropertyValuesHolder.ofFloat("rotation", 75f);
                            animator3 = ObjectAnimator.ofPropertyValuesHolder(button2, pvhX3, pvhY3, pvhSX3, pvhSY3, pvhR3);
                            animator3.setInterpolator(new AccelerateDecelerateInterpolator());
                            animator3.setDuration(zoomOutDuration);
                            animator3.start();

                            animator3.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator3) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animator3) {
                                    button2.setVisibility(View.INVISIBLE);
                                    addScore();
                                    YoYo.with(Techniques.RubberBand)
                                            .duration(500)
                                            .repeat(0)
                                            .playOn(pl1ScoreText);
                                    isBusy = false;
                                    setIsZooming(false);
                                }

                                @Override
                                public void onAnimationCancel(Animator animator3) {
                                    skipped = true;
                                }

                                @Override
                                public void onAnimationRepeat(Animator animator3) {

                                }
                            });

                        }
                    }, showZoomedDuration);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                animator.removeAllListeners();
                button2.setVisibility(View.INVISIBLE);
                addScore();
                YoYo.with(Techniques.RubberBand)
                        .duration(500)
                        .repeat(0)
                        .playOn(pl1ScoreText);
                isBusy = false;
                setIsZooming(false);
                skipped = true;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }


    protected void addScore() {
        if (numberMatched != numberOfElements / 2)
            ((MainActivity)getActivity()).sp.play(((MainActivity)getActivity()).soundIds[3], 0.5f, 0.5f, 1, 0, 1);

        if (turn == 0) {
            pl1Score++;
            pl1ScoreText.setText(pl1Score + "/" + numberOfElements/2);
            if(skipped) {
                YoYo.with(Techniques.RubberBand)
                        .duration(500)
                        .repeat(0)
                        .playOn(pl1ScoreText);
            }
        }

        if (turn == 1) {
            pl1Score++;
            pl1ScoreText.setText(pl1Score + "/" + numberOfElements/2);
            YoYo.with(Techniques.RubberBand)
                    .duration(500)
                    .repeat(0)
                    .playOn(pl1ScoreText);
        }

        if (turn == 2) {
            pl2Score++;
            pl2ScoreText.setText(pl2Score + "/" + numberOfElements/2);
            YoYo.with(Techniques.RubberBand)
                    .duration(500)
                    .repeat(0)
                    .playOn(pl2ScoreText);
        }
    }

    protected void shuffleButtonGraphics() {

        Random rand = new Random();

        for(int i = 0; i < numberOfElements; i++) {
            buttonGraphicIndexes[i] = i % (numberOfElements / 2);
        }

        for(int i = 0; i < numberOfElements; i++) {
            int temp = buttonGraphicIndexes[i];

            int swapIndex = rand.nextInt(numberOfElements);

            buttonGraphicIndexes[i] = buttonGraphicIndexes[swapIndex];

            buttonGraphicIndexes[swapIndex] = temp;
        }

    }

    protected void showEndScore(String endMessage) {
        isBusy = true;
        Fragment endScore = new EndScoreDialog();

        FragmentTransaction ft;
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.game_fragment_container, endScore);
        ft.addToBackStack(null);
        ft.commit();
        ((EndScoreDialog) endScore).setMessage(endMessage);
        ((MainActivity)getActivity()).sp.play(((MainActivity)getActivity()).soundIds[4], 0.3f, 0.3f, 1, 0, 1);
    }

    public void onClick(Layout layout) {
        if(isZooming){
            cancelMatchedAnimation();
        }
    }

    @Override
    public void onClick(View view) {
        view.setSoundEffectsEnabled(false);

        final MemoryButton button = (MemoryButton) view;

        if(isZooming){
            cancelMatchedAnimation();
            return;
        }

        if(isBusy) {
            System.out.println("XX I'm busy mom!");
            return;
        }



        if(button.isMatched)
            return;

        if(selectedButton1 == null){
            ((MainActivity)getActivity()).sp.play(((MainActivity)getActivity()).soundIds[1], 0.3f, 0.3f, 1, 0, 1);
            vibrator.vibrate(vibrateShort);
            selectedButton1 = button;
            selectedButton1.flip();
            return;
        }

        if(selectedButton1.getId() == button.getId()) {
            return;
        }

        if(selectedButton1.getFrontDrawableId() == button.getFrontDrawableId()) {
            isBusy = true;
            ((MainActivity)getActivity()).sp.play(((MainActivity)getActivity()).soundIds[2], 1, 1, 1, 0, 1);
            button.flip();
            vibrator.vibrate(vibrateLong);


            YoYo.with(Techniques.Pulse)
                    .duration(700)
                    .repeat(0)
                    .playOn(selectedButton1);
            YoYo.with(Techniques.Pulse)
                    .duration(700)
                    .repeat(0)
                    .playOn(button);

            button.setMatched(true);
            selectedButton1.setMatched(true);

            selectedButton1.setEnabled(false);
            button.setEnabled(false);

            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    numberMatched++;

                    if (duo) {
                        selectedButton1.setVisibility(View.INVISIBLE);
                        button.setVisibility(View.INVISIBLE);
                        isBusy = false;
                        addScore();
                    }
                    else {
                        skipped = false;
                        animateMatched(button);
                    }

                    selectedButton1 = null;


                    System.out.println("XX Skipped");

                    if (numberMatched == numberOfElements / 2) {
                    //if (numberMatched == 1) { // test mode
                        isBusy = true;
                        System.out.println("XX " + skipped);
                        if (duo) {
                            if (pl1Score > pl2Score) {
                                showEndScore("Nils fick flest par!");
                            }
                            else if (pl1Score < pl2Score) {
                                showEndScore("Gästen fick flest par!");
                            }
                            else {
                                showEndScore("Det blev lika!");
                            }

                        }

                        else {
                            endHandler = new Handler();

                            endHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showEndScore("Bra jobbat!");
                                }
                            }, zoomInDuration + zoomOutDuration + showZoomedDuration);
                        }
                    }
                }
            }, matchedShowDuration);
            //slut

        }

        else {
            ((MainActivity)getActivity()).sp.play(((MainActivity)getActivity()).soundIds[1], 0.3f, 0.3f, 1, 0, 1);
            vibrator.vibrate(vibrateShort);
            selectedButton2 = button ;
            selectedButton2.flip();
            isBusy = true;

            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    selectedButton2.flip();
                    selectedButton1.flip();
                    selectedButton1 = null;
                    selectedButton2 = null;
                    isBusy = false;
                    if (duo) {
                        switchSides();
                    }
                }
            }, flipDelay);
        }
    }
}
