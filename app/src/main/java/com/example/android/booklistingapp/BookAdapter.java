package com.example.android.booklistingapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thodzic on 7/28/17.
 */

public class BookAdapter extends ArrayAdapter<Book> {


    /**
     * Creates a new Book adapter
     *
     * @param context of the app
     * @param books   list of books, which is the date source of the adapter
     */
    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    /**
     * Returns a list item view that displays information about the book at the given
     * position in the list of books.
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Check if there is an existing list item view(called convertView) that we can reuse.
        //otherwise, if convertView is null, inflate a new list item layout.

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        //Find the book at the given position in the list of books
        Book currentBook = getItem(position);

        //Find the TextView with the ID Book Title
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        //Display the title in that view
        titleView.setText(currentBook.getTitle().replace("[", "").replace("]", " ").replace("\"", "")
                .replace(",", " "));

        //Find the TextView with the ID Author
        TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        //Display the author in that view
        authorView.setText(currentBook.getAuthor());

        return listItemView;

    }
}
