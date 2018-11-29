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
import android.graphics.drawable.Drawable;
import android.icu.text.SymbolTable;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

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
    public MemoryButton animatedButton;
    public MemoryButton animatedButton2;

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

    public static void setBusy(boolean busy) {
        isBusy = busy;
    }

    public static boolean skipped = false;

    private static boolean loading = false;

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

        Button backButton = getActivity().findViewById(R.id.button_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).clickSound.start();
                if(animator != null)
                    animator.cancel();
                if(animator2 != null)
                    animator2.cancel();
                if(animator3 != null)
                    animator3.cancel();
                animHandler.removeCallbacksAndMessages(null);
                ((MainActivity)getActivity()).gotoMenu();
            }
        });


        buttons = new MemoryButton[numberOfElements];


        //-------------------------------

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



       /* File dir = new File(Environment.getExternalStorageDirectory().getPath());
        File[] filelist;

        ImageView image;
        image = new ImageView(getActivity().getApplicationContext());
        filelist = dir.listFiles(IMAGE_FILTER);
        System.out.println("XX " + filelist[0].getName());*/

        /*if (dir.isDirectory()) {
           filelist = dir.listFiles(IMAGE_FILTER);

            for (File f : filelist) {
                // do your stuff here

                //Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                //Drawable d = new BitmapDrawable(getResources(), myBitmap);
                //ImageView myImage = (ImageView) getActivity().findViewById(R.id.imageviewTest);
                //myImage.setImageBitmap(myBitmap);


                Picasso.get().load(f).into(image);
                Drawable imageTest = image.getDrawable();


            }
        }*/

        //-------------------------------

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


                //Vrid brickorna - start
                //int buttonRotation = new Random().nextInt(10) - 5;
                //tempButton.setRotation(buttonRotation);
                //slut

                buttons[r * gameColumns + c] = tempButton;
                gridLayout.addView(tempButton);
            }
        }
        ((MainActivity)getActivity()).startSound.start();
        YoYo.with(Techniques.BounceInUp)
                .duration(900)
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
            //pl1Name.setTextColor(getResources().getColor(R.color.button_blue));
            //pl1Name.setTypeface(Typeface.DEFAULT_BOLD);
            //pl2Name.setTypeface(Typeface.DEFAULT);
            //pl2Name.setTextColor(Color.WHITE);
            pl1Bg.setBackground(getResources().getDrawable(R.drawable.player_bg_radius));
            pl2Bg.setBackground(null);
            turn = 1;
        }
        else if (turn == 1) {
            //pl1Name.setTextColor(Color.WHITE);
            //pl1Name.setTypeface(Typeface.DEFAULT);
            //pl2Name.setTextColor(getResources().getColor(R.color.button_blue));
            //pl2Name.setTypeface(Typeface.DEFAULT_BOLD);
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

    protected void animateMatched(final MemoryButton button2) {

        isBusy = true;
        setIsZooming(true);

        final DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

        final MemoryButton button1 = selectedButton1;



        animatedButton = button2;
        animatedButton2 = button1;

        button1.bringToFront();


        PropertyValuesHolder pvhX2 = PropertyValuesHolder.ofFloat("translationX", metrics.widthPixels / 2 - getViewCoords(button1, "x") - button1.getWidth() / 2);
        PropertyValuesHolder pvhY2 = PropertyValuesHolder.ofFloat("translationY",  gameFragmentContainer.getHeight() / 2 + gameFragmentContainer.getY() - getViewCoords(button1, "y") - button1.getHeight() / 2);
        PropertyValuesHolder pvhSX2 = PropertyValuesHolder.ofFloat("scaleX", 3.7f);
        PropertyValuesHolder pvhSY2 = PropertyValuesHolder.ofFloat("scaleY", 3.7f);
        //PropertyValuesHolder pvhA2 = PropertyValuesHolder.ofFloat("alpha", 0.5f);
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
        //button2.setClickable(false);
        button1.setClickable(false);

        gameFragmentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    System.out.println("XX click");
                    animator.cancel();
                    animator2.cancel();
                    skipped = true;
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                /*System.out.println("XX  Button dw " + metrics.widthPixels);
                System.out.println("XX  Button glx " + gridLayout.getX());
                System.out.println("XX  Button glsfx " + getViewCoords(gridLayout, "x"));
                System.out.println("XX  Button w " + button2.getWidth() * button2.getScaleX());
                System.out.println("XX  Button x " + button2.getX());
                System.out.println("XX  Button sx " + getViewCoords(button2, "x"));
                System.out.println("XX  Button gl-ps " + (gridLayout.getRight() - pairSymbol.getX()));*/

                    gameFragmentContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isBusy && isZooming) {
                                animHandler.removeCallbacksAndMessages(null);
                                button2.setVisibility(View.INVISIBLE);
                                addScore();
                                YoYo.with(Techniques.RubberBand)
                                        .duration(500)
                                        .repeat(0)
                                        .playOn(pl1ScoreText);
                                isBusy = false;
                                setIsZooming(false);
                                skipped = true;
                                return;
                            }
                        }
                    });

                    animHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            PropertyValuesHolder pvhX3 = PropertyValuesHolder.ofFloat("x", (button2.getWidth() * button2.getScaleX()) / 2 + (metrics.widthPixels / 2) - gridLayout.getX() - 20);
                            //PropertyValuesHolder pvhX3 = PropertyValuesHolder.ofFloat("x", metrics.widthPixels - getViewCoords(button2, "x") - gridLayout.getX());
                            PropertyValuesHolder pvhY3 = PropertyValuesHolder.ofFloat("y", (button2.getHeight() * button2.getScaleY()) / 2 - (metrics.heightPixels / 2) - gridLayout.getY() - (gameFragmentContainer.getY() / 2));
                            PropertyValuesHolder pvhSX3 = PropertyValuesHolder.ofFloat("scaleX", 0.32f / metrics.density);
                            PropertyValuesHolder pvhSY3 = PropertyValuesHolder.ofFloat("scaleY", 0.32f / metrics.density);
                            PropertyValuesHolder pvhR3 = PropertyValuesHolder.ofFloat("rotation", 75f);
                            //ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(button2, pvhX3, pvhY3, pvhSX3, pvhSY3);
                            animator3 = ObjectAnimator.ofPropertyValuesHolder(button2, pvhX3, pvhY3, pvhSX3, pvhSY3, pvhR3);
                            animator3.setInterpolator(new AccelerateDecelerateInterpolator());
                            animator3.setDuration(zoomOutDuration);
                            animator3.start();

                            gameFragmentContainer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (isBusy && isZooming) {
                                        animator3.cancel();
                                        skipped = true;
                                    }
                                }
                            });

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
        if (turn == 0) {
            pl1ScoreText.setText(numberMatched + "/" + numberOfElements/2);
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
        ((MainActivity)getActivity()).winSound.start();
    }


    @Override
    public void onClick(View view) {
        view.setSoundEffectsEnabled(false);

        if(isBusy && isZooming && view.getClass().getSimpleName() == "MemoryButton"){
            System.out.println("XX view ");
        }

        else if(isBusy) {
            System.out.println("XX I'm busy mom!");
            return;
        }

        final MemoryButton button = (MemoryButton) view;

        if(button.isMatched)
            return;

        if(selectedButton1 == null){
            ((MainActivity)getActivity()).flipSound.start();
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
            ((MainActivity)getActivity()).matchedSound.start();
            //((MainActivity)getActivity()).flipSound.start();
            button.flip();
            vibrator.vibrate(vibrateLong);

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
                        animatedButton = button;
                        animatedButton2 = selectedButton1;
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
                                showEndScore("Nils är vinnaren!");
                            }
                            else if (pl1Score < pl2Score) {
                                showEndScore("Gästen är vinnaren!");
                            }
                            else {
                                showEndScore("Det blev lika!");
                            }

                        }

                        else if (skipped){
                            showEndScore("Bra jobbat Nils!");
                            skipped = false;
                        }

                        else if (skipped == false){
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
            ((MainActivity)getActivity()).flipSound.start();
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
