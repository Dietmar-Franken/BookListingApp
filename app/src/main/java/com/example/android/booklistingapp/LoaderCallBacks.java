package com.example.android.booklistingapp;

import android.content.Loader;

import java.util.List;

/**
 * Created by thodzic on 8/2/17.
 */

interface LoaderCallBacks<T> {
    void onLoadFinished(Loader<List<Book>> loader, List<Book> earthquakes);

    void onLoaderReset(Loader<List<Book>> loader);
}
