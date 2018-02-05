package com.lucky.androidlearn.dagger2learn;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.dagger2learn.learn00.Dagger2Main0Activity;
import com.lucky.androidlearn.dagger2learn.learn01.Dagger2MainActivity;
import com.lucky.androidlearn.dagger2learn.learn02.Dagger2Main2Activity;
import com.lucky.androidlearn.dagger2learn.learn03.Dagger2Main3Activity;
import com.lucky.androidlearn.dagger2learn.lesson04.Dagger2Main4Activity;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zfz
 */
public class DaggerLearnMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger2_learn_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_dagger2_0_learn)
    public void onDagger20Learn() {
        Intent intent = new Intent(this, Dagger2Main0Activity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_dagger2_learn)
    public void onDagger2Learn() {
        Intent intent = new Intent(this, Dagger2MainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_dagger2_2_learn)
    public void onDagger22Learn() {
        Intent intent = new Intent(this, Dagger2Main2Activity.class);
        startActivity(intent);
    }


    @OnClick(R.id.btn_dagger2_3_learn)
    public void onDagger23Learn() {
        Intent intent = new Intent(this, Dagger2Main3Activity.class);
        startActivity(intent);
    }


    @OnClick(R.id.btn_dagger2_4_learn)
    public void onDagger24Learn() {
        Intent intent = new Intent(this, Dagger2Main4Activity.class);
        startActivity(intent);
    }


    @OnClick(R.id.btn_dagger2_5_learn)
    public void onDagger25Learn() {
//        Intent intent = new Intent(this, Dagger2MainActivity.class);
//        startActivity(intent);
    }
}
