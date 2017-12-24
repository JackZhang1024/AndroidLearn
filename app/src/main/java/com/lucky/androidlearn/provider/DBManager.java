package com.lucky.androidlearn.provider;

import android.content.Context;
import android.util.Log;

/**
 * Created by zfz on 2017/12/31.
 */

public class DBManager {
    private static final String TAG = "DBManager";
    private static DBManager dbManager = null;
    private static DBHelper dbHelper;

    public DBManager(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
            Log.e(TAG, "DBManager: ");
        }
    }

    public static void init(Context context) {
        initInstance(context);
    }

    private static DBManager initInstance(Context context) {
        if (dbManager == null) {
            dbManager = new DBManager(context);
        }
        return dbManager;
    }

    public static DBHelper getDbHelper() {

        return dbHelper;
    }

}
