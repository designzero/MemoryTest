package com.example.bjojoh17.memorytest;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private int numberMatched;
    private int numberOfElements;

    private MemoryButton[] buttons;

    private int[] buttonGraphicIndexes;
    private int[] buttonGraphics;

    private MemoryButton selectedButton1;
    private MemoryButton selectedButton2;

    private TextView pl1ScoreText;
    private TextView pl2ScoreText;
    private int pl1Score = 0;
    private int pl2Score = 0;
    private int turn = 0;

    private int flipDelay = 1500;
    private int matchedShowDuration = 1000;
    private int zoomInDuration = 1000;
    private int zoomOutDuration = 1000;
    private int showZoomedDuration= 1500;

    private TextView textEnd;

    private boolean duo;

    private static boolean isBusy = false;

    public static void setBusy(boolean busy) {
        isBusy = busy;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();

        duo = (boolean) getIntent().getExtras().get("duo");

        setContentView(R.layout.activity_game4x3activity);

        pl1ScoreText = findViewById(R.id.pl1_score);
        pl2ScoreText = findViewById(R.id.pl2_score);

        if (duo) {
            turn = 2;
            switchSides();
        }

        else {
            pl1ScoreText.setVisibility(View.INVISIBLE);

            pl2ScoreText.setText("Par:  0");
        }

        GridLayout gridLayout = findViewById(R.id.grid_layout_4x3);
        gridLayout.setRowCount(3);
        gridLayout.setColumnCount(4);

        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int numColumns = gridLayout.getColumnCount();
        int numRows = gridLayout.getRowCount();

        numberOfElements = numColumns * numRows;

        buttons = new MemoryButton[numberOfElements];

        buttonGraphics = new int[numberOfElements / 2];

        /*buttonGraphics[0] = R.drawable.button_1;
        buttonGraphics[1] = R.drawable.button_2;
        buttonGraphics[2] = R.drawable.button_3;
        buttonGraphics[3] = R.drawable.button_4;
        buttonGraphics[4] = R.drawable.button_5;
        buttonGraphics[5] = R.drawable.button_6;*/
        //buttonGraphics[6] = R.drawable.button_7;
        //buttonGraphics[7] = R.drawable.button_8;

        buttonGraphics[0] = R.drawable.button_11;
        buttonGraphics[1] = R.drawable.button_12;
        buttonGraphics[2] = R.drawable.button_13;
        buttonGraphics[3] = R.drawable.button_14;
        buttonGraphics[4] = R.drawable.button_15;
        buttonGraphics[5] = R.drawable.button_16;

       /* // array of supported extensions (use a List if you prefer)
        final String[] EXTENSIONS = new String[]{
                "gif", "png", "bmp" // and other formats you need
        };
        // filter to identify images based on their extensions
        final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

            @Override
            public boolean accept(final File dir, final String name) {
                for (final String ext : EXTENSIONS) {
                    if (name.endsWith("." + ext)) {
                        return (true);
                    }
                }
                return (false);
            }
        };

        File dir = new File("/sdcard/DCIM");
        File[] filelist = dir.listFiles(IMAGE_FILTER );
        for (File f : filelist) {
            // do your stuff here


        }*/



        buttonGraphicIndexes = new int[numberOfElements];

        shuffleButtonGraphics();

        for(int r = 0; r < numRows; r++){
            for(int c = 0; c < numColumns; c++){
                MemoryButton tempButton = new MemoryButton(this, r, c, buttonGraphics[buttonGraphicIndexes[r * numColumns + c]]);
                tempButton.setId(View.generateViewId());
                tempButton.setOnClickListener(this);

                //Vrid brickorna - start
                //int buttonRotation = new Random().nextInt(10) - 5;
                //tempButton.setRotation(buttonRotation);
                //slut

                buttons[r * numColumns + c] = tempButton;
                gridLayout.addView(tempButton);
            }
        }

    }

    protected void switchSides() {
        if (turn == 2) {
            pl1ScoreText.setTextColor(Color.BLACK);
            pl1ScoreText.setTypeface(Typeface.DEFAULT_BOLD);
            pl2ScoreText.setTypeface(Typeface.DEFAULT);
            pl2ScoreText.setTextColor(Color.GRAY);
            turn = 1;
        }
        if (turn == 1) {
            pl1ScoreText.setTextColor(Color.GRAY);
            pl1ScoreText.setTypeface(Typeface.DEFAULT);
            pl2ScoreText.setTextColor(Color.BLACK);
            pl2ScoreText.setTypeface(Typeface.DEFAULT_BOLD);
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
            return x+y;
        }
    }

    protected void animateMatched(final MemoryButton button2) {
        final DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

        final MemoryButton button1 = selectedButton1;

        button1.bringToFront();

        PropertyValuesHolder pvhX2 = PropertyValuesHolder.ofFloat("translationX", metrics.widthPixels / 2 - getViewCoords(button1, "x") - button1.getWidth() /2);
        PropertyValuesHolder pvhY2 = PropertyValuesHolder.ofFloat("translationY", metrics.heightPixels  / 2 - button1.getY() - button1.getHeight() / 2);
        PropertyValuesHolder pvhSX2 = PropertyValuesHolder.ofFloat("scaleX", 4);
        PropertyValuesHolder pvhSY2 = PropertyValuesHolder.ofFloat("scaleY", 4);
        //PropertyValuesHolder pvhA2 = PropertyValuesHolder.ofFloat("alpha", 0.5f);
        ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(button1, pvhX2, pvhY2, pvhSX2, pvhSY2);
        animator2.setInterpolator(new DecelerateInterpolator());
        animator2.setDuration(zoomInDuration);
        isBusy = true;
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
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationY", metrics.heightPixels / 2 - button2.getY() - button2.getHeight() / 2);
        PropertyValuesHolder pvhSX = PropertyValuesHolder.ofFloat("scaleX", 4);
        PropertyValuesHolder pvhSY = PropertyValuesHolder.ofFloat("scaleY", 4);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(button2, pvhX, pvhY, pvhSX, pvhSY);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(zoomInDuration);
        animator.start();

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isBusy = false;

                        PropertyValuesHolder pvhX3 = PropertyValuesHolder.ofFloat("x", metrics.widthPixels - button2.getWidth());
                        PropertyValuesHolder pvhY3 = PropertyValuesHolder.ofFloat("y",  - getViewCoords(button2, "y"));
                        PropertyValuesHolder pvhSX3 = PropertyValuesHolder.ofFloat("scaleX", 0.1f);
                        PropertyValuesHolder pvhSY3 = PropertyValuesHolder.ofFloat("scaleY", 0.1f);
                        PropertyValuesHolder pvhA3 = PropertyValuesHolder.ofFloat("alpha", 0.0f);
                        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(button2, pvhX3, pvhY3, pvhSX3, pvhSY3, pvhA3);
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
                            }

                            @Override
                            public void onAnimationCancel(Animator animator3) {

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

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }


    protected void addScore() {
        if (turn == 0) {
            pl2ScoreText.setText("Par:  " + numberMatched);
        }

        if (turn == 1) {
            pl1Score++;
            pl1ScoreText.setText("Spelare 1:  " + pl1Score);
        }

        if (turn == 2) {
            pl2Score++;
            pl2ScoreText.setText("Spelare 2:  " + pl2Score);
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

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.

        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        android.app.Fragment prev = getFragmentManager().findFragmentByTag("endScoreDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        EndScoreDialog esd = new EndScoreDialog();
        esd.setMessage(endMessage);
        esd.show(getSupportFragmentManager(), "endScoreDialog");
    }

    @Override
    public void onClick(View view) {
        if(isBusy)
            return;

        final MemoryButton button = (MemoryButton) view;

        if(button.isMatched)
            return;

        if(selectedButton1 == null){
            selectedButton1 = button;
            selectedButton1.flip();
            return;
        }

        if(selectedButton1.getId() == button.getId()) {
            return;
        }

        if(selectedButton1.getFrontDrawableId() == button.getFrontDrawableId()) {
            button.flip();

            button.setMatched(true);
            selectedButton1.setMatched(true);

            selectedButton1.setEnabled(false);
            button.setEnabled(false);


            //Behåll brickorna efter matchning - start
//            selectedButton1 = null;
//            numberMatched++;
//            if (numberMatched == numberOfElements / 2) {
//                Toast toast = Toast.makeText(getApplicationContext(), "Bra jobbat!", Toast.LENGTH_SHORT); toast.show();
//            }
            //slut

            //Ta bort brickorna efter matchning - start
            isBusy = true;
            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    numberMatched++;

                    if (duo) {
                        selectedButton1.setVisibility(View.INVISIBLE);
                        button.setVisibility(View.INVISIBLE);
                        addScore();
                        isBusy = false;
                    }
                    else {
                        animateMatched(button);
                    }

                    selectedButton1 = null;


                    //showEndScore("Bra jobbat!");  //Test

                    if (numberMatched == numberOfElements / 2) {
                        if (duo) {
                            if (pl1Score > pl2Score) {
                                showEndScore("Spelare 1 vann!");
                            }
                            else if (pl1Score < pl2Score) {
                                showEndScore("Spelare 2 vann!");
                            }
                            else {
                                showEndScore("Det blev lika!");
                            }

                        }
                        else {
                            final Handler handler = new Handler();

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showEndScore("Bra jobbat!");
                                }
                            }, 3000);
                        }
                    }
                }
            }, matchedShowDuration);
            //slut

        }

        else {
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
