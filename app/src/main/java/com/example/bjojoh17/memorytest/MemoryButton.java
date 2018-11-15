package com.example.bjojoh17.memorytest;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatDrawableManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.GridLayout;

public class MemoryButton extends Button {

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

        front = AppCompatDrawableManager.get().getDrawable(context, frontImageDrawableId);
        back = AppCompatDrawableManager.get().getDrawable(context, R.drawable.button_question_mark);

        setBackground(back);

        GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(r), GridLayout.spec(c));

        tempParams.width = (int) getResources().getDisplayMetrics().density * 150;
        tempParams.height = (int) getResources().getDisplayMetrics().density * 150;
        tempParams.topMargin = 20;
        tempParams.leftMargin = 20;
        tempParams.bottomMargin = 20;
        tempParams.rightMargin = 20;

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
        final MemoryButton tempButton = (MemoryButton) this;

        if(isMatched) {
            return;
        }

        if(isFlipped) {

            //Animera brickan halvvägs (så den blir osynlig)
            ObjectAnimator animation = ObjectAnimator.ofFloat(this, "rotationY", 360f, 270f);
            animation.setDuration(250);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.start();

            //Vänd brickan -180 grader
            tempButton.setRotationY(90f);
            this.setBackground(back);
            isFlipped = false;

            //Animera brickan färdigt
            ObjectAnimator animation2 = ObjectAnimator.ofFloat(this, "rotationY", 90f, 0f);
            animation2.setDuration(250);
            animation2.setInterpolator(new AccelerateDecelerateInterpolator());
            animation2.start();
        }
        else {
            //Animera brickan halvvägs (så den blir osynlig)
            ObjectAnimator animation = ObjectAnimator.ofFloat(this, "rotationY", 0.0f, 90f);
            animation.setDuration(250);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.start();

            //Vänd brickan 180 grader
            tempButton.setRotationY(270f);
            setBackground(front);
            isFlipped = true;

            //Animera brickan färdigt
            ObjectAnimator animation2 = ObjectAnimator.ofFloat(this, "rotationY", 270f, 360f);
            animation2.setDuration(250);
            animation2.setInterpolator(new AccelerateDecelerateInterpolator());
            animation2.start();
        }
    }
}
