package com.example.android.booklistingapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    //Give our book loader an id of 1 and set as a public global variable.
    public static final int BOOK_LOADER_ID = 1;
    //Find a reference to the ListView in the layout
    ListView bookListView;
    //Set the adapter to a non public, non static global variable.
    private BookAdapter mAdapter;
    //Set the query term to a non public, non static global variable.
    private String mQuery = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        // Create a new adapter that takes an empty list of books as imput
        mAdapter = new BookAdapter(MainActivity.this, new ArrayList<Book>());

        bookListView = (ListView) findViewById(R.id.list);

        //Set the adapter on the ListView
        //so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);

        // Save the ListView state (= includes scroll position) as a Parceble
        Parcelable state = bookListView.onSaveInstanceState();

        //Initialize the loader
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);

        // Restore previous state (including selected item index and scroll position)
        bookListView.onRestoreInstanceState(state);

    }


    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        //Create a new loader for the given URL
        //Use URI builder to build the URL string because it helps minimize security risks and
        //allows us to include and encode special characters easily on your URLs.
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.googleapis.com")
                .appendPath("books")
                .appendPath("v1")
                .appendPath("volumes")
                .appendQueryParameter("q", mQuery);
        String myUrl = builder.build().toString();
        return new BookLoader(this, myUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        //Clear the adapter of previous book data
        mAdapter.clear();
        //If there is a valid list of books, then add them to the adapter's dataset.  This will
        //trigger the ListView to update.

        if (books == null) {
            TextView nothing_to_display_text = (TextView) findViewById(R.id.nothing_to_display_text);
            nothing_to_display_text.setVisibility(View.VISIBLE);
        }
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
            TextView nothing_to_display_text = (TextView) findViewById(R.id.nothing_to_display_text);
            nothing_to_display_text.setVisibility(View.GONE);

        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        //Loader reset, so we can clear out our exisiting data
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();

        // Setting the listener on the SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mQuery = query;
                //Check if there is an active internet connection
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();

                //Initiate the loader.  Pass in the int ID constant defined above and pass in null for
                //the bundle.  Pass in this activity for the LoaderCAllbacks parameter (which is valid
                //because this activity implements the LoaderCallbacks interface).
                if (netInfo != null && netInfo.isConnected()) {
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);
                    loaderManager.restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                } else {
                    TextView noInternetView = (TextView) findViewById(R.id.empty_view);
                    noInternetView.setText("No internet");

                    TextView nothingNewToDisplay = (TextView) findViewById(R.id.nothing_to_display_text);
                    nothingNewToDisplay.setVisibility(View.GONE);
                }

                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }
}
