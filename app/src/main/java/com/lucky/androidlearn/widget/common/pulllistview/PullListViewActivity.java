package com.lucky.androidlearn.widget.common.pulllistview;


import java.util.LinkedList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.lucky.androidlearn.R;

public class PullListViewActivity extends Activity {
    private LinkedList<String> list;
    private BaseAdapter adapter;
    private MyListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pull);
        list = new LinkedList<String>();
        for (int i = 0; i < 30; i++) {
            list.add(String.valueOf(i));
        }
        listview = (MyListView) findViewById(R.id.mylistview);
//		ListAdapter adapter=new ListAdapter(this,list);
//		listview.setAdapter(adapter);

        adapter = new BaseAdapter() {

            @Override
            public View getView(int arg0, View convertView, ViewGroup arg2) {
                // TODO Auto-generated method stub
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_item, null);
                TextView textview = (TextView) convertView.findViewById(R.id.textview);
                textview.setText(list.get(arg0));
                return convertView;
            }

            @Override
            public long getItemId(int arg0) {
                // TODO Auto-generated method stub
                return arg0;
            }

            @Override
            public Object getItem(int arg0) {
                // TODO Auto-generated method stub
                return list.get(arg0);
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return list.size();
            }
        };
        listview.setAdapter(adapter);

        listview.setonRefreshListener(new MyListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... arg0) {
                        // TODO Auto-generated method stub
                        try {
                            Thread.sleep(5000);
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                        list.addFirst("刷新后的内容");
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        adapter.notifyDataSetChanged();
                        listview.onRefreshComplete();
                    }
                }.execute(null, null);
            }
        });
    }

}
