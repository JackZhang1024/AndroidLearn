package com.lucky.androidlearn.core.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * 此类是一个拷贝SQLite数据库文件到 /data/data/项目报名/数据库名目录下的工具类
 * 使用时注意工具类的项目包名应该与配置清单文件中的一致 否则会报错
 *
 * @author Jack.Zhang
 */
public class DBManager {
    // 首先获取数据库的输入流对象
    // 然后将数据拷贝到指定的位置
    // 最后在Activity中获取对应的数据库 然后做出查询即可

    private SQLiteDatabase database;
    private Context context;
    public static final String DB_NAME = "cn_b.db";
    private static final String PACKAGE_NAME = "com.lucky.androidlearn";
    public static final String DB_PATH = "/data/"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME;

    public DBManager(Context context) {
        this.context = context;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    public void createDataBase() {
        System.out.println("SqliteDatabase.path" + DB_PATH + "/" + DB_NAME);
        this.database = getMyDataBase(DB_PATH + "/" + DB_NAME, 0);
    }

    public SQLiteDatabase getMyDataBase(String dbfile, int rawID) {
        // 获取数据库输入流对像
        SQLiteDatabase db = null;
        File db_file = new File(dbfile);
        InputStream in = null;
        FileOutputStream fos = null;
        byte[] buff = new byte[1024];
/*		if(db_file.exists()){
            db_file.delete();
		}*/
        // 如果数据库文件不存在 那么创建数据库
        if (!db_file.exists()) {
            try {
                fos = new FileOutputStream(db_file);
                //放在res目录下的raw文件夹中数据库文件
                in = context.getResources().openRawResource(rawID);
                int length = 0;
                while ((length = in.read(buff)) != -1) {
                    fos.write(buff, 0, length);
                }
                in.close();
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        db = SQLiteDatabase.openOrCreateDatabase(db_file, null);
        return db;
    }

    public void closeDataBase() {
        this.database.close();
    }
}
