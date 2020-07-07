package com.lucky.androidlearn.provider;

import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.provider.model.Book;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2017/12/31.
 */

public class MainProviderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {
    private static final String TAG = "MainProviderActivity";
    private MainBookContentObserver bookContentObserver = new MainBookContentObserver(new Handler());
    private static final int BOOKS_LOADER = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        ButterKnife.bind(this);
        registerContentObserver();
        getSupportLoaderManager().initLoader(BOOKS_LOADER, null, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterContentObserver();
    }

    private void registerContentObserver() {
        Uri uri = Uri.parse("content://com.lucky.androidlearn.provider/book");
        getContentResolver().registerContentObserver(uri, true, bookContentObserver);
    }

    private void unregisterContentObserver() {
        if (bookContentObserver != null) {
            getContentResolver().unregisterContentObserver(bookContentObserver);
        }
    }

    @OnClick(R.id.btn_insert)
    public void onInsertClick(View view) {
        Uri uri = Uri.parse("content://com.lucky.androidlearn.provider/book");
        ContentValues values = new ContentValues();
        values.put("name", "Android开发艺术");
        values.put("author", "任玉刚");
        values.put("page_size", "500");
        values.put("press", "机械工业出版社");
        values.put("price", "67.8");
        Uri uriReturn = getContentResolver().insert(uri, values);
        Log.e(TAG, "onInsertClick: " + uriReturn);
    }


    @OnClick(R.id.btn_delete)
    public void onDeleteClick(View view) {
        Uri uri = Uri.parse("content://com.lucky.androidlearn.provider/book");
        // name Android开发艺术
        String where = "name=?";
        String[] selectionArgs = new String[]{"Android开发艺术"};
        int deletedRows = getContentResolver().delete(uri, where, selectionArgs);
        Log.e(TAG, "onDeleteClick: " + deletedRows);
    }

    @OnClick(R.id.btn_update)
    public void onUpdateClick(View view) {
        Uri uri = Uri.parse("content://com.lucky.androidlearn.provider/book");
        ContentValues values = new ContentValues();
        values.put("name", "Android开发艺术");
        values.put("author", "任玉刚");
        values.put("page_size", "478");
        values.put("press", "机械工业出版社");
        values.put("price", "78.9");
        int updatedID = getContentResolver().update(uri, values, null, null);
        Log.e(TAG, "onUpdateClick: " + updatedID);
    }

    @OnClick(R.id.btn_query)
    public void onQueryClick(View view) {
        Uri uri = Uri.parse("content://com.lucky.androidlearn.provider/book");
        Cursor cursor = getContentResolver().query(uri, new String[]{"name", "press", "page_size", "price"}, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            String name = cursor.getString(0);
            String press = cursor.getString(1);
            String pageSize = cursor.getString(2);
            String price = cursor.getString(3);
            Log.e(TAG, "onQueryClick: name " + name + " press " + press + " pageSize " + pageSize + " price " + price);
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    @OnClick(R.id.btn_simple_provider)
    public void onSimpleProviderClick(View view) {
        startActivity(new Intent(this, SimpleProviderActivity.class));
    }

    class MainBookContentObserver extends ContentObserver {

        public MainBookContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.e(TAG, "onChange: ");
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        Log.e(TAG, "onCreateLoader: id " + id);
        return new BooksLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        Log.e(TAG, "onLoadFinished: data.isEmpty " + data.isEmpty());
        for (Book book : data) {
            Log.e(TAG, "onLoadFinished: " + book.getName() + " press " + book.getPress());
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) { // 删除和loader数据相关的引用

    }
}
