package com.lucky.androidlearn.media.video;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2018/1/21.
 */

public class VideoMediaStoreActivity extends AppCompatActivity {
    private static final String TAG = "VideoMediaStore";
    @BindView(R.id.tv_video_meta)
    TextView mTvVideoMediaInfo;
    String[] mediaColumns = new String[]{
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.MIME_TYPE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_mediastore);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_video_info)
    public void onVideoInfoClick() {
        Cursor cursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String type = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
                String videoID = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                Log.e(TAG, "onVideoInfoClick: data " + data);
                Log.e(TAG, "onVideoInfoClick: title" + title);
                Log.e(TAG, "onVideoInfoClick: type " + type);
                Log.e(TAG, "onVideoInfoClick: videoID " + videoID);
                //queryVideoThumbNail(videoID);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @OnClick(R.id.btn_video_thumbnail)
    public void onVideoThumbnailClick(){
            queryVideoThumbNail("");
    }

    private void queryVideoThumbNail(String videoID) {
        String[] thumbnailColumns = new String[]{
                MediaStore.Video.Thumbnails.VIDEO_ID,
                MediaStore.Video.Thumbnails.DATA
        };
        //String selection = MediaStore.Video.Thumbnails.VIDEO_ID + "=" + videoID;
        //Cursor thumbNailCursor = managedQuery(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbnailColumns, selection, null, null);
        Cursor thumbNailCursor = managedQuery(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbnailColumns, null, null, null);
        while (thumbNailCursor.moveToNext()) {
            Log.e(TAG, "VideoThumbNail : Data" + thumbNailCursor.getString(thumbNailCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
            Log.e(TAG, "queryVideoThumbNail: VideoID "+thumbNailCursor.getString(thumbNailCursor.getColumnIndex(MediaStore.Video.Thumbnails.VIDEO_ID)));
            //Log.e(TAG, "queryVideoThumbNail: _ID "+thumbNailCursor.getString(thumbNailCursor.getColumnIndex(MediaStore.Video.Thumbnails._ID)));
        }
    }




}
