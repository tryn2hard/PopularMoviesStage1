package com.example.robot.popularmoviesstage1.utilities;



import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.robot.popularmoviesstage1.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Robot on 11/14/2017.
 */

/**
 * All the data has to be extracted out, and by golly it was not fun.
 * Good thing I did it a few times beforehand.
 */
public final class TMDBJsonUtils {

    private static final String TMDB_MOVIE_ID = "id";

    private static final String TMDB_RESULTS = "results";

    private static final String TMDB_TITLE = "title";

    private static final String TMDB_VOTE_AVERAGE = "vote_average";

    private static final String TMDB_POSTER_PATH = "poster_path";

    private static final String TMDB_OVERVIEW = "overview";

    private static final String TMDB_RELEASE_DATE = "release_date";

    private static final String TMDB_USER_REVIEWS = "content";

    private static final String TMDB_VIDEOS = "key";

    public static ContentValues[] getMovieFromJson(Context context, String movieJsonStr)
            throws JSONException {


        JSONObject moviesJson = new JSONObject(movieJsonStr);

        JSONArray results = moviesJson.getJSONArray(TMDB_RESULTS);

        ContentValues[] movieContentValues = new ContentValues[results.length()];

        for (int i = 0; i < results.length(); i++) {

            JSONObject r = results.getJSONObject(i);
            int movie_id = r.getInt(TMDB_MOVIE_ID);
            String title = r.getString(TMDB_TITLE);
            String release_date = r.getString(TMDB_RELEASE_DATE);
            String poster_path = r.getString(TMDB_POSTER_PATH);
            Double vote_average = r.getDouble(TMDB_VOTE_AVERAGE);
            String overview = r.getString(TMDB_OVERVIEW);




            ContentValues cv = new ContentValues();
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie_id);
            cv.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
            cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, release_date);
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, poster_path);
            cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVG, vote_average);
            cv.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, overview);

            movieContentValues[i] = cv;

        }

        return movieContentValues;
    }

    public static ContentValues getUserReviews (Context context, String reviewsJsonString)
    throws JSONException {

        JSONObject reviewsJson = new JSONObject(reviewsJsonString);

        JSONArray results = reviewsJson.getJSONArray(TMDB_RESULTS);

        ContentValues reviewContentValues = new ContentValues();

            JSONObject r = results.getJSONObject(0);
            String reviews = r.getString(TMDB_USER_REVIEWS);
            Log.d("TMDBJsonUtils", reviews);
            reviewContentValues.put(MovieContract.MovieEntry.COLUMN_REVIEWS, reviews);

        return reviewContentValues;
    }

    public static ContentValues getTrailers (Context context, String trailersJsonString)
            throws JSONException {

        JSONObject reviewsJson = new JSONObject(trailersJsonString);

        JSONArray results = reviewsJson.getJSONArray(TMDB_RESULTS);

        ContentValues reviewContentValues = new ContentValues();

        JSONObject r = results.getJSONObject(0);
        String videos = r.getString(TMDB_VIDEOS);
        Log.d("TMDBJsonUtils", videos);
        reviewContentValues.put(MovieContract.MovieEntry.COLUMN_TRAILERS, videos);

        return reviewContentValues;
    }
}
