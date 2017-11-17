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

    /**
     * Key values for the JSON Query
     */
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String STATIC_TMDB_URL =
            "https://api.themoviedb.org/3/movie/";

    private static final String MOVIE_BASE_URL = STATIC_TMDB_URL;

    private static final String api_key = "";

    private static final String language = "en-US";

    private static final String include_adult = "false";

    private static final String include_video = "false";

    private static final String page = "1";


    /**
     * Search parameters
     */
    private final static String API_KEY = "api_key";
    private final static String LANGUAGE_PARAM = "language";
    private final static String INCLUDE_ADULT_PARAM = "include_adult";
    private final static String INCLUDE_VIDEO_PARAM = "include_video";
    private final static String PAGE_PARAM = "page";

    /**
     * Method builds the proper url to request TMDB for movie data
     * @param sort_by
     * @return
     */
    public static URL buildUrl(String sort_by){

        String baseUrlWithSortPref = MOVIE_BASE_URL + sort_by;

        Uri builtUri = Uri.parse(baseUrlWithSortPref).buildUpon()
                .appendQueryParameter(API_KEY, api_key)
                .appendQueryParameter(INCLUDE_ADULT_PARAM, include_adult)
                .appendQueryParameter(INCLUDE_VIDEO_PARAM, include_video)
                .appendQueryParameter(LANGUAGE_PARAM, language)
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

    /**
     * Method to query TMDB and collect the JSON data.
     * Wish I knew a little more about scanner, but hey
     * maybe Udacity will cover it next time.
     * @param url
     * @return
     * @throws IOException
     */

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
