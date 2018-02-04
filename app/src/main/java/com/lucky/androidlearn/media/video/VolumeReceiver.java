package com.lucky.androidlearn.media.video;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.lucky.androidlearn.R;

/**
 * Created by zfz on 2018/1/20.
 */

public class VolumeReceiver extends BroadcastReceiver {
    private ImageView mImageVolume;
    private SeekBar mSeekBarVolume;
    private AudioManager mAudioManger;

    public VolumeReceiver(Context context, ImageView volumeImageView, SeekBar volumeSeekBar) {
        mImageVolume = volumeImageView;
        mSeekBarVolume = volumeSeekBar;
        mAudioManger = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
            int volume = mAudioManger.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (volume == 0) {
                mImageVolume.setImageResource(R.drawable.mute);
            } else {
                mImageVolume.setImageResource(R.drawable.volume);
            }
            mSeekBarVolume.setProgress(volume);
        }
    }
}
