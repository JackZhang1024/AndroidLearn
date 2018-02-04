package com.lucky.androidlearn.media.video;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;
import com.lucky.androidlearn.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zfz on 2018/1/18.
 */

public class VideoViewActivity extends AppCompatActivity {

    @BindView(R.id.video_view)
    VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoview);
        ButterKnife.bind(this);
        String videoPath = Environment.getExternalStorageDirectory().getPath()+"/big_buck_bunny.mp4";
        Uri uri = Uri.parse(videoPath);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(uri);
        videoView.start();
    }
}
