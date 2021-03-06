package com.example.robot.popularmoviesstage1.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Robot on 12/13/2017.
 */

public class MovieContract {

    // Name of the content provider
    public static final String CONTENT_AUTHORITY = "com.example.robot.popularmoviesstage1";

    // base URI to access the data
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // path to access movie data
    public static final String PATH_MOVIE = "movie";

    // path to access favorite movie data
    public static final String PATH_FAVORITES = "favorites";

    // Inner class that defines the table contents of the movie table
    public static final class MovieEntry implements BaseColumns {

        // The base CONTENT_URI used to query the movie table from the content provider
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        // The base content uri used to query the favorite table from the content provider
        public static final Uri CONTENT_URI_FAVORITES = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();

        // Used inside the database as the name of the table
        public static final String TABLE_NAME = "movie";

        // Name for the favorites table
        public static final String TABLE_NAME_FAVORITES = "favorites";

        /* the various columns we will have in the table (schema??)
        so the plan is to have the following:
            _id(used by the table to identify position)
            movie_id(used to do extra searches to find reviews and trailers
            title(self-explanatory)
            movie_poster(url to get the jpg)
            vote_avg(the overall rating)
            plot(short and sweet deets about the flick)
            trailers(probably a url to pass to an intent to open in youtube or chrome)
            reviews(bet some people had some nasty things to say about some of these movies)
            favorites(plan on using this to create a favorite_movie activity
         */

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_MOVIE_POSTER = "movie_poster";

        public static final String COLUMN_VOTE_AVG = "vote_avg";

        public static final String COLUMN_SYNOPSIS = "synopsis";

        public static final String COLUMN_TRAILERS = "trailers";

        public static final String COLUMN_REVIEWS = "reviews";

        // Helper method to build the uri for the moive's table with the movie id attached
        public static Uri buildMovieUriWithId(int id){
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }

        // Helper method to build the uri for the favorite's table with the movie id attached
        public static Uri buildFavMovieUriWithId(int id){
            return CONTENT_URI_FAVORITES.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }
    }


}
