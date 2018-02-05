package com.lucky.androidlearn.rxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.lucky.androidlearn.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zfz
 * Created by zfz on 2018/3/13.
 */

public class ObserverActivity extends AppCompatActivity {

    @BindView(R.id.tv_result)
    TextView tvResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observer_pattern);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_result)
    public void onClick(View view) {
        Observer girlObserver = new GirlObserver("Jonse");
        MassageSubject massageSubject = new MassageSubject();
        massageSubject.attach(girlObserver);
        massageSubject.change("大保健");
    }

}
