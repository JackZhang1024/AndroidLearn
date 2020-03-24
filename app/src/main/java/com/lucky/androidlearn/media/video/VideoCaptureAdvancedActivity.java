package com.lucky.androidlearn.media.video;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2018/1/27.
 */

public class VideoCaptureAdvancedActivity extends AppCompatActivity
        implements SurfaceHolder.Callback,
        MediaRecorder.OnErrorListener,
        MediaRecorder.OnInfoListener {
    private static final String TAG = "VideoCaptureAdvanced";
    @BindView(R.id.video_surfaceview)
    SurfaceView mSurfaceView;

    MediaRecorder mMediaRecorder;
    SurfaceHolder mSurfaceHolder;
    boolean recording = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initRecorder();
        setContentView(R.layout.activity_videocapture_advanced);
        ButterKnife.bind(this);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceView.setKeepScreenOn(true);
    }

    private void initRecorder() {
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setOnInfoListener(this);
            mMediaRecorder.setOnErrorListener(this);
        } else {
            mMediaRecorder.reset();
        }
        Camera camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(640, 480);
        parameters.setPictureSize(640, 480);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);
        camera.unlock();
        mMediaRecorder.setCamera(camera);
        //mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        mMediaRecorder.setProfile(camcorderProfile);
        String file = Environment.getExternalStorageDirectory().getPath() + "/sample_video.mp4";
        //mMediaRecorder.setOutputFile(new File(file));
        mMediaRecorder.setOutputFile(file);
        mMediaRecorder.setMaxDuration(10*1000);
        mMediaRecorder.setMaxFileSize(20*1024 * 1024); // 设置大小为20M
    }

    private void prepareRecorder() {
        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        try {
            mMediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    @OnClick(R.id.video_surfaceview)
    public void onSurfaceViewClick() {
        if (recording) {
            stopRecord();
        } else {
            mMediaRecorder.start();
            recording = true;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        prepareRecorder();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (recording) {
            mMediaRecorder.stop();
            recording = false;
        }
        mMediaRecorder.release();
        finish();
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        switch (what) {
            case MediaRecorder.MEDIA_RECORDER_ERROR_UNKNOWN:
                break;
            case MediaRecorder.MEDIA_ERROR_SERVER_DIED:
                break;
        }
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
        }
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        switch (what) {
            case MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED:
                Log.e(TAG, "onInfo: 拍摄时间到了");
                stopRecord();
                break;
            case MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_APPROACHING:
                Log.e(TAG, "onInfo: 马上到了拍摄文件存储最大设置");
                break;
            case MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED:
                Log.e(TAG, "onInfo: 已经到了拍摄文件最大设置");
                stopRecord();
                break;
            case MediaRecorder.MEDIA_RECORDER_INFO_UNKNOWN:
                Log.e(TAG, "onInfo: 其他位置错误");
                break;
        }
    }

    private void stopRecord() {
        try {
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setOnInfoListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            mMediaRecorder.stop();
            mMediaRecorder.release();
            recording = false;
            //initRecorder();
            //prepareRecorder();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
