package com.example.bjojoh17.memorytest;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.GridLayout;

public class MemoryButton extends android.support.v7.widget.AppCompatImageButton {

    protected int row;
    protected int column;
    protected int frontDrawableId;

    protected boolean isFlipped = false;
    protected boolean isMatched = false;

    protected Drawable front;
    protected Drawable back;


    public MemoryButton(Context context, int r, int c, int frontImageDrawableId) {

        super(context);

        row = r;
        column = c;
        frontDrawableId = frontImageDrawableId;

        front = ContextCompat.getDrawable(context, frontImageDrawableId);
        back = ContextCompat.getDrawable(context, R.drawable.button_question_mark);

        setBackground(back);

        GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(r), GridLayout.spec(c));

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int buttonMetrics = metrics.heightPixels / 5;
        //int metrics = (int) getResources().getDisplayMetrics().density * 150;

        /*tempParams.width = (int) getResources().getDisplayMetrics().density * 50 * 4;
        tempParams.height = (int) getResources().getDisplayMetrics().density * 50 * 3;

*/
        tempParams.width = (int) (buttonMetrics * 1.33);
        tempParams.height = buttonMetrics;
        tempParams.setMargins(buttonMetrics/4,buttonMetrics/4,buttonMetrics/4,buttonMetrics/4);

        setLayoutParams(tempParams);

    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public int getFrontDrawableId() {
        return frontDrawableId;
    }

    public void flip() {
        final MemoryButton tempButton = this;

        if(isMatched) {
            return;
        }

        if(isFlipped) {
            GameFragment.setBusy(true);
            //Animera brickan halvvägs (så den blir osynlig)
            ObjectAnimator animation = ObjectAnimator.ofFloat(tempButton, "rotationY", 360f, 270f);
            animation.setDuration(200);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.start();


            animation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    //Byta till framsidebilden
                    tempButton.setBackground(back);
                    isFlipped = false;

                    //Animera brickan färdigt
                    ObjectAnimator animation2 = ObjectAnimator.ofFloat(tempButton, "rotationY", 90f, 0f);
                    animation2.setDuration(200);
                    animation2.setInterpolator(new AccelerateDecelerateInterpolator());
                    animation2.start();
                    GameFragment.setBusy(false);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        else {
            //Animera brickan halvvägs (så den blir osynlig)
            ObjectAnimator animation = ObjectAnimator.ofFloat(tempButton, "rotationY", 0.0f, 90f);
            animation.setDuration(200);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.start();

            animation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    tempButton.setBackground(front);
                    isFlipped = true;

                    //Animera brickan färdigt
                    ObjectAnimator animation2 = ObjectAnimator.ofFloat(tempButton, "rotationY", 270f, 360f);
                    animation2.setDuration(200);
                    animation2.setInterpolator(new AccelerateDecelerateInterpolator());
                    animation2.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }
}
