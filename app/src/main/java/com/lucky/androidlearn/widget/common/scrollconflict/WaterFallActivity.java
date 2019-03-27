package com.lucky.androidlearn.widget.common.scrollconflict;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.lucky.androidlearn.R;

//手写瀑布流  https://www.cnblogs.com/hebao0514/p/4854718.html
public class WaterFallActivity extends AppCompatActivity {

    private ListView listView0;
    private ListView listView1;
    private ListView listView2;

    private int ids[] = new int[]{R.drawable.pic_flower, R.drawable.pic_flower, R.drawable.pic_flower, R.drawable.pic_flower};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waterfall);
        listView0 = (ListView) findViewById(R.id.lv0);
        listView1 = (ListView) findViewById(R.id.lv1);
        listView2 = (ListView) findViewById(R.id.lv2);
        MyAdapter adapter0 = new MyAdapter(this, ids);
        MyAdapter adapter1 = new MyAdapter(this, ids);
        MyAdapter adapter2 = new MyAdapter(this, ids);
        listView0.setAdapter(adapter0);
        listView1.setAdapter(adapter1);
        listView2.setAdapter(adapter2);
    }


    class MyAdapter extends BaseAdapter {

        private Context mContext;
        private int[] mImages;
        private LayoutInflater mInflater;

        public MyAdapter(Context context, int[] images) {
            this.mContext = context;
            this.mImages = images;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 3000;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_waterfall, null);
                viewHolder.imageView = convertView.findViewById(R.id.iv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            int index = (int) Math.random() * 4;
            viewHolder.imageView.setImageResource(mImages[index]);
            return convertView;
        }
    }


    public final class ViewHolder {

        ImageView imageView;


    }


}
