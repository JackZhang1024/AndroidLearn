package com.lucky.androidlearn.media.video;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.VideoView;

import com.lucky.androidlearn.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2018/1/27.
 */

public class VideoCaptureActivity extends AppCompatActivity {
    private static final int VIDEO_CAPTURE_REQUEST_CODE = 100;
    private Uri mVideoUri;
    @BindView(R.id.video_view)
    VideoView mVideoView;
    @BindView(R.id.btn_video_capture)
    Button mBtnVideoCapture;
    @BindView(R.id.btn_play_video)
    Button mBtnVideoPlay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videocapture);
        ButterKnife.bind(this);
        mBtnVideoPlay.setEnabled(false);
    }

    @OnClick(R.id.btn_video_capture)
    public void onVideoCaptureClick(){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, VIDEO_CAPTURE_REQUEST_CODE);
    }

    @OnClick(R.id.btn_play_video)
    public void onPlayVideoClick(){
        mVideoView.setVideoURI(mVideoUri);
        mVideoView.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_CAPTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            mVideoUri = data.getData();
            mBtnVideoPlay.setEnabled(true);
        }
    }
}
