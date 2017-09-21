package com.example.android.booklistingapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thodzic on 7/28/17.
 */

public class Book implements Parcelable {

    //Author of the book
    private String mAuthor;

    //Book title
    private String mTitle;

    /**
     * Construcsts a new Book object
     *
     * @param author    is the author of the book
     * @param bookTitle is the title of the book
     */

    public Book(String author, String bookTitle) {
        mAuthor = author;
        mTitle = bookTitle;
    }

    //Returns author of the book
    public String getAuthor() {
        return mAuthor;
    }

    //Returns title of the book
    public String getTitle() {
        return mTitle;
    }

    protected Book(Parcel in) {
        mAuthor = in.readString();
        mTitle = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAuthor);
        dest.writeString(mTitle);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}