package com.lucky.androidlearn.widget.common.scrollconflict;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

import com.lucky.androidlearn.R;

/**
 * <pre>
 * 功能描述    解决ListView与ScrollView冲突的问�?
 * Author:XXX
 * Create:2014-9-1
 * Modify:
 * 
 * </pre>
 */
public class NoScrollListView extends ListView {
	public ScrollView scrollView;
	public NoScrollListView(Context context) {
		super(context);
	}

	public NoScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	
	public void initView(Context context){
		LayoutInflater inflater=LayoutInflater.from(context);
		View view=inflater.inflate(R.layout.activity_scroll_conflict,null);
		scrollView=(ScrollView) view.findViewById(R.id.scrollview);
	}
	
	public void setParentScrollView(ScrollView scrollView){
		this.scrollView=scrollView;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			setParentScrollViewScrollAble(false);
			break;
		case MotionEvent.ACTION_UP:			
			break;
		case MotionEvent.ACTION_CANCEL:
			setParentScrollViewScrollAble(true);
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	public void setParentScrollViewScrollAble(boolean scrollAble){
		scrollView.requestDisallowInterceptTouchEvent(!scrollAble);
	}
	
}
