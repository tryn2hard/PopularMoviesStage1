package com.example.robot.popularmoviesstage1.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.example.robot.popularmoviesstage1.data.MovieContract;
import com.example.robot.popularmoviesstage1.utilities.NetworkUtils;
import com.example.robot.popularmoviesstage1.utilities.TMDBJsonUtils;

import java.net.URL;

/**
 * Created by Robot on 12/15/2017.
 */

public class MovieSyncTask {

    private static String user_sort_request;


    synchronized public static void syncMovie(Context context, String sort_request){

        user_sort_request = sort_request;
        try{
            URL movieRequestUrl = NetworkUtils.buildUrl(user_sort_request);

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

    synchronized public static void syncReviews(Context context, int movieId){

        try{
            URL movieRequestUrl = NetworkUtils.buildUrlForReviews(movieId);

            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

            ContentValues reviewValues = TMDBJsonUtils
                    .getUserReviews(context, jsonMovieResponse);

            String selection = Integer.toString(movieId);

            Uri movieUriWithId = MovieContract.MovieEntry.buildMovieUriWithId(movieId);

            if(reviewValues != null){
                ContentResolver movieContentResolver = context.getContentResolver();

                movieContentResolver.update(
                        movieUriWithId,
                        reviewValues,
                        selection,
                        null);


            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    synchronized public static void syncTrailers(Context context, int movieId) {

        try {
            URL movieRequestUrl = NetworkUtils.buildUrlForTrailers(movieId);

            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

            ContentValues trailerValues = TMDBJsonUtils
                    .getTrailers(context, jsonMovieResponse);

            String selection = Integer.toString(movieId);

            Uri movieUriWithId = MovieContract.MovieEntry.buildMovieUriWithId(movieId);

            if (trailerValues != null) {
                ContentResolver movieContentResolver = context.getContentResolver();

                movieContentResolver.update(
                        movieUriWithId,
                        trailerValues,
                        selection,
                        null);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
