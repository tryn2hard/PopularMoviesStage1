package com.example.robot.popularmoviesstage1.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by Robot on 12/15/2017.
 */

public class MovieSyncUtils {

        public static void startImmediateSync(@NonNull final Context context){
            Intent intentForImmediateSync = new Intent(context, MovieSyncIntentService.class);
            context.startService(intentForImmediateSync);
        }
    }

