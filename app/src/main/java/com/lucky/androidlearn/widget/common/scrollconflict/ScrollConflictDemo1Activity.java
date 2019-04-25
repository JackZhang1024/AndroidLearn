package com.lucky.androidlearn.widget.common.scrollconflict;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jingewenku.abrahamcaijin.commonutil.AppScreenMgr;
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.dagger2learn.lesson04.ToastManager;

import java.util.ArrayList;

/**
 * 外部拦截法
 */
public class ScrollConflictDemo1Activity extends AppCompatActivity {

    HorizontalScrollViewEx mListContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_conflict_demo1);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = getLayoutInflater();
        mListContainer = (HorizontalScrollViewEx) findViewById(R.id.container);
        final int screenWidth = AppScreenMgr.getScreenWidth(this);
        final int screenHeight = AppScreenMgr.getScreenHeight(this);

        for (int i = 0; i < 3; i++) {
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.content_layout,
                    mListContainer, false);
            layout.getLayoutParams().width = screenWidth;
            TextView textView = layout.findViewById(R.id.title);
            textView.setText("page " + (i + 1));
            layout.setBackgroundColor(Color.rgb(255 / (i + 1), 255 / (i + 1), 0));
            createList(layout);
            mListContainer.addView(layout);
        }

    }

    private void createList(ViewGroup layout) {
        ListView listView = layout.findViewById(R.id.list);
        ArrayList<String> datas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
             datas.add("name "+i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.item_scroll_conflict, R.id.name, datas) ;

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppToastMgr.shortToast(ScrollConflictDemo1Activity.this,
                        "click item  "+position);
            }
        });

    }


}
