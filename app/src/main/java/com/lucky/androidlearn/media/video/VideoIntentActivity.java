package com.lucky.androidlearn.media.video;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lucky.androidlearn.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2018/1/18.
 */

public class VideoIntentActivity extends AppCompatActivity {
    private static final String TAG = "VideoIntentActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_intent);
        ButterKnife.bind(this);
    }

    //http://blog.csdn.net/qq_24531461/article/details/73456794
    //http://blog.csdn.net/androidstarjack/article/details/68954614?fps=1&locationNum=9
    @OnClick(R.id.btn_play_video)
    public void onVideoPlayClick(View view) {
        String videoPath = Environment.getExternalStorageDirectory().getPath();
        Log.e(TAG, "onVideoPlayClick: "+videoPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Hongyanjiu.mp4");
        intent.setDataAndType(uri, "video/mp4");
        startActivity(intent);
    }
}
