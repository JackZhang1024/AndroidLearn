package com.lucky.androidlearn.mvvm.learn;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.databinding.ItemDatabindingRvBinding;

/**
 * Created by zfz on 2018/2/6.
 */

public class DBRecycleAdapter extends RecyclerView.Adapter<DBRecycleAdapter.DBViewHolder> {

    private String[] dataArray;

    public DBRecycleAdapter(String[] data) {
        this.dataArray = data;
    }

    @Override
    public DBRecycleAdapter.DBViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemDatabindingRvBinding itemDatabindingRvBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_databinding_rv, parent, false);
        return new DBViewHolder(itemDatabindingRvBinding);
    }

    @Override
    public void onBindViewHolder(DBRecycleAdapter.DBViewHolder holder, int position) {
        ItemDatabindingRvBinding binding = holder.getDatabindingRvBinding();
        binding.rvItem.setOnClickListener((v -> {
            if (onItemClickListener!=null){
                String item = dataArray[position];
                onItemClickListener.onItemClick(position, item);
            }
        }));
        binding.setStr(dataArray[position]);
    }

    @Override
    public int getItemCount() {
        return dataArray.length;
    }

    class DBViewHolder extends RecyclerView.ViewHolder {
        private ItemDatabindingRvBinding databindingRvBinding;

        public DBViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());
            this.databindingRvBinding = (ItemDatabindingRvBinding) viewDataBinding;
        }

        public void setDatabindingRvBinding(ItemDatabindingRvBinding databindingRvBinding) {
            this.databindingRvBinding = databindingRvBinding;
        }

        public ItemDatabindingRvBinding getDatabindingRvBinding() {
            return databindingRvBinding;
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position, String str);
    }
}
