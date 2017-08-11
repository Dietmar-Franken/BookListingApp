package com.example.android.booklistingapp;

import java.util.List;

import android.content.Context;
import android.content.AsyncTaskLoader;

/**
 * Created by thodzic on 8/2/17.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    //Query Url
    private String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Book> books = BookQuery.fetchBookData(mUrl);
        return books;
    }
}
