package com.example.robot.popularmoviesstage1.utilities;

import com.example.robot.popularmoviesstage1.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Robot on 11/14/2017.
 */

/**
 * All the data has to be extracted out, and by golly it was not fun.
 * Good thing I did it a few times beforehand.
 */
public final class TMDBJsonUtils {

    public static ArrayList<Movies> getMovieFromJson(String movieJsonStr)
            throws JSONException {

        final String TMDB_RESULTS = "results";

        final String TMDB_TITLE = "title";

        final String TMDB_VOTE_AVERAGE = "vote_average";

        final String TMDB_POSTER_PATH = "poster_path";

        final String TMDB_OVERVIEW = "overview";

        final String TMDB_RELEASE_DATE = "release_date";

        ArrayList<Movies> movies = new ArrayList<>();

        JSONObject moviesJson = new JSONObject(movieJsonStr);

        JSONArray results = moviesJson.getJSONArray(TMDB_RESULTS);

        for (int i = 0; i < results.length(); i++) {

            JSONObject r = results.getJSONObject(i);
            String title = r.getString(TMDB_TITLE);
            Double vote_average = r.getDouble(TMDB_VOTE_AVERAGE);
            String poster_path = r.getString(TMDB_POSTER_PATH);
            String overview = r.getString(TMDB_OVERVIEW);
            String release_date = r.getString(TMDB_RELEASE_DATE);

            movies.add(new Movies(title, poster_path, overview, release_date, vote_average));

        }

        return movies;
    }
}
