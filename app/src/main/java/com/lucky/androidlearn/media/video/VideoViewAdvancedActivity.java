package com.lucky.androidlearn.media.video;

import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jingewenku.abrahamcaijin.commonutil.DensityUtils;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.core.util.TimeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 加上configChanges则不会重新走生命周期方法onCreate方法也不会调用 同时在屏幕方向发生变化的时候会调用onConfigChanged方法
 * Created by zfz on 2018/1/18.
 */

public class VideoViewAdvancedActivity extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {
    private static final String TAG = "VideoViewAdvanced";

    @BindView(R.id.rl_video)
    RelativeLayout mRlVideo;

    @BindView(R.id.vv_player)
    FullScreenVideoView mVideoView;
    //VideoView mVideoView;

    // 底部的控制面板
    @BindView(R.id.ll_control)
    LinearLayout mLLControl;

    // 播放器进度条
    @BindView(R.id.sb_play)
    SeekBar mPlaySeekBar;

    // 播放按钮控制面板
    @BindView(R.id.ll_playControl)
    LinearLayout mLLPlayControl;

    // 当前视频播放时间进度
    @BindView(R.id.tv_currentTime)
    TextView mTvCurrentTime;

    // 视频播放总时间
    @BindView(R.id.tv_totalTime)
    TextView mTvTotalTime;

    // 播放暂停控制按钮
    @BindView(R.id.iv_playControl)
    ImageView mImagePlayControl;

    @BindView(R.id.iv_screenSwitch)
    ImageView mImageScreenSwitch;

    @BindView(R.id.ll_volumeControl)
    LinearLayout mLLVolumeControl;
    @BindView(R.id.iv_volume)
    ImageView mImageVolume;
    @BindView(R.id.sb_volume)
    SeekBar mVolumeSeekBar;

    // 屏幕宽度(横屏或者竖屏下的屏幕宽度)
    private int screenWidth;
    // 屏幕高度(横屏或者书评下的屏幕高度)
    private int screenHeight;
    // 音频管理器
    private AudioManager mAudioManager;
    GestureDetector mGestureDetector;
    private VolumeReceiver mVolumeReceiver;
    private int mCurrentPosition;
    private static final int UPDATE_TIME = 1;
    private Handler uiHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int currentTime = mVideoView.getCurrentPosition();
            mTvCurrentTime.setText(TimeUtil.updatePlayTimeFormat(currentTime));
            mPlaySeekBar.setProgress(currentTime);
            uiHandler.sendEmptyMessageDelayed(UPDATE_TIME, 500);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoview_advanced);
        ButterKnife.bind(this);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mGestureDetector = new GestureDetector(this, this);
        mVolumeSeekBar.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        mVolumeSeekBar.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        addListeners();
        registerReceiver();
        //setVideoPath("big_buck_bunny.mp4");
        setLocalVideoPath();
        //setNetVideoUrl();
    }

    private void setVideoPath(String videoPath) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/" + videoPath;
        mVideoView.setVideoPath(path);
    }

    private void setLocalVideoPath() {
        mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.big_buck_bunny));
    }

    private void setNetVideoUrl() {
        String url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        mVideoView.setVideoURI(Uri.parse(url));
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.start();
            }
        });
    }

    private void registerReceiver() {
        mVolumeReceiver = new VolumeReceiver(this, mImageVolume, mVolumeSeekBar);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(mVolumeReceiver, filter);
    }

    private void unregisterReceiver() {
        if (mVolumeReceiver != null) {
            unregisterReceiver(mVolumeReceiver);
        }
    }

    private void addListeners() {
        mImagePlayControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoView.isPlaying()) {
                    setPauseStatus();
                    mVideoView.pause();
                    uiHandler.removeMessages(UPDATE_TIME);
                } else {
                    setPlayStatus();
                    mVideoView.start();
                    uiHandler.sendEmptyMessage(UPDATE_TIME);
                }
            }
        });
        mPlaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mVideoView.seekTo(progress);
                    mTvCurrentTime.setText(TimeUtil.updatePlayTimeFormat(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                uiHandler.removeMessages(UPDATE_TIME);
                if (!mVideoView.isPlaying()) {
                    setPauseStatus();
                    mVideoView.start();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                uiHandler.sendEmptyMessage(UPDATE_TIME);
            }
        });
        mVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mImagePlayControl.setImageResource(R.drawable.play_btn_style);
                mVideoView.seekTo(0);
                mPlaySeekBar.setProgress(0);
                mTvCurrentTime.setText(TimeUtil.updatePlayTimeFormat(0));
                mVideoView.pause();
                uiHandler.removeMessages(UPDATE_TIME);
            }
        });
        mImageScreenSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mImageScreenSwitch.setImageResource(R.drawable.full_screen);
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    mImageScreenSwitch.setImageResource(R.drawable.exit_full_screen);
                }
            }
        });
        mVideoView.setOnTouchListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView.canSeekBackward()) {
            mVideoView.seekTo(mCurrentPosition);
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setSystemUiHide();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView.canPause()) {
            // 设置停止状态
            setPauseStatus();
            mVideoView.pause();
            mCurrentPosition = mVideoView.getCurrentPosition();
        }
        // 停止更新播放进度时间
        if (uiHandler.hasMessages(UPDATE_TIME)) {
            uiHandler.removeMessages(UPDATE_TIME);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mVideoView.canPause()) {
            setPauseStatus();
            mVideoView.pause();
        }
        if (uiHandler.hasMessages(UPDATE_TIME)) {
            uiHandler.removeMessages(UPDATE_TIME);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView.canPause()) {
            setPauseStatus();
            mVideoView.pause();
        }
        if (uiHandler.hasMessages(UPDATE_TIME)) {
            uiHandler.removeMessages(UPDATE_TIME);
        }
        unregisterReceiver();
    }

    private void setPlayStatus() {
        mImagePlayControl.setImageResource(R.drawable.pause_btn_style);
        mPlaySeekBar.setMax(mVideoView.getDuration());
        mTvTotalTime.setText(TimeUtil.updatePlayTimeFormat(mVideoView.getDuration()));
    }

    private void setPauseStatus() {
        mImagePlayControl.setImageResource(R.drawable.play_btn_style);
    }

    private void setSystemUiHide() {
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }

    private void setSystemUiVisible() {
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }


    /**
     * 设置布局大小
     */
    private void setVideoViewScale(int width, int height) {
        ViewGroup.LayoutParams params = mRlVideo.getLayoutParams();
        params.width = width;
        params.height = height;
        mRlVideo.setLayoutParams(params);
        ViewGroup.LayoutParams layoutParams = mVideoView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        mVideoView.setLayoutParams(layoutParams);
    }

    private void changeVolume(float offset) {
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int index = (int) (offset / screenHeight * maxVolume);
        int volume = Math.max(currentVolume + index, 0);
        volume = Math.min(volume, maxVolume);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        mVolumeSeekBar.setProgress(volume);
    }

    private void changeBrightness(float offset) {
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        float brightness = attributes.screenBrightness;
        float index = offset / screenHeight / 2;
        brightness = Math.max(brightness + index, WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF);
        brightness = Math.min(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL, brightness);
        attributes.screenBrightness = brightness;
        getWindow().setAttributes(attributes);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (mLLControl.getVisibility() == View.VISIBLE) {
            mLLControl.setVisibility(View.GONE);
        } else {
            mLLControl.setVisibility(View.VISIBLE);
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float offsetX = e1.getX() - e2.getX();
        float offsetY = e1.getY() - e2.getY();
        float absOffsetX = Math.abs(offsetX);
        float absOffsetY = Math.abs(offsetY);
        if ((e1.getX() < screenWidth / 2) && (e2.getX() < screenWidth / 2) && (absOffsetX < absOffsetY)) {
            changeBrightness(offsetY);
        } else if ((e1.getX() > screenWidth / 2) && (e2.getX() > screenWidth / 2) && (absOffsetX < absOffsetY)) {
            changeVolume(offsetY);
        }
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        Log.e(TAG, "onConfigurationChanged: screenWidth " + screenWidth + " screenHeight " + screenHeight + " orientation " + newConfig.orientation);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setSystemUiHide();
            setVideoViewScale(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //setVideoViewScale(screenWidth, screenHeight);
            mImageScreenSwitch.setImageResource(R.drawable.exit_full_screen);
            mLLVolumeControl.setVisibility(View.VISIBLE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setVideoViewScale(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(this, 240f));
            mImageScreenSwitch.setImageResource(R.drawable.full_screen);
            mLLVolumeControl.setVisibility(View.GONE);
            setSystemUiVisible();
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mImageScreenSwitch.setImageResource(R.drawable.exit_full_screen);
        } else {
            super.onBackPressed();
        }
    }


}
