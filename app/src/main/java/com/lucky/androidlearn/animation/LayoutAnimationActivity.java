package com.lucky.androidlearn.animation;

import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 布局动画
 * http://www.cnblogs.com/whoislcj/p/5802899.html
 * Created by zfz on 2018/1/1.
 */

public class LayoutAnimationActivity extends AppCompatActivity {

    @BindView(R.id.content)
    LinearLayout llContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_animation);
        ButterKnife.bind(this);
        setLayoutAnimation();
    }

    @OnClick(R.id.btn_add_child)
    public void addChildView() {
        TextView textView = new TextView(this);
        textView.setText("哈哈");
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setTextSize(20);
        llContent.addView(textView);
    }

    private void setLayoutAnimation() {
//        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1);
//        scaleAnimation.setDuration(2000);
//        LayoutAnimationController lac = new LayoutAnimationController(scaleAnimation, 0.5f);
//        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
//        llContent.setLayoutAnimation(lac);
//        llContent.startLayoutAnimation();

        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(2000);
        LayoutAnimationController animationController = new LayoutAnimationController(alphaAnimation, 0.5f);
        animationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
        llContent.setLayoutAnimation(animationController);
    }


}
