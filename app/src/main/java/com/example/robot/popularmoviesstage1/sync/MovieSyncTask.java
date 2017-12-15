package com.example.robot.popularmoviesstage1.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.robot.popularmoviesstage1.data.MovieContract;
import com.example.robot.popularmoviesstage1.utilities.NetworkUtils;
import com.example.robot.popularmoviesstage1.utilities.TMDBJsonUtils;

import java.net.URL;

/**
 * Created by Robot on 12/15/2017.
 */

public class MovieSyncTask {

    synchronized public static void syncMovie(Context context){

        try{
            URL movieRequestUrl = NetworkUtils.buildUrl("popular?");

            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

            ContentValues[] movieValues = TMDBJsonUtils
                    .getMovieFromJson(context, jsonMovieResponse);

            if(movieValues != null&& movieValues.length != 0){
                ContentResolver movieContentResolver = context.getContentResolver();

                movieContentResolver.delete(MovieContract.MovieEntry.CONTENT_URI, null, null);

                movieContentResolver.bulkInsert(MovieContract.MovieEntry.CONTENT_URI,
                        movieValues);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
