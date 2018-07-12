package com.imgod.kk.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * MediaPlayUtils.java 播放音频文件的工具类
 *
 * @author imgod1
 * @version 2.0.0 2018/5/26 14:59
 * @update imgod1 2018/5/26 14:59
 * @updateDes
 * @include {@link }
 * @used {@link }
 */
public class MediaPlayUtils {
    private static MediaPlayer myMediaPlayer;

    public static void playSound(Context context, String assetsFileName) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前音乐音量
        int maxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 获取最大声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0); // 设置为最大声音，可通过SeekBar更改音量大小

        AssetFileDescriptor fileDescriptor;
        try {
            if (null == myMediaPlayer) {
                fileDescriptor = context.getAssets().openFd(assetsFileName);
                myMediaPlayer = new MediaPlayer();
                myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                myMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),

                        fileDescriptor.getStartOffset(),

                        fileDescriptor.getLength());

                myMediaPlayer.prepare();
                myMediaPlayer.setLooping(true);
            }
            myMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopPlay() {
        if (null != myMediaPlayer) {
            myMediaPlayer.stop();
        }
    }
}
