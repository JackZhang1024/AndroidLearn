package com.lucky.androidlearn.loadmore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lucky.androidlearn.R;

import java.util.ArrayList;
import java.util.List;

public class PullRefreshAdapter extends RecyclerView.Adapter<PullRefreshAdapter.PullRefreshViewHolder> {

    public List<ApiResponse> mDataList = new ArrayList<>();

    public PullRefreshAdapter(List<ApiResponse> responseList) {
        this.mDataList = responseList;
    }

    public void setData(List<ApiResponse> responses) {
        this.mDataList = responses;
        notifyDataSetChanged();
    }

    public void appendData(List<ApiResponse> responses) {
        this.mDataList.addAll(responses);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PullRefreshViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_response_item, viewGroup, false);
        return new PullRefreshViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PullRefreshViewHolder pullRefreshViewHolder, int position) {
        pullRefreshViewHolder.setItemData(mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class PullRefreshViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvMsg;

        public PullRefreshViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvMsg = itemView.findViewById(R.id.tv_msg);
        }

        public void setItemData(ApiResponse apiResponse) {
            mTvMsg.setText(apiResponse.getData());
        }
    }


}
