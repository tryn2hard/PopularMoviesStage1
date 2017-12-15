package com.example.robot.popularmoviesstage1.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Robot on 12/15/2017.
 */

public class MovieSyncIntentService extends IntentService {

    public MovieSyncIntentService(){super("MovieSyncIntentService");}

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        MovieSyncTask.syncMovie(this);
    }
}
