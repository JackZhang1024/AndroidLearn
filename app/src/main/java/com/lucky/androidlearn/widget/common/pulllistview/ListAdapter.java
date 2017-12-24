package com.lucky.androidlearn.widget.common.pulllistview;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.lucky.androidlearn.R;

public class ListAdapter extends BaseAdapter {
   private Context context;
   private ArrayList<String> list;
   private LayoutInflater inflater;
   public ListAdapter(Context context,ArrayList<String> list){
	   this.context=context;
	   this.list=list;
	   inflater=LayoutInflater.from(context);
   }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView=inflater.inflate(R.layout.list_item,null);
		TextView tv=(TextView) convertView.findViewById(R.id.textview);
		String text=list.get(position);
		tv.setText(text);
		return convertView;
	}

}
