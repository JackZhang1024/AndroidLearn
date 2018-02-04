package com.lucky.androidlearn.media.video;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;

/**
 * Created by zfz on 2018/1/27.
 */

public class LKMediaRecorder {

    MediaRecorder mMediaRecorder;
    MediaPlayer mediaPlayer;
    Context context;
    File mRecAudioFile;
    File mRecAudioPath;
    String strTempFile = "recaudio_";
    private static MediaRecorder m = null;

    public LKMediaRecorder(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        mediaPlayer = new MediaPlayer();
    }

    private MediaRecorder getInstance() {
        if (m == null) {
            m = new MediaRecorder();
        }
        return m;
    }

    public void startRecord() {
        try {
            if (!initRecAudioPath()) {
                return;
            }
            if (mMediaRecorder != null) {
                return;
            }
            mMediaRecorder = getInstance();
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            try {
                mRecAudioFile = File.createTempFile(strTempFile, ".amr",
                        mRecAudioPath);

            } catch (Exception e) {
                e.printStackTrace();
            }
            mMediaRecorder.setOutputFile(mRecAudioFile.getAbsolutePath());
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecord() {
        if (mRecAudioFile != null && mMediaRecorder != null) {

            try {
                mMediaRecorder.setOnErrorListener(null);
                mMediaRecorder.setPreviewDisplay(null);
                mMediaRecorder.stop();
                mMediaRecorder.release();
                mMediaRecorder = null;
            } catch (Exception e) {

            }
        }
    }

    public void playMusic() {
        /*
         * Intent intent = new Intent();
         * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         * intent.setAction(android.content.Intent.ACTION_VIEW);
         * intent.setDataAndType(Uri.fromFile(mRecAudioFile), "audio");
         * context.startActivity(intent);
         */
        if (mMediaRecorder != null) {
            return;
        }
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(mRecAudioFile.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private boolean initRecAudioPath() {
        if (sdcardIsValid()) {
            String path = Environment.getExternalStorageDirectory().toString() + File.separator + "record";
            mRecAudioPath = new File(path);
            if (!mRecAudioPath.exists()) {
                mRecAudioPath.mkdirs();
            }
        } else {
            mRecAudioPath = null;
        }
        return mRecAudioPath != null;
    }

    private boolean sdcardIsValid() {
        if (Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
        }
        return false;
    }

}
