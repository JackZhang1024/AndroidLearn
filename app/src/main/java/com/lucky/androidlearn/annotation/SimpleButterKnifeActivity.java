package com.lucky.androidlearn.annotation;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;
import com.lucky.androidlearn.R;
import com.lucky.simplebutterknife.annotations.SimpleBindView;

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
        mBtnShow.setOnClickListener(v->{
            AppToastMgr.show(this, "Hello World!");
        });
    }
}
