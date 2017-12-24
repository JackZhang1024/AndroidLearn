package com.lucky.androidlearn.animation.transitionanimation;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import com.lucky.androidlearn.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2018/1/2.
 */

public class TransitionMainStartActivity extends AppCompatActivity {

    @BindView(R.id.btn_share)
    View btnShare;
    @BindView(R.id.btn_fab)
    View btnFab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tranistion_main_start);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn_explode)
    public void onExplodeClick(View view) {
        Intent intent = new Intent(this, TransitionMainTargetActivity.class);
        intent.putExtra("flag", 0);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
    }

    @OnClick(R.id.btn_slide)
    public void onSlideClick(View view) {
        Intent intent = new Intent(this, TransitionMainTargetActivity.class);
        intent.putExtra("flag", 1);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
    }

    @OnClick(R.id.btn_fade)
    public void onFadeClick(View view) {
        Intent intent = new Intent(this, TransitionMainTargetActivity.class);
        intent.putExtra("flag", 2);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
    }

    @OnClick(R.id.btn_share)
    public void onShareClick(View view) {
        Intent intent = new Intent(this, TransitionMainTargetActivity.class);
        intent.putExtra("flag", 3);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            //startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, btnShare, "share").toBundle());
            Pair<View, String> pairA = Pair.create(btnShare, "share");
            Pair<View, String> pairB = Pair.create(btnFab, "fab");
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, pairA, pairB).toBundle());
        }
    }

    @OnClick(R.id.btn_custom)
    public void onCustomClick(View view){
        startActivity(new Intent(this, TransitionStartActivity.class));
    }


}
