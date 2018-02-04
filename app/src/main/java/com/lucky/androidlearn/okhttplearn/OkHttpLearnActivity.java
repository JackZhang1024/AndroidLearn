package com.lucky.androidlearn.okhttplearn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.lucky.androidlearn.R;
import butterknife.ButterKnife;

/**
 * http://blog.csdn.net/hello_1s/article/details/76641527
 *
 * Created by zfz on 2018/2/4.
 */

public class OkHttpLearnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp_learn);
        ButterKnife.bind(this);

    }


}
