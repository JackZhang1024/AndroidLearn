package com.lucky.androidlearn.annotation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import com.lucky.androidlearn.R;
import com.lucky.simplebutterknife_annotations.SimpleBindView;

public class SimpleButterKnifeActivity extends AppCompatActivity {

    @SimpleBindView(R.id.tv_content)
    TextView mTvContent;

    @SimpleBindView(R.id.btn_show)
    Button mBtnShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplebutterknife);
        SimpleButterKnife.bind(this);
        mTvContent.setText("简单的ButterKnife");
    }
}
