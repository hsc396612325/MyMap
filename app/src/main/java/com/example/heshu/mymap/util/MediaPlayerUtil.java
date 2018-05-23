package com.example.heshu.mymap.util;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by heshu on 2018/4/12.
 * MediaPlayer音频播放工具类
 */

public class MediaPlayerUtil {

    private static MediaPlayer mMediaPlayer;
    private static final String TAG = "MediaPlayerUtil";

    public static void initPaly(String filePath) {
        try {
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
            } else {
                mMediaPlayer.reset();
            }
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "onBindViewHolder: 错误");
            mMediaPlayer.stop();
        }

    }

    public static void mediaPlayerStart() {
        mMediaPlayer.start();
    }

    public static void mediaPlayerStop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public static String mediaPlayerDate(){
        java.text.SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        return   format.format(mMediaPlayer.getDuration());
    }

    public static void midiaPlayerSetListenrt(MediaPlayer.OnCompletionListener listener){
        mMediaPlayer.setOnCompletionListener(listener);
    }
}
