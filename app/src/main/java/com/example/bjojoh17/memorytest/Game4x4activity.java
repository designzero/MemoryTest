package com.example.bjojoh17.memorytest;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Random;

public class Game4x4activity extends AppCompatActivity implements View.OnClickListener {

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
    private int turn = 2;

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
            Log.d("Duo","Duo mode engaged!");
            switchSides();
        }

        else {
            pl1ScoreText.setVisibility(View.INVISIBLE);
            pl2ScoreText.setVisibility(View.INVISIBLE);
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
            pl2ScoreText.setTextColor(Color.GRAY);
            turn = 1;
        }
        else {
            pl1ScoreText.setTextColor(Color.GRAY);
            pl2ScoreText.setTextColor(Color.BLACK);
            turn = 2;
        }
    }
    protected void addScore() {
        if (turn == 1) {
            pl1Score++;
            pl1ScoreText.setText("Spelare 1:  " + pl1Score);
        } else {
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
                    selectedButton1.setVisibility(View.INVISIBLE);
                    button.setVisibility(View.INVISIBLE);
                    selectedButton1 = null;
                    numberMatched++;
                    if (duo) {
                        addScore();
                    }
                    showEndScore("Bra jobbat!!!");

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
                            //Toast toast = Toast.makeText(getApplicationContext(), "Bra jobbat!", Toast.LENGTH_SHORT);
                            //toast.show();
                            showEndScore("Bra jobbat!");

                        }
                    }
                    isBusy = false;
                }
            }, 1000);
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
                    switchSides();
                }
            }, 1000);
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

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
