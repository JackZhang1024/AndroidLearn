package com.lucky.androidlearn.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by zfz on 2017/12/31.
 */

public class BooksProvider extends ContentProvider {
    private DBHelper dbHelper;
    private static final String AUTHORITIES = "com.lucky.androidlearn.provider";
    private static final int BOOK_DIR = 0;  // 处理整个book表
    private static final int BOOK_ITEM = 1; // 处理book表中的某条数据
    private static final int CATEGORY = 2;  // 处理整个category表
    private static final int CATEGORY_ITEM = 3; // 处理category表中的某条数据

    private static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITIES, "book", BOOK_DIR);
        uriMatcher.addURI(AUTHORITIES, "book/#", BOOK_ITEM);
        uriMatcher.addURI(AUTHORITIES, "category", CATEGORY);
        uriMatcher.addURI(AUTHORITIES, "category/#", CATEGORY_ITEM);
    }


    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                cursor = db.query("book", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ITEM:
                String bookID = uri.getPathSegments().get(1);
                cursor = db.query("book", projection, "id=?", new String[]{bookID}, null, null, sortOrder);
                break;
            case CATEGORY:
                cursor = db.query("category", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORY_ITEM:
                String categoryID = uri.getPathSegments().get(1);
                cursor = db.query("category", projection, "id=?", new String[]{categoryID}, null, null, sortOrder);
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookID = db.insert("book", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITIES + "/book/" + newBookID);
                break;
            case CATEGORY:
            case CATEGORY_ITEM:
                long newCategoryID = db.insert("book", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITIES + "/category/" + newCategoryID);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uriReturn;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int deletedRows = -1;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                deletedRows = db.delete("book", selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookID = uri.getPathSegments().get(1);
                deletedRows = db.delete("book", "id=?", new String[]{bookID});
                break;
            case CATEGORY:
                deletedRows = db.delete("category", selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryID = uri.getPathSegments().get(1);
                deletedRows = db.delete("category", "id=?", new String[]{categoryID});
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int updatedRows = -1;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                updatedRows = db.update("book", values, selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookID = uri.getPathSegments().get(1);
                updatedRows = db.update("book", values, "id=?", new String[]{bookID});
                break;
            case CATEGORY:
                updatedRows = db.update("category", values, selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryID = uri.getPathSegments().get(1);
                updatedRows = db.update("category", values, "id=?", new String[]{categoryID});
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updatedRows;
    }

    /**
     * 1. 必须以vnd 开头。
     * 2. 如果内容URI 以路径结尾，则后接android.cursor.dir/，如果内容URI 以id 结尾，
     * 则后接android.cursor.item/。
     * 3. 最后接上vnd.<authority>.<path>。
     * 所以，对于content://com.example.app.provider/table1 这个内容URI，它所对应的MIME
     * 类型就可以写成：
     * vnd.android.cursor.dir/vnd.com.example.app.provider.table1
     * 对于content://com.example.app.provider/table1/1 这个内容URI，它所对应的MIME 类型
     * 就可以写成：
     * vnd.android.cursor.item/vnd. com.example.app.provider.table1
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String type = "";
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                type = "vnd.android.cursor.dir/vnd.com.lucky.androidlearn.provider.book";
                break;
            case BOOK_ITEM:
                type = "vnd.android.cursor.item/vnd.com.lucky.androidlearn.provider.book";
                break;
            case CATEGORY:
                type = "vnd.android.cursor.dir/vnd.com.lucky.androidlearn.provider.category";
                break;
            case CATEGORY_ITEM:
                type = "vnd.android.cursor.item/vnd.com.lucky.androidlearn.provider.category";
                break;
        }
        return null;
    }
}


