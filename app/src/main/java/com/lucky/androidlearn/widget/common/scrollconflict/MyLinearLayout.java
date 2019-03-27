package com.lucky.androidlearn.widget.common.scrollconflict;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 瀑布流
 */

public class MyLinearLayout extends LinearLayout {


    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 事件传递机制：
     * 1. view执行dispatchTouchEvent方法，开始分发事件
     * 2. 执行onInterceptTouchEvent 判断是否中断事件
     * 3. 执行onTouchEvent 方法，处理事件
     *
     */


    /**
     * 分发事件的方法，最早执行
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int width = getWidth() / getChildCount();
        int height = getHeight();

        int count = getChildCount();

        float eventX = event.getX();
        // 滑动左边的listView
        if (eventX < width) {
            // 告诉左边的listView, 事件触发点在左边的listView的x坐标的一半处（y坐标随意滑动）
            event.setLocation(width / 2, event.getY());

            float eventY = event.getY();
            if (eventY < height / 2) {
                event.setLocation(width / 2, event.getY());
                getChildAt(0).dispatchTouchEvent(event);
                getChildAt(2).dispatchTouchEvent(event);
                System.out.println("左边的ListView的上班部分 " + eventY);
                return true;
            } else if (eventY > height / 2) {
                event.setLocation(width / 2, event.getY());
                try {
                    getChildAt(0).dispatchTouchEvent(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("左边的ListView的下半部分： " + eventY);
                return true;
            }
            return true;
        } else if (eventX > width && eventX < width * 2) {
            // 滑动中间的ListView
            float eventY = event.getY();
            // 滑动中间的listView上半部分（0 < eventY< height/2）
            if (eventY < height / 2) {
                // 告诉中间的listview,事件触发点在中间的ListVIew的x坐标
                // 的一半处（y坐标随意滑动）
                event.setLocation(width / 2, event.getY());
                for (int i = 0; i < count; i++) {
                    View child = getChildAt(i);
                    try {
                        child.dispatchTouchEvent(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("中间listview的上半部分 " + eventY);
                return true;
            } else if (eventY > height / 2) {
                // 滑动中间的listView下半部分（height/2 < eventY<height）
                // 告诉中间的listView,事件触发点在中间listView的x坐标一半处（y坐标随意滑动）
                event.setLocation(width / 2, event.getY());
                try {
                    getChildAt(1).dispatchTouchEvent(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("中间listView的下半部分 " + eventY);
                return true;
            }
            return true;
        } else if (eventX > width * 2) {
            // 滑动右边的listView
            // event.setLocation(width/2, event.getY());
            // 告诉右边的listView,事件触发点在右边listView的X坐标一半处（y坐标随意滑动）
            float eventY = event.getY();
            if (eventY < height / 2) {
                event.setLocation(width / 2, event.getY());
                getChildAt(0).dispatchTouchEvent(event);
                getChildAt(2).dispatchTouchEvent(event);
                System.out.println("右边listView的上半部分： " + event.getY());
                return true;
            } else if (eventY > height / 2) {
                event.setLocation(width / 2, event.getY());
                try {
                    getChildAt(2).dispatchTouchEvent(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("右边listView的下半部分： " + eventY);
                return true;
            }
            return true;
        }
        System.out.println("最后返回");
        return true;
    }
}
