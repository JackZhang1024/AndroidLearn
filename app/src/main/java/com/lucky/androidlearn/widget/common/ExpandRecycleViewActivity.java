package com.lucky.androidlearn.widget.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lucky.androidlearn.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpandRecycleViewActivity extends AppCompatActivity {

    @BindView(R.id.recycler)
    RecyclerView mRecycleList;
    MyRecycleAdapter myRecycleViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand, parent, false);
            MyRecycleViewHolder holder = new MyRecycleViewHolder(itemView, mDisplayClick);
            return holder;
        }

        @Override
        public int getItemCount() {
            return mListData.size();
        }

    }

    public interface OnDisplayClick {
        void onClick(int position);
    }

    class MyRecycleViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mDisplayContent;
        private TextView mDisplayText;
        private Button mBtnDisplay;
        private OnDisplayClick mOnDisplayClick;

        public MyRecycleViewHolder(View itemView, OnDisplayClick onDisplayClick) {
            super(itemView);
            mDisplayContent = itemView.findViewById(R.id.ll_hidden_content);
            mDisplayText = itemView.findViewById(R.id.txt_expand);
            mBtnDisplay = itemView.findViewById(R.id.btn_expand);
            mOnDisplayClick = onDisplayClick;
        }

        public void setItemData(int position, ItemData itemData) {
            boolean display = itemData.isDisplay();
            String displayText = itemData.getDisplayText();
            mDisplayText.setText(displayText);
            if (display) {
                mDisplayContent.setVisibility(View.VISIBLE);
            } else {
                mDisplayContent.setVisibility(View.GONE);
            }
            mBtnDisplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (display) {
                        itemData.setDisplay(false);
                    } else {
                        itemData.setDisplay(true);
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
    }

}
