package com.lucky.androidlearn.core.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;

/**
 * 这个是一个图片工具类,利用该类我们可以放大或者缩小图片
 * @author Jack.Zhang
 */
public class BitmapUtils {
    /**
     * 从指定字节数组按 指定宽高等比例加载位图
     * @param data
     * @param width
     * @param height
     * @return
     */
    public static Bitmap loadBitmap(byte[] data, int width, int height) {
        // 创建Options
        Options opts = new Options();
        opts.inJustDecodeBounds = true;
        // 加载边界信息
        BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        // 计算收缩比例
        int xScale = opts.outWidth / width;
        int yScale = opts.outHeight / height;
        // 设置收缩比例
        opts.inSampleSize = xScale > yScale ? xScale : yScale;
        opts.inJustDecodeBounds = false;
        // 返回 加载位图
        return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
    }

    /**
     * 从指定路径加载图片
     *
     * @param path
     * @return
     */
    public static Bitmap loadBitmap(String path) {
        return BitmapFactory.decodeFile(path);
    }

    /**
     * 将位图对象 保存到指定文件
     *
     * @param bm
     * @param file
     */
    public static void saveBitMapToFile(Bitmap bm, File file) throws IOException {
        // 如果文件所在目录不存在，则创建
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        // 如果文件不存在,则创建
        if (!file.exists()) {
            file.createNewFile();
        }
        // 创建文件输出流
        FileOutputStream out = new FileOutputStream(file);
        // 保存图片到指定输出流
        bm.compress(CompressFormat.JPEG, 100, out);
        out.close();
    }

    /**
     * 放大或者缩小图片
     *
     * @param bitmap 要缩放的图片
     * @param scale  缩放比例 scale>1 则表示放大图片，小于1则表示缩小图片
     * @return
     */
    public static Bitmap getShrinkedBitmap(Bitmap bitmap, int scale) {
        float scalewidth = 1;
        float scaleheight = 1;
        int bmpwidth = bitmap.getWidth();
        int bmpheight = bitmap.getHeight();
        //计算出此次要缩小的比例
        scalewidth = (float) (scalewidth * scale);
        scaleheight = (float) (scaleheight * scale);
        //生成缩放之后的Bitmap对象
        Matrix matrix = new Matrix();
        matrix.postScale(scalewidth, scaleheight);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bmpwidth, bmpheight, matrix, true);
        return bitmap;
    }


}
