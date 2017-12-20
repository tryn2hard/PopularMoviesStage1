package com.example.robot.popularmoviesstage1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Robot on 12/19/2017.
 */
// Creating the database schema
public class FavoritesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favorites.db";

    private static final int DATABASE_VERSION = 2;

    public FavoritesDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE =

                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME_FAVORITES + " (" +

                        MovieContract.MovieEntry._ID        + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +

                        MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +

                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +

                        MovieContract.MovieEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +

                        MovieContract.MovieEntry.COLUMN_VOTE_AVG + " INTEGER NOT NULL, " +

                        MovieContract.MovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, " +

                        MovieContract.MovieEntry.COLUMN_TRAILERS + " TEXT, " +

                        MovieContract.MovieEntry.COLUMN_REVIEWS + " TEXT, " +

                        // this handy bit of code helps to make sure a movie is inputted twice into our favorites table
                        " UNIQUE (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME_FAVORITES);
        onCreate(db);
    }
}
