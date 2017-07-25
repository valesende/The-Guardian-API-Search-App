package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by chase on 7/23/2017.
 */

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    //tag for log messages
    private static final String LOG_TAG = ArticleLoader.class.getName();

    //query URL
    private String mUrl;

    //construct a new article loader that takes in context of the activity and the url to
    //load data from

    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    //this is in background thread

    @Override
    public List<Article> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        //perform the network request, parse the response, and extract the list of articles
        return QueryUtils.fetchArticleData(mUrl);
    }
}
