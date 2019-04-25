package com.lucky.androidlearn.widget.common;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;


/**
 * 重写ViewGroup的onMeasure和onLayout方法
 */
public class FlowLayout extends ViewGroup {

    private static final String TAG = "FlowLayout";

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int widthSize = 0;
        int heightSize = 0;

        Map<String, Integer> map = compute(widthSpecSize - getPaddingRight());
        if (widthSpecMode == MeasureSpec.EXACTLY) {
            widthSize = widthSpecSize;
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            // 如果不重新处理， AT MOST 是和父容器的宽度一样大
            widthSize = map.get("allWidth");
        }
        if (heightSpecMode == MeasureSpec.EXACTLY) {
            heightSize = heightSpecSize;
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            heightSize = map.get("allHeight");
        }
        Log.e(TAG, "onMeasure: width " + widthSize + " heightSize " + heightSize);
        setMeasuredDimension(widthSize, heightSize);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int index = 0; index < childCount; index++) {
            View childView = getChildAt(index);
            Rect rect = (Rect) childView.getTag();
            childView.layout(rect.left, rect.top, rect.right, rect.bottom);
        }
    }

    private Map<String, Integer> compute(int flowWidth) {
        // 是否单行
        boolean aRow = true;
        MarginLayoutParams marginParams;
        int rowsWidth = getPaddingLeft();
        int columnHeight = getPaddingTop();
        int rowsMaxHeight = 0;

        //遍历每个ChildView 获得宽度 和 高度
        for (int index = 0; index < getChildCount(); index++) {
            View childView = getChildAt(index);
            marginParams = (MarginLayoutParams) childView.getLayoutParams();
            int measuredWidth = childView.getMeasuredWidth();
            int childWidth = measuredWidth + marginParams.leftMargin + marginParams.rightMargin;

            int measuredHeight = childView.getMeasuredHeight();
            int childHeight = measuredHeight + marginParams.topMargin + marginParams.bottomMargin;

            rowsMaxHeight = Math.max(rowsMaxHeight, childHeight);

            // 如果已占用的宽度+子View的宽度大于flowWidth 换行
            if (rowsWidth + childWidth > flowWidth) {
                // 重置行款
                rowsWidth = getPaddingLeft() + getPaddingRight();
                // 累加的高度
                columnHeight += rowsMaxHeight;
                // 重置行中最大的高度
                rowsMaxHeight = childHeight;
                aRow = false;

            }
            rowsWidth += childWidth;
            Log.e(TAG, "compute: "+rowsWidth+"  childWidth "+childWidth+" childHeight "+childHeight+" leftMargin "+marginParams.leftMargin+" ");
            childView.setTag(new Rect(
                    rowsWidth - childWidth + marginParams.leftMargin,
                    columnHeight + marginParams.topMargin,
                    rowsWidth - marginParams.rightMargin,
                    columnHeight + childHeight - marginParams.bottomMargin
            ));
        }
        // 先考虑不换行情况
        Map<String, Integer> map = new HashMap<>();
        if (aRow) {
            // aRow  为true 表示不满一行  rowsWidth
            map.put("allWidth", rowsWidth);
        } else {
            // aRow 为false 表示多余一行 flowWidth
            map.put("allWidth", flowWidth);
        }
        // 计算出来的高度 = 当前的距离顶部的高度+本行中高度最大标签的高度+底部padding
        map.put("allHeight", columnHeight + rowsMaxHeight + getPaddingBottom());
        return map;
    }



    /**
     * 测量过程
     * @param flowWidth 该view的宽度
     * @return  返回子元素总所占宽度和高度（用于计算Flowlayout的AT_MOST模式设置宽高）
     */
//    private Map<String, Integer> compute(int flowWidth) {
//        //是否是单行
//        boolean aRow = true;
//        MarginLayoutParams marginParams;//子元素margin
//        int rowsWidth = getPaddingLeft();//当前行已占宽度(注意需要加上paddingLeft)
//        int columnHeight =getPaddingTop();//当前行顶部已占高度(注意需要加上paddingTop)
//        int rowsMaxHeight = 0;//当前行所有子元素的最大高度（用于换行累加高度）
//
//        for (int i = 0; i <  getChildCount(); i++) {
//
//            View child = getChildAt(i);
//            //获取元素测量宽度和高度
//            int measuredWidth = child.getMeasuredWidth();
//            int measuredHeight = child.getMeasuredHeight();
//            //获取元素的margin
//            marginParams = (MarginLayoutParams) child.getLayoutParams();
//            //子元素所占宽度 = MarginLeft+ child.getMeasuredWidth+MarginRight  注意此时不能child.getWidth,因为界面没有绘制完成，此时wdith为0
//            int childWidth = marginParams.leftMargin + marginParams.rightMargin + measuredWidth;
//            int childHeight = marginParams.topMargin + marginParams.bottomMargin + measuredHeight;
//            //判断是否换行： 该行已占大小+该元素大小>父容器宽度  则换行
//
//            rowsMaxHeight = Math.max(rowsMaxHeight, childHeight);
//            //换行
//            if (rowsWidth + childWidth > flowWidth) {
//                //重置行宽度
//                rowsWidth = getPaddingLeft()+getPaddingRight();
//                //累加上该行子元素最大高度
//                columnHeight += rowsMaxHeight;
//                //重置该行最大高度
//                rowsMaxHeight = childHeight;
//                aRow = false;
//            }
//            //累加上该行子元素宽度
//            rowsWidth += childWidth;
//            //判断时占的宽段时加上margin计算，设置顶点位置时不包括margin位置，不然margin会不起作用，这是给View设置tag,在onlayout给子元素设置位置再遍历取出
//            child.setTag(new Rect(rowsWidth - childWidth + marginParams.leftMargin, columnHeight + marginParams.topMargin, rowsWidth - marginParams.rightMargin, columnHeight + childHeight - marginParams.bottomMargin));
//        }
//
//        //返回子元素总所占宽度和高度（用于计算Flowlayout的AT_MOST模式设置宽高）
//        Map<String, Integer> flowMap = new HashMap<>();
//        //单行
//        if (aRow) {
//            flowMap.put("allChildWidth", rowsWidth);
//        } else {
//            //多行
//            flowMap.put("allChildWidth", flowWidth);
//        }
//        //FlowLayout测量高度 = 当前行顶部已占高度 +当前行内子元素最大高度+FlowLayout的PaddingBottom
//        flowMap.put("allChildHeight", columnHeight+rowsMaxHeight+getPaddingBottom());
//        return  flowMap;
//    }


}
