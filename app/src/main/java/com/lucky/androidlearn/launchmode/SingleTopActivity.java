package com.lucky.androidlearn.launchmode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2017/11/26.
 */

public class SingleTopActivity extends AppCompatActivity {

    @BindView(R.id.tv_instance_name)
    TextView tvInstanceName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singletop);
        ButterKnife.bind(this);
        tvInstanceName.setText(this.toString());
    }

    @OnClick(R.id.btn_first_standard)
    public void onStandardClick(View view) {
        Intent intent = new Intent(this, StandardActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_singletop)
    public void onSingleTopClick(View view) {
        Intent intent = new Intent(this, SingleTopActivity.class);
        startActivity(intent);
    }
}
