package com.lucky.androidlearn.widget.common.pulllistview;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.lucky.androidlearn.R;

public class MyListView extends ListView implements OnScrollListener {
    private static final int RELEASE_TO_REFRESH = 0;//松开刷新
    private static final int PULL_TO_REFRESH = 1;//下拉刷新
    private static final int REFRESHING = 2;
    private static final int DONE = 3;
    private static final int LOADING = 4;
    private static final int RATIO = 3;
    private ImageView arrowImageView;
    private TextView tipsTextView, lastUpdateTextView;
    private ProgressBar progressbar;
    private LayoutInflater inflater;
    private LinearLayout headView;

    private int headContentWidth;
    private int headContentHeight;
    private int startY;

    private boolean isRefreshable;
    private boolean isBack;
    private boolean isRecored;//用于保证startY的值在一个完整的touch事件中只被记录一次

    private RotateAnimation animation;
    private RotateAnimation rotateanimation;

    private int state;//表示headView的状态

    private int firstItemIndex;

    private OnRefreshListener refreshListener;

    public MyListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initViews(context);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initViews(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initViews(context);
    }

    private void initViews(Context context) {
        inflater = LayoutInflater.from(context);
        headView = (LinearLayout) inflater.inflate(R.layout.head, null);
        arrowImageView = (ImageView) headView
                .findViewById(R.id.head_arrowImageView);
        arrowImageView.setMinimumHeight(70);
        arrowImageView.setMinimumWidth(50);
        tipsTextView = (TextView) headView.findViewById(R.id.head_tipsTextView);
        lastUpdateTextView = (TextView) headView
                .findViewById(R.id.head_lastUpdatedTextView);
        progressbar = (ProgressBar) headView.findViewById(R.id.head_progressBar);

        measureView(headView);

        headContentWidth = headView.getMeasuredWidth();
        headContentHeight = headView.getMeasuredHeight();

        headView.setPadding(0, -1 * headContentHeight, 0, 0);
        headView.invalidate();
        /**
         * Add a fixed view to appear at the top of the list. If addHeaderView
         * is called more than once, the views will appear in the order they
         * were added. Views added using this call can take focus if they want.
         * <p>
         * NOTE: Call this before calling setAdapter. This is so ListView can
         * wrap the supplied cursor with one that will also account for header
         * and footer views.
         *
         * @param v
         *            The view to add.
         */
        addHeaderView(headView, null, false);
        setOnScrollListener(this);// 给自定义View添加滚动监听

        animation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(250);
        animation.setFillAfter(true);

        rotateanimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateanimation.setInterpolator(new LinearInterpolator());
        animation.setDuration(200);
        animation.setFillAfter(true);

        isRefreshable = false;
        state = DONE;
    }

    /**
     * 设置子View的宽,高
     *
     * @param child
     */
    public void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
        int childwidthspec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpheight = p.height;
        int childheigtspec;
        if (lpheight > 0) {
            childheigtspec = MeasureSpec.makeMeasureSpec(lpheight,
                    MeasureSpec.EXACTLY);
        } else {
            childheigtspec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childwidthspec, childheigtspec);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stub
        firstItemIndex = firstVisibleItem;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (isRefreshable) {//如果可以刷新
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (firstItemIndex == 0 && !isRecored) {
                        isRecored = true;
                        startY = (int) ev.getY();
                        Log.v("info", "在down时候记录当前位置");
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    int tempY = (int) ev.getY();

                    if (!isRecored && firstItemIndex == 0) {
                        Log.i("info", "在move时候记录下位置");
                        isRecored = true;
                        startY = tempY;
                    }
                    if (state != REFRESHING && isRecored && state != LOADING) {
                        if (state == RELEASE_TO_REFRESH) {
                            setSelection(0);
                            if ((tempY - startY) / RATIO < headContentHeight
                                    && (tempY - startY) > 0) {
                                state = PULL_TO_REFRESH;
                                changeHeadViewByState();
                                Log.i("info", "由松开刷新状态转变为下拉刷新状态");
                            } else if (tempY - startY <= 0) {
                                state = DONE;
                                changeHeadViewByState();
                                Log.i("info", "由松开刷新状态转变到done状态");
                            } else {

                            }
                        }
                        if (state == PULL_TO_REFRESH) {
                            setSelection(0);
                            //下拉到可以进入RELEASE_TO_REFRESH的状态
                            if ((tempY - startY) / RATIO >= headContentHeight) {
                                state = RELEASE_TO_REFRESH;
                                isBack = true;
                                changeHeadViewByState();
                                Log.i("info", "由done或者下拉刷新状态转变到松开刷新");
                            } else if ((tempY - startY <= 0)) {
                                state = DONE;
                                changeHeadViewByState();

                                Log.i("info", "由DONE或者下拉刷新状态转到DONE状态");
                            }
                        }
                        //DONE状态下
                        if (state == DONE) {
                            if (tempY - startY > 0) {
                                state = PULL_TO_REFRESH;
                                changeHeadViewByState();
                            }
                        }
                        //更新headview的size
                        if (state == PULL_TO_REFRESH) {
                            headView.setPadding(0, -1 * headContentHeight + (tempY - startY) / RATIO,
                                    0, 0);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:

                    if (state != REFRESHING && state != LOADING) {
                        if (state == DONE) {
                            //什么都不做
                        }
                        if (state == PULL_TO_REFRESH) {
                            state = DONE;
                            changeHeadViewByState();

                            Log.v("info", "由下拉刷新状态，到done状态结束");
                        }
                        if (state == RELEASE_TO_REFRESH) {
                            state = REFRESHING;
                            changeHeadViewByState();
                            onRefresh();
                            Log.v("info", "由松开刷新状态，到donez状态");
                        }
                    }
                    isRecored = false;
                    isBack = false;
                    break;
            }

        }

        return super.onTouchEvent(ev);
    }

    public void setonRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
        isRefreshable = true;
    }

    private void onRefresh() {
        // TODO Auto-generated method stub
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
    }

    public void onRefreshComplete() {
        state = DONE;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String date = sdf.format(new Date());
        lastUpdateTextView.setText("最近更新：" + date);
        changeHeadViewByState();
    }

    private void changeHeadViewByState() {
        // TODO Auto-generated method stub
        switch (state) {
            case RELEASE_TO_REFRESH:
                arrowImageView.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);
                tipsTextView.setVisibility(View.VISIBLE);
                lastUpdateTextView.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.startAnimation(animation);
                tipsTextView.setText("松开刷新");
                Log.i("info", "当前状态,松开刷新");
                break;
            case PULL_TO_REFRESH:
                progressbar.setVisibility(View.GONE);
                tipsTextView.setVisibility(View.VISIBLE);
                lastUpdateTextView.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.VISIBLE);
                //是由RELEASE_TO_REFRESH状态转变来的
                if (isBack) {
                    isBack = false;
                    arrowImageView.clearAnimation();
                    arrowImageView.startAnimation(rotateanimation);
                    tipsTextView.setText("下拉刷新");
                } else {
                    tipsTextView.setText("下拉刷新");
                }
                Log.i("info", "当前状态，下拉刷新");
                break;
            case REFRESHING:
                headView.setPadding(0, 0, 0, 0);
                progressbar.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.GONE);
                tipsTextView.setText("正在刷新......");
                lastUpdateTextView.setVisibility(View.VISIBLE);
                Log.i("info", "当前状态，正在刷新.....");
                break;

            case DONE:
                headView.setPadding(0, -1 * headContentHeight, 0, 0);
                progressbar.setVisibility(View.GONE);
                arrowImageView.clearAnimation();
                arrowImageView.setImageResource(R.drawable.arrow_down);
                tipsTextView.setText("下拉刷新");
                lastUpdateTextView.setVisibility(View.VISIBLE);

                Log.i("info", "当前状态，DONE");

                break;
        }
    }

    public void setAdapter(BaseAdapter adapter) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
        String date = sdf.format(new Date());
        lastUpdateTextView.setText("最近更新 ：" + date);
        super.setAdapter(adapter);
    }

    public interface OnRefreshListener {
        public void onRefresh();
    }
}

