package com.example.robot.popularmoviesstage1.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by Robot on 12/15/2017.
 */

public class MovieSyncUtils {

        public static void startImmediateSync(@NonNull final Context context, String sort_request){
            Intent intentForImmediateSync = new Intent(context, MovieSyncIntentService.class);
            intentForImmediateSync.putExtra("key", sort_request);
            context.startService(intentForImmediateSync);
        }

        public static void startSyncForReviews(@NonNull final Context context, int movieId){
            Intent intentForReviews = new Intent(context, MovieSyncIntentService.class);
            intentForReviews.putExtra("reviews_key", movieId);
            context.startService(intentForReviews);
        }

        public static void startSyncForTrailers(@NonNull final Context context, int movieId){
            Intent intentForTrailers = new Intent(context, MovieSyncIntentService.class);
            intentForTrailers.putExtra("trailers_key", movieId);
            context.startService(intentForTrailers);
        }
    }

