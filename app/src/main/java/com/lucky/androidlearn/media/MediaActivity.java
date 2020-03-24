package com.lucky.androidlearn.media;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.media.image.ImageActivity;
import com.lucky.androidlearn.media.video.SurfaceViewVideoActivity;
import com.lucky.androidlearn.media.video.VideoCaptureActivity;
import com.lucky.androidlearn.media.video.VideoCaptureAdvancedActivity;
import com.lucky.androidlearn.media.video.VideoCaptureTextureActivity;
import com.lucky.androidlearn.media.video.VideoIntentActivity;
import com.lucky.androidlearn.media.video.VideoMediaStoreActivity;
import com.lucky.androidlearn.media.video.VideoViewActivity;
import com.lucky.androidlearn.media.video.VideoViewAdvancedActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2017/12/24.
 */

public class MediaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_image)
    public void onImageClick() {
        startActivity(new Intent(this, ImageActivity.class));
    }

    @OnClick(R.id.btn_video)
    public void onVideoClick() {
        startActivity(new Intent(this, VideoIntentActivity.class));
    }

    @OnClick(R.id.btn_video_view)
    public void onVideoViewClick() {
        startActivity(new Intent(this, VideoViewActivity.class));
    }

    @OnClick(R.id.btn_video_view_advanced)
    public void onAdvancedVideoViewClick() {
        startActivity(new Intent(this, VideoViewAdvancedActivity.class));
    }

    @OnClick(R.id.btn_surface_video)
    public void onSurfaceVideoClick(){
        startActivity(new Intent(this, SurfaceViewVideoActivity.class));
    }

    @OnClick(R.id.btn_video_mediastore)
    public void onVideoMediaStoreClick(){
        startActivity(new Intent(this, VideoMediaStoreActivity.class));
    }

    @OnClick(R.id.btn_video_capture)
    public void onVideoCaptureClick(){
        startActivity(new Intent(this, VideoCaptureActivity.class));
    }

    @OnClick(R.id.btn_video_capture_advanced)
    public void onVideoCaptureAdvancedClick(){
        startActivity(new Intent(this, VideoCaptureAdvancedActivity.class));
    }

    @OnClick(R.id.btn_video_capture_texture)
    public void onVideoCaptureTextureClick(){
        startActivity(new Intent(this, VideoCaptureTextureActivity.class));
    }
}
