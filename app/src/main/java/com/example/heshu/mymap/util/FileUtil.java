package com.example.heshu.mymap.util;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by heshu on 2018/2/23.
 * 文件工具类
 */

public class FileUtil {

    /**
     * 判断sdcard是否被挂载
     */
    public static boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建一个保存视频的文件
     */
    public static File getOutPutMediaFile(){

        if(!hasSdcard()){
            return null;
        }
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"MyCameraApp");
        if(!mediaStorageDir.exists()){
            mediaStorageDir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        return mediaFile;
    }


}
