package com.lucky.androidlearn.widget.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lucky.androidlearn.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 流式标签布局
 *
 */
public class FlowLayoutActivity extends AppCompatActivity {

    private static final String TAG = "FlowLayoutActivity";

    @BindView(R.id.flowlayout)
    FlowLayout flowLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowlayout);
        ButterKnife.bind(this);
        addView();
    }


    private void addView(){
        //往容器内添加TextView数据
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 5, 10, 5);
        if (flowLayout != null) {
            flowLayout.removeAllViews();
        }
        List<String> labels = createList();
        for (int i = 0; i < labels.size(); i++) {
            TextView tv = new TextView(this);
            tv.setPadding(28, 10, 28, 10);
            tv.setText(labels.get(i));
            tv.setMaxEms(10);
            tv.setSingleLine();
            tv.setBackgroundResource(R.drawable.label_background);
            tv.setLayoutParams(layoutParams);
            flowLayout.addView(tv, layoutParams);
        }

    }

    private List<String> createList(){
        ArrayList<String> labels = new ArrayList<>();
        String[] texts = new String[]{"新闻", "娱乐", "连环画", "动脑学院", "知识学习"};
        Random random = new Random(System.currentTimeMillis());
        for (int index=0; index< 30; index++){
            String label = texts[random.nextInt(texts.length)];
            labels.add(label);
        }
        return labels;
    }

}
