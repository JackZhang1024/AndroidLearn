package com.lucky.androidlearn.core.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import com.jingewenku.abrahamcaijin.commonutil.disklrucache.DiskLruCache;
import com.jingewenku.abrahamcaijin.commonutil.disklrucache.DiskLruCacheUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DiskLruCacheManager {

    private static DiskLruCacheManager manager;
    private DiskLruCache mDiskLruCache = null;
    private static final int CACHE_MAX_SIZE = 10 * 1024 * 1024;
    private static final int VALUE_COUNT = 1;
    private static final int BUFFER = 1024;

    private DiskLruCacheManager() {

    }

    public static DiskLruCacheManager getInstance() {
        if (manager == null) {
            synchronized (DiskLruCacheManager.class) {
                if (manager == null) {
                    manager = new DiskLruCacheManager();
                }
            }
        }
        return manager;
    }

    /**
     * 获取版本号
     */
    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 获取缓存目录
     */
    public File getDiskCacheDir(Context context, @NonNull String uniqueName) {
        String cachePath = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            File f = context.getExternalCacheDir();
            if (f != null) {
                cachePath = f.getPath();
            }
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        Log.e("DiskLruCacheManager", "getDiskCacheDir: " + cachePath);
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 一个缓存目录只有一个文件
     */
    public DiskLruCacheManager open(Context context, String dir) {
        open(context, dir, VALUE_COUNT);
        return manager;
    }

    /**
     * dir:缓存的目录/路径
     * valueCount:缓存文件的个数
     */
    private DiskLruCacheManager open(Context context, String dir, int valueCount) {
        try {
            mDiskLruCache = DiskLruCache.open(getDiskCacheDir(context, dir), getAppVersion(context), valueCount, CACHE_MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return manager;
    }

    /**
     * md5生成缓存key
     */
    public String hashKeyForDisk(@NonNull String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 保存字符串
     * key是通过LinkedHashMap获取缓存中的Entry对象
     * index是获取目录中的文件索引
     * value是需要保存的信息值
     */
    private void saveString(String key, int index, @NonNull String value) {
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(hashKeyForDisk(key));
            editor.set(index, value);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveString(String key, @NonNull String value) {
        saveString(key, 0, value);
    }

    /**
     * 保存流信息
     * key是通过LinkedHashMap获取缓存中的Entry对象
     * index是获取目录中的文件索引
     * value是需要保存的信息流
     */
    private void saveStream(String key, int index, @NonNull InputStream value) {
        int len = 0;
        byte[] bytes = new byte[BUFFER];
        OutputStream os = null;
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(hashKeyForDisk(key));
            os = editor.newOutputStream(index);
            while ((len = value.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            editor.commit();
            mDiskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //快速关流
            DiskLruCacheUtil.closeQuietly(os);
        }
    }

    public void saveStream(@NonNull String key, @NonNull InputStream value) {
        saveStream(key, 0, value);
    }

    /**
     * 获取缓存的String字符串
     * key是通过LinkedHashMap获取缓存中的Entry对象
     * index是获取目录中的文件索引
     */
    private String getString(@NonNull String key, int index) {
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(hashKeyForDisk(key));
            if (snapshot != null) {
                return snapshot.getString(index);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getString(@NonNull String key) {
        return getString(key, 0);
    }

    /**
     * 获取缓存的String字符串
     * key是通过LinkedHashMap获取缓存中的Entry对象
     * index是获取目录中的文件索引
     */
    public InputStream getInputStream(@NonNull String key, int index) {
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(hashKeyForDisk(key));
            if (snapshot == null) return null;
            return snapshot.getInputStream(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream getInputStream(@NonNull String key) {
        return getInputStream(key, 0);
    }

    /**
     * 删除对应的key缓存
     */
    public boolean remove(@NonNull String key) {
        try {
            return mDiskLruCache.remove(hashKeyForDisk(key));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 清除所有信息
     */
    public void clear() {
        try {
            mDiskLruCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
