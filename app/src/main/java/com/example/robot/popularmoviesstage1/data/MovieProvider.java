package com.example.robot.popularmoviesstage1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Robot on 12/14/2017.
 */

public class MovieProvider extends ContentProvider {

    // int values for our uri matcher to use
    public static final int CODE_MOVIE = 100;
    public static final int CODE_MOVIE_WITH_ID = 101;
    public static final int CODE_MOVIE_FAVORITES = 200;
    public static final int CODE_FAV_MOVIE_WITH_ID = 201;


    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mMovieHelper;
    private FavoritesDbHelper mFavoriteHelper;

    // building our uriMatcher and adding the code values we'll be matching up with
    public static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, CODE_MOVIE);

        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", CODE_MOVIE_WITH_ID);

        matcher.addURI(authority, MovieContract.PATH_FAVORITES, CODE_MOVIE_FAVORITES);

        matcher.addURI(authority, MovieContract.PATH_FAVORITES + "/#", CODE_FAV_MOVIE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {

        // Creating our helpers to give us workability with our databases
        mMovieHelper = new MovieDbHelper(getContext());
        mFavoriteHelper = new FavoritesDbHelper(getContext());

        return true;
    }

    // Used only by the movie database. Done when we sync our movie data with the api
    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mMovieHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIE:
                db.beginTransaction();
                int rowsInserted = 0;

                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }

                    }

                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    // Getting readable data from the databases. Can either be a whole table or a single row.
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)){
            case CODE_MOVIE: {
                cursor = mMovieHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;

            }

            case CODE_MOVIE_WITH_ID: {
                String movieIdString = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{movieIdString};

                cursor = mMovieHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;

            }
            case CODE_MOVIE_FAVORITES:{
                cursor = mFavoriteHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME_FAVORITES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case CODE_FAV_MOVIE_WITH_ID: {
                String movieIdString = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{movieIdString};

                cursor = mFavoriteHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME_FAVORITES,
                        projection,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;

            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    // Using this to insert data from the movie table into the favorites table (done in detailMovieActivity)
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        SQLiteDatabase favoritesDatabase = mFavoriteHelper.getWritableDatabase();
        Long id = favoritesDatabase.insert(MovieContract.MovieEntry.TABLE_NAME_FAVORITES, null, values);

        if(id==-1){
            Log.e("Error", "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    // Deleting the database when we notice a change in sort request from popularity or rating. Not
    // used by the favorites table although I should probably give users the option of removing movies
    // from their favorites list
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;

        if (selection == null) selection = "1";

        switch(sUriMatcher.match(uri)){

            case CODE_MOVIE:
                numRowsDeleted = mMovieHelper.getWritableDatabase().delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;

            case CODE_FAV_MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = " _id=?";
                String[] mSelectionArgs = new String[]{id};
                numRowsDeleted = mFavoriteHelper.getWritableDatabase().delete(
                        MovieContract.MovieEntry.TABLE_NAME_FAVORITES,
                        mSelection,
                        mSelectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    // Updating the movie table with trailers and reviews.
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match){

            case CODE_MOVIE_WITH_ID:
                selection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{uri.getLastPathSegment()};

               SQLiteDatabase database = mMovieHelper.getWritableDatabase();

               int rowsUpdated = database.update(
                       MovieContract.MovieEntry.TABLE_NAME,
                       values,
                       selection,
                       selectionArgs);

               if(rowsUpdated != 0){
                   getContext().getContentResolver().notifyChange(uri, null);

                   return rowsUpdated;
               }
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

    }
}
