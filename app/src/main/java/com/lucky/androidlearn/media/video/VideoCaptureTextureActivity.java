package com.lucky.androidlearn.media.video;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.TextureView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zfz on 2018/1/27.
 */

public class VideoCaptureTextureActivity extends AppCompatActivity {
    private static final String TAG = "VideoCaptureTexture";
    private static final int MAX_DURATION = 50 * 1000;
    @BindView(R.id.texttureview)
    TextureView mTextureView;
    SurfaceTexture mSurfaceTexture;
    Camera mCamera;
    MediaRecorder mMediaRecorder;
    private HandlerThread mHandlerThread;
    private RecordHandler mRecordHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videocapture_texture);
        ButterKnife.bind(this);
        mHandlerThread = new HandlerThread("mHandlerThread");
        mHandlerThread.start();
        mRecordHandler = new RecordHandler(mHandlerThread.getLooper());
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener(){

            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                mSurfaceTexture = surface;
                mRecordHandler.sendEmptyMessage(RecordHandler.MSG_OPEN_CAMERA);
                mRecordHandler.sendEmptyMessage(RecordHandler.MSG_OPEN_RECORD);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                mRecordHandler.sendEmptyMessage(RecordHandler.MSG_STOP_CAMERA);
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
    }

    class RecordHandler extends Handler implements MediaRecorder.OnErrorListener,
            MediaRecorder.OnInfoListener {
        public static final int MSG_OPEN_CAMERA = 1;
        public static final int MSG_OPEN_RECORD = 2;
        public static final int MSG_STOP_RECORD = 3;
        public static final int MSG_STOP_CAMERA = 4;
        public static final int MAX_DURATION = 10 * 1000;
        Camera mCamera;
        MediaRecorder mMediaRecorder;
        boolean isRecordStart = false;

        public RecordHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_OPEN_CAMERA:
                    openCamera();
                    break;
                case MSG_OPEN_RECORD:
                    startRecord();
                    break;
                case MSG_STOP_RECORD:
                    stopRecorder();
                    break;
                case MSG_STOP_CAMERA:
                    releaseRecorder();
                    releaseCamera();
                    break;
            }
        }

        private void releaseRecorder() {
            if (mMediaRecorder != null) {
                mMediaRecorder.reset();
                mMediaRecorder.release();
                mMediaRecorder = null;
                isRecordStart = false;
            }
        }

        private void releaseCamera() {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                mCamera.release();
                mCamera = null;
            }
        }

        // 在子线程中操作，因为会阻塞主线程
        private void openCamera() {
            mCamera = Camera.open();
            try {
                mCamera.setPreviewTexture(mSurfaceTexture);
            } catch (Exception e) {
                e.printStackTrace();
            }
            initCamera(mCamera);
        }

        private void initCamera(Camera camera) {
            if (camera == null) {
                return;
            }
            Camera.Parameters parameters = camera.getParameters();
            //parameters.getSupportedPreviewSizes();
            //parameters.getSupportedPictureSizes();
            parameters.setPreviewSize(1920, 1080);
            parameters.setPictureSize(1920, 1080);
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                // 设置对焦模式
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mCamera.setParameters(parameters);
            mCamera.setDisplayOrientation(90); // 设置摄像方向
        }

        private void startRecord() {
            if (isRecordStart) {
                return;
            }
            if (mCamera == null) {
                return;
            }
            try {
                mCamera.unlock();
            } catch (Exception e) {
                Log.e(TAG, "startRecorder: " + e.getMessage());
                return;
            }
            if (mMediaRecorder == null) {
                mMediaRecorder = new MediaRecorder();
                isRecordStart = false;
            }
            mMediaRecorder.reset();
            mMediaRecorder.setCamera(mCamera);
            CamcorderProfile camRecordProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mMediaRecorder.setProfile(camRecordProfile);
            String filePath = getFilePath();
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.setMaxDuration(MAX_DURATION);
            mMediaRecorder.setOnInfoListener(this);
            mMediaRecorder.setOnErrorListener(this);
            try {
                mMediaRecorder.prepare();
            } catch (Exception e) {
                Log.e(TAG, "startRecorder: " + e.getMessage());
            }
            mMediaRecorder.start();
            isRecordStart = true;
            Log.e(TAG, "startRecord: 录制开始...");
        }

        private void stopRecorder() {
            try {
                mMediaRecorder.setOnErrorListener(null);
                mMediaRecorder.setOnInfoListener(null);
                mMediaRecorder.setPreviewDisplay(null);
                mMediaRecorder.reset();
                isRecordStart = false;
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        private String getFilePath() {
            String format = "yyyy-MM-dd_HH-mm-ss-SSS";
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            String name = sdf.format(new Date());
            String folder = Environment.getExternalStorageDirectory().getPath()+"/android_learn/";
            File file = new File(folder);
            if (!file.exists()) {
                file.mkdirs();
            }
            return folder + name + ".mp4";
        }

        @Override
        public void onError(MediaRecorder mr, int what, int extra) {

        }

        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            switch (what){
                case MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED:
                    Log.e(TAG, "onInfo: 拍摄时间到....");
                    stopRecorder();
                    break;
            }

        }

        private void recordVideoOnly() {
            if (mMediaRecorder == null) {
                mMediaRecorder = new MediaRecorder();
            }
            mMediaRecorder.reset();
            mMediaRecorder.setCamera(mCamera);
            CamcorderProfile camRecordProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mMediaRecorder.setOutputFormat(camRecordProfile.fileFormat);
            mMediaRecorder.setVideoEncodingBitRate(camRecordProfile.videoBitRate);
            mMediaRecorder.setVideoFrameRate(camRecordProfile.videoFrameRate);
            mMediaRecorder.setVideoSize(1920, 1080);
            mMediaRecorder.setVideoEncoder(camRecordProfile.videoCodec);
            String filePath = getFilePath();
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.setMaxDuration(MAX_DURATION);
            mMediaRecorder.setOnInfoListener(this);
            mMediaRecorder.setOnErrorListener(this);
            try {
                mMediaRecorder.prepare();
                mMediaRecorder.start();
            } catch (Exception e) {
                Log.e(TAG, "startRecorder: " + e.getMessage());
            }
        }

        private void recordAudioOnly() {
            mMediaRecorder.reset();
            CamcorderProfile camRecordProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            mMediaRecorder.setOutputFormat(camRecordProfile.fileFormat);
            mMediaRecorder.setAudioChannels(camRecordProfile.audioChannels);
            mMediaRecorder.setAudioSamplingRate(camRecordProfile.audioSampleRate);
            mMediaRecorder.setAudioEncodingBitRate(camRecordProfile.audioBitRate);
            mMediaRecorder.setAudioEncoder(camRecordProfile.audioCodec);
            String filePath = getFilePath();
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.setMaxDuration(MAX_DURATION);
            mMediaRecorder.setOnInfoListener(this);
            mMediaRecorder.setOnErrorListener(this);
            try {
                mMediaRecorder.prepare();
                mMediaRecorder.start();
            } catch (Exception e) {
                Log.e(TAG, "startRecorder: " + e.getMessage());
            }
        }
    }


}
