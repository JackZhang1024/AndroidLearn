package com.lucky.androidlearn.provider;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.lucky.androidlearn.provider.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by zfz on 2017/12/31.
 */

public class BooksLoader extends AsyncTaskLoader<List<Book>> {
    private static final String TAG = "BooksLoader";
    private BookContentObserver bookContentObserver;
    private List<Book> result = new ArrayList<>();
    private AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    public BooksLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        // 注册URI
        Uri uri = Uri.parse("content://com.lucky.androidlearn.provider/book");
        bookContentObserver = new BookContentObserver(new Handler());
        getContext().getContentResolver().registerContentObserver(uri, true, bookContentObserver);
        Log.e(TAG, "onStartLoading: (result==null) "+(result==null)+" takeContentChanged() "+takeContentChanged());
        if (result == null || result.isEmpty()|| takeContentChanged()) {
            forceLoad();
        } else {
            deliverResult(result);
        }
    }

    @Override
    public List<Book> loadInBackground() {
        List<Book> result = null;
        try {
            atomicBoolean.set(true);
            BookDao bookDao = new BookDao(getContext());
            result = bookDao.loadBooks();
            Log.e(TAG, "loadInBackground: "+result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            atomicBoolean.set(false);
        }
        return result;
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
    }

    @Override
    public void deliverResult(List<Book> data) {
        result = data;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        result = null;
        stopLoading();
        if (bookContentObserver != null) {
            getContext().getContentResolver().unregisterContentObserver(bookContentObserver);
            bookContentObserver = null;
        }
    }

    class BookContentObserver extends ContentObserver {
        private static final String TAG = "BookContentObserver";

        public BookContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.e(TAG, "onChange: ");
            onContentChanged(); // 当表里数据发生变化时就重新加载数据
        }
    }


}
