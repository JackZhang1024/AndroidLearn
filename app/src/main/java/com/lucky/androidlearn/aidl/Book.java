package com.lucky.androidlearn.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {


    private int bookId;
    private String bookName;


    public Book(int bookId, String bookName) {
        this.bookId = bookId;
        this.bookName = bookName;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(bookId);
        out.writeString(bookName);
    }


    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };


    private Book(Parcel in) {
        bookId = in.readInt();
        bookName = in.readString();
    }

    @Override
    public String toString() {
        return String.format("bookId:%s, bookName:%s", bookId, bookName);
    }
}
