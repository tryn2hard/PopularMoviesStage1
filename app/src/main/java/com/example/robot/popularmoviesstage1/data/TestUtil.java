package com.example.robot.popularmoviesstage1.data;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robot on 12/13/2017.
 */

public class TestUtil {

    public static void insertFakeData(SQLiteDatabase db){
        if(db == null){
            return;
        }

        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 346364);
        cv.put(MovieContract.MovieEntry.COLUMN_TITLE, "It" );
        cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "2017-09-05");
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, "/9E2y5Q7WlCVNEhP5GiVTjhEhx1o.jpg");
        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVG, 7.2);
        cv.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, "In a small town in Maine, seven children " +
                "known as The Losers Club come face to face with life problems, bullies and a " +
                "monster that takes the shape of a clown called Pennywise.");
        list.add(cv);

        cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 8844);
        cv.put(MovieContract.MovieEntry.COLUMN_TITLE, "Jumanji" );
        cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "1995-12-15");
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, "/8wBKXZNod4frLZjAKSDuAcQ2dEU.jpg");
        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVG, 6.9);
        cv.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, "When siblings Judy and Peter discover an" +
                " enchanted board game that opens the door to a magical world, they unwittingly" +
                " invite Alan -- an adult who's been trapped inside the game for 26 years -- into" +
                " their living room. Alan's only hope for freedom is to finish the game, which " +
                "proves risky as all three find themselves running from giant rhinoceroses, evil " +
                "monkeys and other terrifying creatures.");

        list.add(cv);

        //insert all guests in one transaction
        try
        {
            db.beginTransaction();
            //clear the table first
            db.delete (MovieContract.MovieEntry.TABLE_NAME,null,null);
            //go through the list and add one by one
            for(ContentValues c:list){
                db.insert(MovieContract.MovieEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally
        {
            db.endTransaction();
        }


    }
}
