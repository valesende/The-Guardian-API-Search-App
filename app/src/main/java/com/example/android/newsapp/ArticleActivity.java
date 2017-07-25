package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends AppCompatActivity
 implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String LOG_TAG = ArticleActivity.class.getName();

    //constant variable for the article loader ID, only used if there are multiple loaders
    private static final int ARTICLE_LOADER_ID = 1;

    private static final String JSON_URL = "http://content.guardianapis.com/search?q=science&api-key=test";

    //adapter for article list
    private ArticleAdapter mAdadpter;

    //text view to display when list is empty
    public TextView mEmptyStateTextView;

    //function that checks for network connectivity - is the user online or not?
    public boolean userIsOnline() {
        //gets reference to connectivity manager
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService
                (Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_main);

        //find a reference to the ListView
        ListView articleListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        articleListView.setEmptyView(mEmptyStateTextView);

        //create a new adapter that takes an empty list of articles as input
        mAdadpter = new ArticleAdapter(this, new ArrayList<Article>());

        //set the adapter on the ListView so the list can be populated
        articleListView.setAdapter(mAdadpter);

        //set an item on click listener on the ListView, so when each article is clicked, an intent
        //is sent to the browser to open the desired article's url
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //find the current article that was clicked on
                Article currentArticle = mAdadpter.getItem(position);

                //convert the string url into a uri object to pass into the intent constructor
                Uri articleUri = Uri.parse(currentArticle.getUrl());

                //create a new intent to view the  article URL
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                //send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        //fetch results of query if internet connection is available
        if (userIsOnline()) {
            getLoaderManager().initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            //hide the loading indicator so we can see the error message
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            //if no connection, update the empty text view with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle bundle) {

        return new ArticleLoader(this, JSON_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        //hide the loading indicator after the articles are loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        //set empty state text to display "no articles found"
        mEmptyStateTextView.setText(R.string.no_articles);

        //clear the adapter of previous data
        mAdadpter.clear();

        //if there is a valid list of articles then add them to the adapter's articles set
        //this causes the listview to update
        if (articles != null & !articles.isEmpty()) {
            mAdadpter.addAll(articles);
        }
    }

        @Override
        public void onLoaderReset(Loader<List<Article>> loader) {
            //loader resets and clears the existing data
           mAdadpter.clear();
        }
    }

