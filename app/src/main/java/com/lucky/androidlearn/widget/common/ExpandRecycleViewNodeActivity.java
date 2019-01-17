package com.lucky.androidlearn.widget.common;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.soloader.SoLoader;
import com.facebook.yoga.YogaDisplay;
import com.facebook.yoga.YogaFlexDirection;
import com.facebook.yoga.YogaNode;
import com.facebook.yoga.android.YogaLayout;
import com.lucky.androidlearn.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpandRecycleViewNodeActivity extends AppCompatActivity {

    @BindView(R.id.recycler)
    RecyclerView mRecycleList;
    MyRecycleAdapter myRecycleViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SoLoader.init(this, false);
        setContentView(R.layout.activity_expand_list);
        ButterKnife.bind(this);
        mRecycleList.setLayoutManager(new LinearLayoutManager(this));
        mRecycleList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        myRecycleViewAdapter = new MyRecycleAdapter(createListData());
        mRecycleList.setAdapter(myRecycleViewAdapter);
    }


    class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleViewHolder> {
        private List<ItemData> mListData;
        private OnDisplayClick mDisplayClick;

        public MyRecycleAdapter(List<ItemData> listData) {
            this.mListData = listData;
            mDisplayClick = new OnDisplayClick() {
                @Override
                public void onClick(int position) {
                    //notifyDataSetChanged();
                    notifyItemChanged(position);
                }
            };
        }

        @Override
        public void onBindViewHolder(MyRecycleViewHolder holder, int position) {
            ItemData itemData = mListData.get(position);
            holder.setItemData(position, itemData);
        }

        @Override
        public MyRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = createItemView(parent.getContext());
            MyRecycleViewHolder holder = new MyRecycleViewHolder(itemView, mDisplayClick);
            return holder;
        }

        @Override
        public int getItemCount() {
            return mListData.size();
        }


        private YogaLayout createItemView(Context context) {
            YogaLayout itemView = new YogaLayout(context);
            YogaLayout.LayoutParams params = new YogaLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(params);
            YogaNode itemYogaNode = itemView.getYogaNode();
            itemYogaNode.setFlexDirection(YogaFlexDirection.COLUMN);

            // 添加Button
            YogaLayout btnYogaLayout = new YogaLayout(context);
            itemView.addView(btnYogaLayout);
            Button btnDisplay = new Button(context);
            btnDisplay.setTag("btn_layout");
            btnDisplay.setText("点击弹出");
            btnDisplay.setGravity(Gravity.CENTER);
            btnYogaLayout.addView(btnDisplay);

            //添加文字显示
            YogaLayout textYogaLayout = new YogaLayout(context);
            textYogaLayout.setTag("display_layout");
            YogaNode textYogaNode = textYogaLayout.getYogaNode();
            textYogaNode.setDisplay(YogaDisplay.NONE);
            textYogaLayout.setVisibility(View.GONE);
            itemView.addView(textYogaLayout);
            TextView textDisplay = new TextView(context);
            textDisplay.setTag("content_layout");
            textYogaLayout.addView(textDisplay);

            return itemView;
        }
    }

    public interface OnDisplayClick {
        void onClick(int position);
    }

    class MyRecycleViewHolder extends RecyclerView.ViewHolder {
        private YogaLayout mDisplayContent;
        private TextView mDisplayText;
        private Button mBtnDisplay;
        private OnDisplayClick mOnDisplayClick;
        private Map<Integer, YogaNode> mYogaMap = new HashMap<>();

        public MyRecycleViewHolder(View itemView, OnDisplayClick onDisplayClick) {
            super(itemView);
            mDisplayContent = itemView.findViewWithTag("display_layout");
            mDisplayText = itemView.findViewWithTag("content_layout");
            mBtnDisplay = itemView.findViewWithTag("btn_layout");
            mOnDisplayClick = onDisplayClick;
        }

        public void setItemData(int position, ItemData itemData) {
//            mDisplayContent = itemView.findViewWithTag("display_layout");
//            mDisplayText = itemView.findViewWithTag("content_layout");
//            mBtnDisplay = itemView.findViewWithTag("btn_layout");
            boolean display = itemData.isDisplay();
            int color = itemData.getColor();
            String displayText = itemData.getDisplayText();
            mBtnDisplay.setText("点击第" + position + "个");
            mDisplayText.setText(displayText);
            mBtnDisplay.setTextColor(color);
            mDisplayContent.setTag(position);
            YogaNode yogaNode = mDisplayContent.getYogaNode();
            Log.e("rq", "setItemData: " + yogaNode.getDisplay().intValue() + "  display " + display + "  " + mDisplayContent.getVisibility());
//            if (display) {
//                yogaNode.setDisplay(YogaDisplay.FLEX);
//                mDisplayContent.setVisibility(View.VISIBLE);
//            } else {
//                yogaNode.setDisplay(YogaDisplay.NONE);
//                mDisplayContent.setVisibility(View.GONE);
//            }
            //mDisplayContent.requestLayout();
            mBtnDisplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("rq", "onClick: " + display);
                   /* if (display) {
                        itemData.setDisplay(false);
                    } else {
                        itemData.setDisplay(true);
                    }*/
                    if (color == Color.YELLOW) {
                        itemData.setColor(Color.RED);
                    } else {
                        itemData.setColor(Color.YELLOW);
                    }
                    mOnDisplayClick.onClick(position);
                }
            });
        }

    }

    private List<ItemData> createListData() {
        ArrayList<ItemData> listData = new ArrayList<>();
        for (int index = 0; index < 40; index++) {
            ItemData itemData = new ItemData();
            itemData.setDisplay(false);
            itemData.setDisplayText("我是第" + index + "个");
            listData.add(itemData);
        }
        return listData;
    }

    class ItemData {
        private boolean display;
        private String displayText;
        private int color = Color.RED;

        public boolean isDisplay() {
            return display;
        }

        public void setDisplay(boolean display) {
            this.display = display;
        }

        public String getDisplayText() {
            return displayText;
        }

        public void setDisplayText(String displayText) {
            this.displayText = displayText;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }
    }

}
