package com.example.heshu.mymap.util;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by heshu on 2018/4/9.
 * 录音工具类
 */

public class AudioRecoderUtil {
    //文件路径
    private String filePath;

    //文件夹路径
    private String FolderPath;

    private MediaRecorder mMediaRecorder;
    public static final int MAX_LENGTH = 1000 * 60 * 12;

    private OnAudioStatusUpdateListener audioStatusUpdateListener;
    private static final String TAG = "AudioRecoderUtil";

    /**
     * 文件存储默认sdcard/record
     */
    public AudioRecoderUtil() {
        //默认保存路径/dcard/record
        this(Environment.getExternalStorageDirectory() + "/record/");
    }

    public AudioRecoderUtil(String filePath) {
        File path = new File(filePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        this.FolderPath = filePath;
    }

    private long startTime;
    private long endTime;

    /**
     * 开始录音
     */
    public void startRecord() {
        // Initial: 实例化MediaRecorder
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }

        try {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT); //设置音频文件编码
             /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            filePath = FolderPath + timeStamp + ".amr";

            //准备
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.setMaxDuration(MAX_LENGTH);
            mMediaRecorder.prepare();
            //开始
            mMediaRecorder.start();
//            updateMicStatus();
            //获取开始时间
            startTime = System.currentTimeMillis();
            updateMicStatus();
            Log.e(TAG, "startRecord: " + startTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止录音
     *
     * @return
     */
    public long stopRecord() {
        if (mMediaRecorder == null) {
            return 0L;
        }

        endTime = System.currentTimeMillis();
        try {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;

            audioStatusUpdateListener.onStop(filePath);
            Log.d(TAG, "stopRecord: " + filePath);
        } catch (RuntimeException e) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;

            File file = new File(filePath);
            Log.d(TAG, "stopRecord: " + filePath);
            if (file.exists()) {
                file.delete();
            }
            filePath = "";
        }
        Log.d(TAG, "stopRecord: " + (endTime - startTime));

        return endTime - startTime;
    }

    /**
     * 取消录音
     */
    public void cencelRecord() {
        try {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        } catch (RuntimeException e) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }

        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        filePath = "";
    }

    private final Handler mHandler = new Handler();
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        @Override
        public void run() {
            updateMicStatus();
            Log.d(TAG, "run: ");
        }
    };

    private int BASE = 1;
    private int SPACE = 100; //时间间隔取样

    public void setOnAudioStatusUpdateListener(OnAudioStatusUpdateListener audioStatusUpdateListener) {
        this.audioStatusUpdateListener = audioStatusUpdateListener;
    }

    /**
     * 更新UI状态
     */
    private void updateMicStatus() {
        if (mMediaRecorder != null) {
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / BASE;
            double db = 0;// 分贝

            if (null != audioStatusUpdateListener) {
                audioStatusUpdateListener.onUpdate(db, System.currentTimeMillis() - startTime);
            }
            Log.d(TAG, "updateMicStatus: ");
            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
        }
    }

    public interface OnAudioStatusUpdateListener {
        /**
         * 录音中...
         *
         * @param db   当前声音分贝
         * @param time 录音时长
         */
        public void onUpdate(double db, long time);

        /**
         * 停止录音
         *
         * @param filePath 保存路径
         */
        public void onStop(String filePath);
    }

}
