package com.example.robot.popularmoviesstage1.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Robot on 12/15/2017.
 */

public class MovieSyncIntentService extends IntentService {

    private static String user_sort_request;

    private static int movieId;

    public MovieSyncIntentService(){super("MovieSyncIntentService");}

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Creating our intents based on passed in keys. We can either sync all the movie data,
        // get user reviews, or get movie trailers
        if(intent.hasExtra("key")) {
            user_sort_request = intent.getStringExtra("key");
            MovieSyncTask.syncMovie(this, user_sort_request );
        } else if(intent.hasExtra("reviews_key")){
            movieId = intent.getIntExtra("reviews_key", 0);
            MovieSyncTask.syncReviews(this, movieId);
        } else if(intent.hasExtra("trailers_key")){
            movieId = intent.getIntExtra("trailers_key", 0);
            MovieSyncTask.syncTrailers(this, movieId);
        }

    }
}
