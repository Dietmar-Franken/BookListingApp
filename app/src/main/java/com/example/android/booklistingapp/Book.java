package com.example.android.booklistingapp;

/**
 * Created by thodzic on 7/28/17.
 */

public class Book {

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
}
