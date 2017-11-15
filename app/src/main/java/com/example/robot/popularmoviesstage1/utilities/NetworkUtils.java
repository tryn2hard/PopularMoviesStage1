package com.example.robot.popularmoviesstage1.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Robot on 11/14/2017.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String STATIC_TMDB_URL =
            "https://api.themoviedb.org/3/discover/movie?";

    private static final String MOVIE_BASE_URL = STATIC_TMDB_URL;

    private static final String api_key = "";

    private static final String language = "en-US";

    private static final String sort_by_populartiy = "popularity.desc";

    private static final String sort_by_rating = "vote_count.desc";

    private static final String include_adult = "false";

    private static final String include_video = "false";

    private static final String page = "1";



    final static String API_KEY = "api_key";
    final static String LANGUAGE_PARAM = "language";
    final static String SORT_BY_PARAM = "sort_by";
    final static String INCLUDE_ADULT_PARAM = "include_adult";
    final static String INCLUDE_VIDEO_PARAM = "include_video";
    final static String PAGE_PARAM = "page";

    public static URL buildUrl(String sort_by){
        String sort_request;
        if(sort_by.contains("popularity")){
            sort_request = sort_by_populartiy;
        }
        else{
            sort_request = sort_by_rating;
        }
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY, api_key)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .appendQueryParameter(SORT_BY_PARAM, sort_request)
                .appendQueryParameter(INCLUDE_ADULT_PARAM, include_adult)
                .appendQueryParameter(INCLUDE_VIDEO_PARAM, include_video)
                .appendQueryParameter(PAGE_PARAM, page)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI" + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
