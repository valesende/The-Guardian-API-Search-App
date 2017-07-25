package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chase on 7/23/2017.
 */

public final class QueryUtils {

    //tag for log messages
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    //query the data set and return a list of article objects
    public static List<Article> fetchArticleData(String requestUrl) {

        //create url object
        URL url = createUrl(requestUrl);

        //perform HTTP request to the url and receive a JSON response
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Article> articles = extractResultsFromJson(jsonResponse);

        //return the list of articles
        return articles;
    }

    //returns new URL object from given string URL
    private static URL createUrl (String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        } return url;
    }

    //makes the HTTP request to the url and return a string as a response
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //if the URL is null, return early
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //if the request was successful (response code 200), then read the
            //input stream and parse the response
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the article JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                //closing the input stream may throw an IO exception which is why
                // the makeHTTprequest method signature specifies that an IOexception
                //could be thrown
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //convert the input stream into a string which contains the entire json response
    //from the server
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //reutrn a list of Article objects that has been created from parsing the
    //JSON response
    private static List<Article> extractResultsFromJson(String articleJSON) {
        //if the JSON string is empty or null, return early
        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }

        //create an empty array list so we can add articles to it
        List<Article> articles = new ArrayList<>();

        //try to parse the JSON response string. if there's a problem with the way
        //the JSON is formatted, a JSONException object will be thrown.
        //the following is used to catch the exception so the app doesn't crash
        try {
            //create a jsonObject from the json response string
            JSONObject baseJsonResponse = new JSONObject(articleJSON);

            //check if "results" exists for a particular article,
            //if so, extract the JSONArray associated with the key called "results", which
            //represents a list of results (articles)
            JSONObject responseObject = baseJsonResponse.getJSONObject("response");
            JSONArray articleArray = responseObject.getJSONArray("results");

            //for each article in the articleArray, create an Article object
            for (int i = 0; i < articleArray.length(); i++) {

                //get a single article at position i within the list of articles
                JSONObject currentArticle = articleArray.getJSONObject(i);

                //extract the value for the key called "webTitle"
                String title = currentArticle.getString("webTitle");

                //extract the value for the key called "sectionId"
                String sectionId = currentArticle.getString("sectionId");

                //extract the value for the key called "webPublicationDate"
                String date = currentArticle.getString("webPublicationDate");

                //extract the link to the article on the guardian website
                String url = currentArticle.getString("webUrl");

                //create a new Article object with the title, sectionId, date, and url
                Article article = new Article(title, sectionId, date, url);

                //add the new Article to the list of articles
                articles.add(article);
                }

            } catch (JSONException e) {
                //if an error is thrown while executing any of the above statements in the try block,
                //catch the exception here, so the app doesn't crash. Print a log message
                //with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the article JSON results", e);
            }

            //return the list of articles
        return articles;
        }
    }


