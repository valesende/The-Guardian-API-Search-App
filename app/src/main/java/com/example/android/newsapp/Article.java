package com.example.android.newsapp;

/**
 * Created by chase on 7/23/2017.
 */

public class Article {

    //title of article
    private String mTitle;

    //section of article
    private String mSection;

    //web publication date of the article
    private String mDate;

    //link to web article
    private String mUrl;

    //construct a new Article object
    public Article (String title, String section, String date, String url){
        mTitle = title;
        mSection = section;
        mDate = date;
        mUrl = url;
    }

    //returns article title
    public String getTitle() { return mTitle; }

    //return article section
    public String getSection() { return mSection; }

    //return web publication date
    public String getDate() { return mDate; }

    //return web url
    public String getUrl() { return mUrl; }
}
