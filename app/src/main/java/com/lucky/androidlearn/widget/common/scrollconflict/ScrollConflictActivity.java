package com.lucky.androidlearn.widget.common.scrollconflict;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.lucky.androidlearn.R;

public class ScrollConflictActivity extends AppCompatActivity {
     
	private NoScrollListView noScorllView;
	private ScrollView scrollView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		scrollView=(ScrollView) findViewById(R.id.scrollview);
		noScorllView=(NoScrollListView) findViewById(R.id.noscrollview);
		noScorllView.setParentScrollView(scrollView);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,createObjs());
		noScorllView.setAdapter(adapter);
	}

	private String[] createObjs() {
		String[] objs=new String[100];
		for(int i=0;i<100;i++){
			objs[i]="zhangsan"+i;
		}
		return objs;
	}
}
