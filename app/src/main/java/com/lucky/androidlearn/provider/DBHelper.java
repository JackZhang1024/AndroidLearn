package com.lucky.androidlearn.provider;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by zfz on 2017/12/31.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    private static final String DB_NAME = "books.db";
    private static final int DB_VERSION = 1;
    private String createBookSql = "create table book(" +
            "id INTEGER primary key AUTOINCREMENT, " +
            "name VARCHAR(20),  " +
            "author VARCHAR(20), " +
            "page_size INTEGER, " +
            "press VARCHAR(20), " +
            "price INTEGER)";
    private String createCategorySql = "create table category(" +
            "id INTEGER primary key AUTOINCREMENT, " +
            "name VARCHAR(20)," +
            "kind VARCHAR(20)" +
            ")";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createBookSql);
        db.execSQL(createCategorySql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void safeAlterTable(SQLiteDatabase db, String tableName, String columnName, String columnType) {
        try {
            db.execSQL(String.format("ALTER TABLE %s ADD COLUMN %s %s", tableName, columnName, columnType));
        } catch (SQLException e) {
            Log.e(TAG, "safeAlterTable: " + e.getMessage());
        }
    }

    private void deleteTable(SQLiteDatabase db, String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }
}
