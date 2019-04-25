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

import java.util.ArrayList;


/*
 * 内部拦截法：
 * 父容器不拦截事件，所有的事件全部传递给子元素，如果子元素需要此事件就
 * 直接消耗掉，否则就交给父容器进行处理。
 *
 * 这种方法和Android中的事件分发机制不一样，需要配合getParent().requestDisallowInterceptTouchEvent
 * 方法才能正常工作，使用起来较外部拦截法稍显负责一点。
 *
 */

public class ScrollConflictDemo2Activity extends AppCompatActivity {

    private static final String TAG = "ScrollConflictDemo2";
    private HorizontalScrollViewEx2 mListContainer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_conflict_demo2);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = getLayoutInflater();
        mListContainer = (HorizontalScrollViewEx2) findViewById(R.id.container);
        final int screenWidth = AppScreenMgr.getScreenWidth(this);
        final int screenHeight = AppScreenMgr.getScreenHeight(this);

        for (int i = 0; i < 3; i++) {
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.content_layout_inner_intercept,
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
        ListViewEx listView = (ListViewEx) layout.findViewById(R.id.list);
        ArrayList<String> datas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            datas.add("name "+i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.item_scroll_conflict, R.id.name, datas) ;
        listView.setHorizontalScrollViewEx2(mListContainer);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppToastMgr.shortToast(ScrollConflictDemo2Activity.this, "click item  "+position);
            }
        });

    }

}
