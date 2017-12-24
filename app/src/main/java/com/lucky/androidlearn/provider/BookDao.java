package com.lucky.androidlearn.provider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.lucky.androidlearn.provider.model.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zfz on 2017/12/31.
 */

public class BookDao {
    private Context context;

    public BookDao(Context context) {
        this.context = context;
    }

    public List<Book> loadBooks() {
        List<Book> bookList = new ArrayList<>();
        Uri uri = Uri.parse("content://com.lucky.androidlearn.provider/book");
        Cursor cursor = context.getContentResolver().query(uri, new String[]{"name", "press", "page_size", "price"}, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            String name = cursor.getString(0);
            String press = cursor.getString(1);
            int pageSize = cursor.getInt(2);
            int price = cursor.getInt(3);
            Book book = new Book();
            book.setName(name);
            book.setPress(press);
            book.setPage_size(pageSize);
            book.setPrice(price);
            bookList.add(book);
        }
        if (cursor != null) {
            cursor.close();
        }
        return bookList;
    }

}
