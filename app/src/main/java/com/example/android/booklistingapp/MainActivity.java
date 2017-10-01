package com.example.android.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.app.LoaderManager.LoaderCallbacks;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    String userInput = "";
    String REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private TextView mEmptyStateTextView;

    private BookAdapter mAdapter;

    private static final int BOOK_LOADER_ID = 1;

    private static final String STATE_ITEMS = "items";
    private static final String LIST_INSTANCE_STATE = "Saved Scroll Position";

    //Find a reference to the ListView in the layout
    ListView bookListView;


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

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);

        final Button submitButton = (Button) findViewById(R.id.search_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Once the search button is clicked, hide the search instructions
                TextView search_instructions = (TextView) findViewById(R.id.search_instruction);
                search_instructions.setVisibility(View.GONE);

                //Once the user hits search, the keyboard disappears
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                //Check if there is an active internet connection
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();

                //Initiate the loader.  Pass in the int ID constant defined above and pass in null for
                //the bundle.  Pass in this activity for the LoaderCAllbacks parameter (which is valid
                //because this activity implements the LoaderCallbacks interface).
                if (netInfo != null && netInfo.isConnected()){
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);
                    loaderManager.restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                } else {
                    TextView noInternetView = (TextView) findViewById(R.id.empty_view);
                    noInternetView.setText("No internet");

                    TextView nothingNewToDisplay = (TextView) findViewById(R.id.nothing_to_display_text);
                    nothingNewToDisplay.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        //Create a new loader for the given URL
        EditText search_bar = (EditText) findViewById(R.id.search_bar);
        userInput = search_bar.getText().toString();
        String search = REQUEST_URL + userInput;
        return new BookLoader(this, search);
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
            TextView search_instructions = (TextView) findViewById(R.id.search_instruction);
            search_instructions.setVisibility(View.GONE);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LIST_INSTANCE_STATE, bookListView.getFirstVisiblePosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int position = savedInstanceState.getInt(LIST_INSTANCE_STATE);
        bookListView.setSelection(position);
    }
}
