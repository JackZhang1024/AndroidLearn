package com.lucky.androidlearn.animation.curveanimation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

public class CurveAnimationMainActivity extends AppCompatActivity {

    private TextView move;
    private ImageView imageView;
    private RelativeLayout relativeLayout;
    private int targetX;
    private int targetY;

    //屏幕中心点X坐标
    private int srcX = 0;

    //屏幕中心点Y坐标
    private int srcY = 0;
    //父布局定点X坐标
    private int parentX;
    //父布局定点Y坐标
    private int parentY;
    private PathMeasure pathMeasure;
    private float[] mCurrentPositions = new float[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curve_main);
        Button btnSend = (Button) findViewById(R.id.send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //curveMove();
                //showToast();
                //bezierMove();
                showDoneDialog();
            }
        });
        imageView = (ImageView) findViewById(R.id.engulf);
        move = (TextView) findViewById(R.id.move);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        Button btnSlideUp = (Button) findViewById(R.id.slide_up);
        btnSlideUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //在这块进行位置的计算
        //计算出起始View和目标View的坐标
        getLocations();
    }

    private void getLocations() {
        int[] destLocations = new int[2];
        imageView.getLocationInWindow(destLocations);
        targetX = destLocations[0];
        targetY = destLocations[1];
        //srcLocations计算的时window的中心点坐标
        //window的高度包括了ToolBar的高度
        int[] srcLocations = new int[2];
        //move.getLocationInWindow(srcLocations);
        srcLocations = getScreenCenterPosition();
        srcX = srcLocations[0];
        srcY = srcLocations[1];

        int[] parentLocations = new int[2];
        relativeLayout.getLocationInWindow(parentLocations);
        parentX = parentLocations[0];
        parentY = parentLocations[1];
    }

    //of Int中的参数是指的是某一个变量的开始和结束的数值
    //可以假定1000为view开始动画时的x轴坐标 400为结束动画时x轴坐标
    private void curveMove() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(srcX, targetX);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int x = (Integer) valueAnimator.getAnimatedValue();
                int y = 2 * x;
                System.out.println("x " + x + " y " + y);
                moveView(x, y, move);
            }
        });
        valueAnimator.setDuration(3000);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.start();
    }

    private void moveView(int rawX, int rawY, View movedView) {
        int left  = rawX - movedView.getWidth() / 2;
        int top   = rawY - movedView.getHeight() / 2;
        int right = left + movedView.getWidth();
        int bottom= top + movedView.getHeight();
        movedView.layout(left, top, right, bottom);
    }


    //贝塞尔曲线移动动画
    private void bezierMove() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(100, 100);
        //RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(100,100);
        final TextView moveCopy = new TextView(this);
        moveCopy.setLayoutParams(layoutParams);
        relativeLayout.addView(moveCopy, layoutParams);
        relativeLayout.removeView(move);
        //动画开始的坐标
        int startX = srcX - parentX + move.getWidth() / 2;
        int startY = srcY - parentY + move.getHeight() / 2;
        //动画结束的坐标
        int endX = targetX - parentX + imageView.getWidth() / 5;
        int endY = targetY - parentY;
        System.out.println(" parentX  " + parentX + " parentY  " + parentY);
        System.out.println("startX " + startX + " startY " + startY + " endX " + endX + " endY " + endY);
        //设置贝塞尔曲线路径
        Path path = new Path();
        //将贝塞尔曲线移动到开始中心点位置
        path.moveTo(startX, startY);
        //设置贝塞尔曲线中间位置的点坐标
        path.quadTo((startX + endX) / 2, startY, endX, endY);
        pathMeasure = new PathMeasure(path, false);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, pathMeasure.getLength());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float distance = (Float) valueAnimator.getAnimatedValue();
                //distance是当前位置的距离 然后根据距离来获取对应的切线与曲线相切的点的坐标（mCurrentPositions）
                pathMeasure.getPosTan(distance, mCurrentPositions, null);
                float currentPositionX = mCurrentPositions[0];
                float currentPositionY = mCurrentPositions[1];
                System.out.println("currentPositionX " + currentPositionX + " currentPositionY " + currentPositionY);
                //move.setTranslationX(currentPositionX);
                //move.setTranslationY(currentPositionY);
                moveCopy.setTranslationX(currentPositionX);
                moveCopy.setTranslationY(currentPositionY);
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(1000);
        valueAnimator.start();
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //relativeLayout.removeView(move);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void showToast() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.toast_added, null);
        RelativeLayout relativeDone = (RelativeLayout) view.findViewById(R.id.relative_done);
        Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
        bezierToastMove();
    }

    //贝塞尔曲线移动动画
    private void bezierToastMove() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(100, 100);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View toastView = inflater.inflate(R.layout.toast_added, null);
        toastView.setLayoutParams(layoutParams);
        relativeLayout.addView(toastView, layoutParams);
        //relativeLayout.removeView(move);
        //动画开始的坐标
        int startX = srcX - parentX + toastView.getWidth() / 2;
        int startY = srcY - parentY + toastView.getHeight() / 2;
        //动画结束的坐标
        int endX = targetX - parentX + imageView.getWidth() / 5;
        int endY = targetY - parentY;
        System.out.println(" parentX  " + parentX + " parentY  " + parentY);
        System.out.println("startX " + startX + " startY " + startY + " endX " + endX + " endY " + endY);
        //设置贝塞尔曲线路径
        Path path = new Path();
        //将贝塞尔曲线移动到开始中心点位置
        path.moveTo(startX, startY);
        //设置贝塞尔曲线中间位置的点坐标
        path.quadTo((startX + endX) / 2, startY, endX, endY);
        pathMeasure = new PathMeasure(path, false);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, pathMeasure.getLength());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float distance = (Float) valueAnimator.getAnimatedValue();
                //distance是当前位置的距离 然后根据距离来获取对应的切线与曲线相切的点的坐标（mCurrentPositions）
                pathMeasure.getPosTan(distance, mCurrentPositions, null);
                float currentPositionX = mCurrentPositions[0];
                float currentPositionY = mCurrentPositions[1];
                System.out.println("currentPositionX " + currentPositionX + " currentPositionY " + currentPositionY);
                //move.setTranslationX(currentPositionX);
                //move.setTranslationY(currentPositionY);
                toastView.setTranslationX(currentPositionX);
                toastView.setTranslationY(currentPositionY);
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(1000);
        valueAnimator.start();
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //relativeLayout.removeView(move);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private ClickDoneDialog dialog;

    private void showDoneDialog() {
        dialog = new ClickDoneDialog(this, R.style.dialog);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 300, getResources().getDisplayMetrics());
        layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 300, getResources().getDisplayMetrics());
        layoutParams.x = srcX - layoutParams.width / 2;
        layoutParams.y = srcY - layoutParams.height / 2;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setGravity(Gravity.TOP | Gravity.START);
        dialog.show();
        ImageView circle = dialog.getCircle();
        //((RelativeLayout) circle.getParent()).removeView(circle);
        //bezierCircleMove(circle);
        bezierCircleFadIn(circle);
    }

    //贝塞尔曲线移动动画
    private void bezierCircleMove(final View circle) {
        FrameLayout topView = (FrameLayout) getWindow().getDecorView();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(60, 60);
        circle.setLayoutParams(layoutParams);
        relativeLayout.addView(circle, layoutParams);
        //动画开始的坐标
        int startX = srcX - parentX;
        int startY = srcY - parentY;
        //动画结束的坐标
        int endX = targetX - parentX + imageView.getWidth() / 5;
        int endY = targetY - parentY;
        //控制点坐标
        int controlX = (startX + endX) / 2;
        int controlY = (startY + endY) / 5;
        final int[] srcLocations = new int[]{startX, startY};
        // 设置贝塞尔曲线路径
        Path path = new Path();
        // 将贝塞尔曲线移动到开始中心点位置
        path.moveTo(startX - 50, startY);
        // 设置贝塞尔曲线中间位置的点坐标
        // path.quadTo((startX + endX) /3, startY, endX, endY);
        path.quadTo(controlX, controlY, endX, endY);
        pathMeasure = new PathMeasure(path, false);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, pathMeasure.getLength());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float distance = (Float) valueAnimator.getAnimatedValue();
                //distance是当前位置的距离 然后根据距离来获取对应的切线与曲线相切的点的坐标（mCurrentPositions）
                pathMeasure.getPosTan(distance, mCurrentPositions, null);
                float currentPositionX = mCurrentPositions[0];
                float currentPositionY = mCurrentPositions[1];
                //System.out.println("currentPositionX "+currentPositionX+" currentPositionY "+currentPositionY);
                //move.setTranslationX(currentPositionX);
                //move.setTranslationY(currentPositionY);
                circle.setTranslationX(currentPositionX);
                circle.setTranslationY(currentPositionY);
                float scale = (float) (0.4 * (distance / pathMeasure.getLength()) + 1.6);
                //imageView.setScaleX((distance / pathMeasure.getLength() + 1));
                //imageView.setScaleY((distance / pathMeasure.getLength() + 1));
                //判断circle是否还在对话框中
                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 300, getResources().getDisplayMetrics());
                boolean isInRange = isInRange(width, mCurrentPositions, srcLocations);
                imageView.setScaleX(scale);
                imageView.setScaleY(scale);
                if (isInRange) {
                    dialog.getWindow().getDecorView().setAlpha(1 - distance / pathMeasure.getLength());
                } else {
                    dialog.dismiss();
                }
            }
        });
        //valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(1000);
        valueAnimator.start();
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                dialog.dismiss();
                //relativeLayout.removeView(circle);
                imageView.setScaleX(0.9f);
                imageView.setScaleY(0.9f);

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void bezierCircleFadIn(final ImageView circle) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (Float) animation.getAnimatedValue();
                dialog.getWindow().getDecorView().setAlpha(alpha);
                //设置
                float scale = 0.5f * alpha + 1;
                imageView.setScaleX(scale);
                imageView.setScaleY(scale);
            }
        });
        valueAnimator.start();
        valueAnimator.setDuration(1000);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ((RelativeLayout) circle.getParent()).removeView(circle);
                bezierCircleMove(circle);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private int[] getScreenCenterPosition() {
        // 1.获取屏幕的宽高
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        // 2.获取状态栏的高度
        // getDecorView获取得到的DecorView是window最顶层的View
        // DecoreView不包括状态栏 它的顶部坐标恰好是状态栏的高度
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int stateHeight = frame.top;
        // 3.屏幕高度减去状态栏高度 就是整个contentView的高度
        int screenHeight = height - stateHeight;
        // 4.ContentView 就是标题栏之下的部分视图
        View contentView = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        int contentViewHeight = contentView.getHeight();
        int titleHeight = contentView.getTop();
        System.out.println("屏幕高度 " + screenHeight + " 内容高度 " + contentViewHeight + " 标题栏高度 " + titleHeight + "状态栏高度 " + stateHeight);
        int[] centeralPositions = new int[2];
        centeralPositions[0] = width / 2;
        centeralPositions[1] = screenHeight / 2;
        return centeralPositions;
    }

    /**
     * true still in Range
     */
    private boolean isInRange(float width, float[] currentLocations, int[] srcLocations) {
        double squarRadius = Math.pow(width / 2, 2) * 2;
        double squarDistance = Math.pow((currentLocations[0] - srcLocations[0]), 2) + Math.pow((currentLocations[1] - srcLocations[1]), 2);
        return squarRadius > squarDistance;
    }
}
