package com.lucky.androidlearn.animation.transitionanimation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.lucky.androidlearn.R;

/**
 * Created by zfz on 2017/1/4.
 */

public class TransitionTargetActivity extends AppCompatActivity {
    private CardView cardView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        cardView = (CardView) findViewById(R.id.cardview_search);
        cardView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                performEnterAnimation();
            }
        });
    }

    @Override
    public void onBackPressed() {
        performExitAnimation();
    }

    private void performEnterAnimation() {
        int[] locations = new int[2];
        cardView.getLocationOnScreen(locations);
        int searchXPosition = getIntent().getIntExtra(TransitionStartActivity.SEARCH_X_POSITION, 0);
        int searchYPosition = getIntent().getIntExtra(TransitionStartActivity.SEARCH_Y_POSITION, 0);
        int distance = searchYPosition - locations[1];
        cardView.setY(cardView.getY() + distance);
        float startY = cardView.getY();
        float endY = locations[1];
        System.out.println("Enter startY "+startY+" endY "+endY);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(startY, endY);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                cardView.setY((Float) animation.getAnimatedValue());
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setDuration(500);
        valueAnimator.start();
        ValueAnimator scaleVa = ValueAnimator.ofFloat(1, 0.8f);
        scaleVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                cardView.setScaleX((Float) valueAnimator.getAnimatedValue());
            }
        });
        scaleVa.setDuration(500);
        scaleVa.start();
    }

    //View的getY()方法获取的是View的左上角(顶部)位置距离父布局的顶部位置的高度
    private void performExitAnimation() {
        int[] locations = new int[2];
        cardView.getLocationOnScreen(locations);
        int searchXPosition = getIntent().getIntExtra(TransitionStartActivity.SEARCH_X_POSITION, 0);
        int searchYPosition = getIntent().getIntExtra(TransitionStartActivity.SEARCH_Y_POSITION, 0);
        int distance = searchYPosition - locations[1];
        float startY = cardView.getY();
        float endY   = locations[1]+distance;
        System.out.println("Exit startY "+startY+" endY "+endY);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(startY, endY);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translateY=(Float) animation.getAnimatedValue();
                cardView.setY(translateY);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setDuration(500);
        valueAnimator.start();
        ValueAnimator scaleVa = ValueAnimator.ofFloat(0.8f, 1.0f);
        scaleVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                cardView.setScaleX((Float) valueAnimator.getAnimatedValue());
            }
        });
        scaleVa.setDuration(500);
        scaleVa.start();
    }

}
