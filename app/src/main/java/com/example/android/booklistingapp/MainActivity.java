package com.example.android.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.app.LoaderManager.LoaderCallbacks;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    final static String REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=android";

    private BookAdapter mAdapter;

    private static final int BOOK_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        //Find a reference to the ListView in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of books as imput
        mAdapter = new BookAdapter(this,new ArrayList<Book>());

        //Set the adapter on the ListView
        //so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);

        //Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        //Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        final Button submitButton = (Button) findViewById(R.id.search_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                //Find a reference to the ListView in the layout
                ListView bookListView = (ListView) findViewById(R.id.list);

                // Create a new adapter that takes an empty list of books as imput
                mAdapter = new BookAdapter(this,new ArrayList<Book>());

                //Set the adapter on the ListView
                //so the list can be populated in the user interface
                bookListView.setAdapter(mAdapter);

                //Get a reference to the ConnectivityManager to check state of network connectivity
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);

                //Get details on the currently active default data network
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                LoaderManager loaderManager = getLoaderManager();

                //Initiate the loader.  Pass in the int ID constant defined above and pass in null for
                //the bundle.  Pass in this activity for the LoaderCAllbacks parameter (which is valid
                //because this activity implements the LoaderCallbacks interface).
                loaderManager.initLoader(BOOK_LOADER_ID, null, this);
            }
        });
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        //Create a new loader for the given URL
        return new BookLoader(this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        //Clear the adapter of previous book data
        mAdapter.clear();
        //If there is a valid list of books, then add them to the adapter's dataset.  This will
        //trigger the ListView to update.
        if(books !=null && !books.isEmpty()){
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        //Loader reset, so we can clear out our exisiting data
        mAdapter.clear();
    }
}
