package com.lucky.androidlearn.provider;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images;
import android.util.Log;
import com.lucky.androidlearn.provider.model.MyAudio;


public class ReadDataFromContentProvider {

    public static void readImage(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Uri imgUri = Images.Media.EXTERNAL_CONTENT_URI;
        String[] columns = new String[]{
                Images.Media.TITLE,
                Images.Media.DATA,
                Images.Media.SIZE,
                Images.Media.MIME_TYPE    // 类型
        };
        Cursor cursor = resolver.query(imgUri, columns, null, null, null);
        if (null != cursor && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Log.e("imgs", cursor.getString(0) + "\t" + cursor.getString(3));
            }
        }
        if (null != cursor) {
            cursor.close();
        }
    }

    public static List<MyAudio> readAudio(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Uri audioUri = Audio.Media.EXTERNAL_CONTENT_URI;
        String[] columns = {
                Audio.Media.TITLE,    // 歌名
                Audio.Media.DATA,    // 路径
                Audio.Media.ARTIST,    // 演唱者
                Audio.Media.ALBUM,    // 专辑名
                Audio.Media.DURATION,    // 音频文件长度，毫秒
                Audio.Media.ALBUM_KEY    // 用来读取专辑图片时使用

        };
        List<MyAudio> data = null;
        Cursor cursor = resolver.query(
                audioUri,
                columns,
                //null, null,
                Audio.Media.DATA + " like ? or " + Audio.Media.DATA + " like ?", new String[]{"%.mp3", "%.wma"},
                null);
        if (null != cursor && cursor.getCount() > 0) {
            data = new ArrayList<MyAudio>();
            while (cursor.moveToNext()) {
                // 读到一个音频
                /////////////////// 读取该音频的专辑图片信息
                Bitmap bmp = null;
                String albumKey = cursor.getString(5);
                Uri albumUri = Audio.Albums.EXTERNAL_CONTENT_URI;
                Cursor albumCursor = resolver.query(albumUri, new String[]{Audio.Albums.ALBUM_ART}, Audio.Albums.ALBUM_KEY + "=?", new String[]{albumKey}, null);
                if (albumCursor != null && albumCursor.getCount() > 0) {
                    albumCursor.moveToNext();
                    String albumPath = albumCursor.getString(0);
                    bmp = BitmapFactory.decodeFile(albumPath);
                    albumCursor.close();
                }
                //////////////////
                data.add(new MyAudio(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getLong(4), bmp));
            }
        }
        if (null != cursor) {
            cursor.close();
        }
        return data;
    }
}










