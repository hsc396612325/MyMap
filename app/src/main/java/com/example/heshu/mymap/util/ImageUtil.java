package com.example.heshu.mymap.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by heshu on 2018/2/3.
 *图片绘制工具类
 */

public class ImageUtil {
    /**
     * 绘制文字到中间
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @return
     */
    public static Bitmap drawTextToCenter(Context context, Bitmap bitmap, String text,
                                          int size, int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                (bitmap.getWidth() - bounds.width()) / 7*3,
                (bitmap.getHeight() + bounds.height()) / 7*3);
    }

    /**
     * dip转pix
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**图片上绘制文字
     *
     * @param context
     * @param bitmap
     * @param text
     * @param paint
     * @param bounds
     * @param paddingLeft
     * @param paddingTop
     * @return
     */
    private static Bitmap drawTextToBitmap(Context context, Bitmap bitmap, String text,
                                           Paint paint, Rect bounds, int paddingLeft, int paddingTop) {
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();

        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawText(text, paddingLeft, paddingTop, paint);
        return bitmap;
    }

    /**通过Uri获得图片并压缩
     *
     * @param ac
     * @param uri
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static Bitmap getBitmapFormUri(Activity ac , Uri uri)throws FileNotFoundException,IOException{
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeStream(input,null,onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if((originalWidth == -1)||(originalHeight == -1)){
            return null;
        }

        float hh = 800f;//设置高度
        float ww = 480f;

        //缩放比
        int be = 1;//be=1 表示不缩放
        if(originalWidth > originalHeight && originalWidth >ww){
            be = (int)(originalWidth/ww);
        }else {
            be = (int)(originalHeight/hh);
        }

        if(be<=0){
            be = 1;
        }

        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;
        bitmapOptions.inDither= true;
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input,null,bitmapOptions);

        return compressImage(bitmap); //质量压缩
    }

    /**质量压缩
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100,baos);//质量压缩，100表示不压缩，结果保存在baos中
        int options = 100;
        while(baos.toByteArray().length /1024>100){  //循环判断压缩后的图片是否大于100kb，大于继续压缩
            baos.reset(); // 重置baos
            image.compress(Bitmap.CompressFormat.JPEG,options,baos);
            options -= 10;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm,null,null);
        return bitmap;
    }


}
