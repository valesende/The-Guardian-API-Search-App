package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chase on 7/23/2017.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    //creates a new article adapter with articles as the list of articles
    public ArticleAdapter (Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    //returns a list item view that displays information about the article
    //at a given position

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {
        //check for existing list item view (convertView) that we can reuse,
        //otherwise, convertView is null, and a new list item layout is inflated
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.article_list_item, parent, false);
        }

        //find the article at the given position in the list of articles
        Article currentArticle = getItem(position);

        //find the textView with the view id title
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);

        //get the title and store it in a string
        String titleText = currentArticle.getTitle();

        //set the title text to the corresponding text View
        titleView.setText(titleText);

        //find the textView with the id publication date
        TextView dateView = (TextView) listItemView.findViewById(R.id.publication_date);

        //get the date and store it in a string
        String dateText = currentArticle.getDate();

        //set the date text to the corresponding text View
        dateView.setText(dateText);

        //find the textView with the id section
        TextView sectionView = (TextView) listItemView.findViewById(R.id.section);

        //get the section and store it in a string
        String sectionText = currentArticle.getSection();

        //set the date text to the corresponding text View
        sectionView.setText(sectionText);

        return listItemView;
    }
}
