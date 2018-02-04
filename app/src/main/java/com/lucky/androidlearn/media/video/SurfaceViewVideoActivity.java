package com.lucky.androidlearn.media.video;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import com.lucky.androidlearn.R;
import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zfz on 2018/1/20.
 */

public class SurfaceViewVideoActivity extends AppCompatActivity implements
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnVideoSizeChangedListener,
        SurfaceHolder.Callback,
        MediaController.MediaPlayerControl {
    private static final String TAG = "SurfaceViewVideo";
    @BindView(R.id.surfaceview)
    SurfaceView mSurfaceView;
    @BindView(R.id.main_view)
    LinearLayout mMainView;
    private SurfaceHolder mSurfaceHolder;
    private MediaPlayer mMediaPlayer;
    private int mScreenWidth;
    private int mScreenHeight;
    private MediaController mMediaController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surfaceview_video);
        ButterKnife.bind(this);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(this);
        mMediaController = new MediaController(this);
        initMediaPlayer();
        addListeners();
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;
    }

    private void addListeners() {
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
        mMediaPlayer.setOnVideoSizeChangedListener(this);
    }

    private void initMediaPlayer() {
        try {
            mMediaPlayer = new MediaPlayer();
            String url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
            //Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.big_buck_bunny);
            Uri uri = Uri.parse(url);
            mMediaPlayer.setDataSource(this, uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.e(TAG, "onCompletion: ");
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.e(TAG, "onError: SERVER_DIED");
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.e(TAG, "onError: MEDIA_ERROR_UNKNOWN");
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                Log.e(TAG, "onInfo: 码率太高 解码有问题");
                break;
            case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                Log.e(TAG, "onInfo: 直播不能拖拽 ");
                break;
            case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                Log.e(TAG, "onInfo: 音频和视频轨道交错有问题");
                break;
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.e(TAG, "onPrepared: 开始准备播放");
        double videoWidth = mp.getVideoWidth();
        double videoHeight = mp.getVideoHeight();
        Log.e(TAG, "onPrepared: ScreenWith " + mScreenWidth + " ScreenHeight " + mScreenHeight);
        Log.e(TAG, "onPrepared: videoWidth " + videoWidth + " videoHeight " + videoHeight);
        if (videoWidth > mScreenWidth || videoHeight > mScreenHeight) {
            float widthRatio = (float) videoWidth / mScreenWidth;
            float heightRatio = (float) videoHeight / mScreenHeight;
            Log.e(TAG, "onPrepared: ratio widthRatio " + widthRatio + " heightRatio " + heightRatio);
            if (widthRatio > 1 || heightRatio > 1) {
                if (widthRatio > heightRatio) {
                    videoWidth = Math.ceil(videoWidth / widthRatio);
                    videoHeight = Math.ceil(videoHeight / widthRatio);
                } else {
                    videoWidth = Math.ceil(videoWidth / heightRatio);
                    videoHeight = Math.ceil(videoHeight / heightRatio);
                }
            }
        }
        Log.e(TAG, "onPrepared: Final videoWidth " + videoWidth + " videoHeight " + videoHeight);
        //mSurfaceView.setLayoutParams(new LinearLayout.LayoutParams((int) videoWidth, (int) videoHeight));
        mSurfaceView.setLayoutParams(new LinearLayout.LayoutParams((int) mScreenWidth, (int) videoHeight));
        mMediaController.setMediaPlayer(this);
        mMediaController.setAnchorView(mSurfaceView);
        mMediaController.setEnabled(true);
        mMediaPlayer.start();
        mMediaController.show();
        Log.e(TAG, "onPrepared: 正式开始播放");
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        Log.e(TAG, "onSeekComplete: ");
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Log.e(TAG, "onVideoSizeChanged: width " + width + " height " + height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 指定MediaPlayer将surfaceView设置成播放表层
        mMediaPlayer.setDisplay(holder);
        try {
            mMediaPlayer.prepareAsync();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e(TAG, "surfaceChanged: width " + width + "  height " + height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG, "surfaceDestroyed: ");
    }

    @Override
    public void start() {
        mMediaPlayer.start();
    }

    @Override
    public void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    @Override
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mMediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mMediaController.isShowing()) {
            mMediaController.hide();
        } else {
            mMediaController.show(3000);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
    }
}
