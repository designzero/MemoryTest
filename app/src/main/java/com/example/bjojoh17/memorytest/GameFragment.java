package com.example.bjojoh17.memorytest;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
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

import java.io.File;
import java.io.FilenameFilter;
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

    public static void setBusy(boolean busy) {
        isBusy = busy;
    }

    Vibrator vibrator;
    private int vibrateShort = 100;
    private int vibrateLong = 500;

    LinearLayout buttonContainer;
    FrameLayout gameFragmentContainer;
    GridLayout gridLayout;

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

        numberMatched = 0;

        pl1Score = 0;

        pl1ScoreText = getActivity().findViewById(R.id.pl1_score);


        if (duo) {
            pl2Score = 0;
            pl2ScoreText = getActivity().findViewById(R.id.pl2_score);
            turn = 2;
            switchSides();
        }

        else {
            turn = 0;
            //pl1ScoreText.setVisibility(View.INVISIBLE);
            pl1ScoreText.setText("0");
        }

        buttonContainer = getActivity().findViewById(R.id.button_container);
        gameFragmentContainer = getActivity().findViewById(R.id.game_fragment_container);
        gridLayout = getActivity().findViewById(R.id.grid_layout);

        GridLayout gridLayout = getActivity().findViewById(R.id.grid_layout);
        gridLayout.setRowCount(gameRows);
        gridLayout.setColumnCount(gameColumns);

        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        Button backButton = getActivity().findViewById(R.id.button_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).gotoMenu();
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
            buttonGraphics[6] = R.drawable.button_15;
            buttonGraphics[7] = R.drawable.button_16;
        }

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

        //shuffleButtonGraphics();

        for(int r = 0; r < numRows; r++){
            for(int c = 0; c < numColumns; c++){
                MemoryButton tempButton = new MemoryButton(getContext(), r, c, buttonGraphics[buttonGraphicIndexes[r * numColumns + c]]);
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

    protected void loadImages() {
        // array of supported extensions (use a List if you prefer)
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

        String dirPath = Environment.getExternalStorageDirectory().toString()+"/Pictures";
        File dir = new File(dirPath);
        File[] filelist = dir.listFiles(IMAGE_FILTER );
        for (File f : filelist) {
            // do your stuff here
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),bmOptions);
            //bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
            //imageView.setImageBitmap(bitmap);
        }
    }


    protected void switchSides() {
        if (turn == 2) {
            pl1ScoreText.setTextColor(getResources().getColor(R.color.button_blue));
            pl1ScoreText.setTypeface(Typeface.DEFAULT_BOLD);
            pl2ScoreText.setTypeface(Typeface.DEFAULT);
            pl2ScoreText.setTextColor(Color.WHITE);
            turn = 1;
        }
        else if (turn == 1) {
            pl1ScoreText.setTextColor(Color.WHITE);
            pl1ScoreText.setTypeface(Typeface.DEFAULT);
            pl1ScoreText.setTextColor(getResources().getColor(R.color.button_blue));
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

        GridLayout.LayoutParams lp = (GridLayout.LayoutParams) button1.getLayoutParams();

        button1.bringToFront();

        System.out.println("XX " + gridLayout.getY());

        PropertyValuesHolder pvhX2 = PropertyValuesHolder.ofFloat("translationX", metrics.widthPixels / 2 - getViewCoords(button1, "x") - button1.getWidth() /2);
        //PropertyValuesHolder pvhY2 = PropertyValuesHolder.ofFloat("translationY", buttonContainer.getHeight() / 2 - getViewCoords(button1, "y") + button1.getHeight() / 2);
        PropertyValuesHolder pvhY2 = PropertyValuesHolder.ofFloat("translationY",  gameFragmentContainer.getHeight() / 2 - (lp.topMargin + button1.getY() + button1.getHeight() / 2) - gridLayout.getY());
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
        //PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationY", metrics.heightPixels / 2 - getViewCoords(button2, "y") + button2.getHeight() / 2);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationY",  gameFragmentContainer.getHeight() / 2 - (lp.topMargin + button2.getY() + button2.getHeight() / 2) - gridLayout.getY());
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

                        PropertyValuesHolder pvhX3 = PropertyValuesHolder.ofFloat("x", metrics.widthPixels - button2.getWidth() - gridLayout.getX());
                        PropertyValuesHolder pvhY3 = PropertyValuesHolder.ofFloat("y",  - getViewCoords(button2, "y") - gridLayout.getY());
                        PropertyValuesHolder pvhSX3 = PropertyValuesHolder.ofFloat("scaleX", 0.1f);
                        PropertyValuesHolder pvhSY3 = PropertyValuesHolder.ofFloat("scaleY", 0.1f);
                        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(button2, pvhX3, pvhY3, pvhSX3, pvhSY3);
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
            pl1ScoreText.setText("" + numberMatched);
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

        Fragment endScore = new EndScoreDialog();

        FragmentTransaction ft;
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.game_fragment_container, endScore);
        ft.addToBackStack(null);
        ft.commit();
        ((EndScoreDialog) endScore).setMessage(endMessage);
    }

    @Override
    public void onClick(View view) {
        if(isBusy)
            return;

        final MemoryButton button = (MemoryButton) view;

        if(button.isMatched)
            return;

        if(selectedButton1 == null){

            vibrator.vibrate(vibrateShort);
            selectedButton1 = button;
            selectedButton1.flip();
            return;
        }

        if(selectedButton1.getId() == button.getId()) {
            return;
        }

        if(selectedButton1.getFrontDrawableId() == button.getFrontDrawableId()) {
            button.flip();
            vibrator.vibrate(vibrateLong);

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
                    //if (numberMatched == 1) { // test mode
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
                            }, zoomInDuration + zoomOutDuration + showZoomedDuration);
                        }
                    }
                }
            }, matchedShowDuration);
            //slut

        }

        else {
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
