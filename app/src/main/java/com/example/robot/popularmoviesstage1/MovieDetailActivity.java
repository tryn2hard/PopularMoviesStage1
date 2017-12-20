package com.example.robot.popularmoviesstage1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robot.popularmoviesstage1.data.MovieContract;
import com.example.robot.popularmoviesstage1.data.MovieDbHelper;
import com.example.robot.popularmoviesstage1.sync.MovieSyncUtils;
import com.example.robot.popularmoviesstage1.utilities.PopularMoviesUtils;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    private static final String[] DATA_COLUMNS_FROM_CONTENT_PROVIDER = {
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER,
            MovieContract.MovieEntry.COLUMN_VOTE_AVG,
            MovieContract.MovieEntry.COLUMN_SYNOPSIS,
            MovieContract.MovieEntry.COLUMN_TRAILERS,
            MovieContract.MovieEntry.COLUMN_REVIEWS,};

    private static final int MOVIE_INDEX_MOVIE_ID = 0;
    private static final int MOVIE_INDEX_TITLE = 1;
    private static final int MOVIE_INDEX_RELEASE_DATE = 2;
    private static final int MOVIE_INDEX_MOVIE_POSTER = 3;
    private static final int MOVIE_INDEX_VOTE_AVG = 4;
    private static final int MOVIE_INDEX_SYNOPSIS = 5;
    private static final int MOVIE_INDEX_TRAILERS = 6;
    private static final int MOVIE_INDEX_REVIEWS = 7;


    private static final int ID_DETAIL_ACTIVITY_LOADER = 23;

    private Uri mUri;

    private String YoutubeTrailerLink;

    private String cursorTitle;
    private String cursorPoster;
    private String cursorSynopsis;
    private String cursorReleaseDate;
    private int cursorRating;
    private String cursorReviews;
    private int cursorMovieId;
    private String cursorTrailer;


    // Variable declarations
    TextView mMovieTitle;
    ImageView mMoviePoster;
    TextView mMovieSynopsis;
    TextView mMovieReleaseDate;
    TextView mMovieRating;
    TextView mReviews;
    TextView mTrailers;
    ImageView mTrailerPlay;
    ImageView mFavorite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);


        // Linking the variables to their respective view parts.
        mMovieTitle = findViewById(R.id.tv_movie_title);
        mMovieSynopsis = findViewById(R.id.tv_detail_synopsis);
        mMovieReleaseDate = findViewById(R.id.tv_detail_release_date);
        mMovieRating = findViewById(R.id.tv_detail_rating);
        mMoviePoster = findViewById(R.id.iv_detail_movie_poster);
        mReviews = findViewById(R.id.tv_detail_reviews);
        mTrailers = findViewById(R.id.tv_detail_trailers);
        mTrailerPlay = findViewById(R.id.iv_detail_trailer_play);
        mFavorite = findViewById(R.id.iv_detail_favorites);

        /**
         *   Let's get the intent from Main, and if it has data
         *   pull it out and attach it to the view parts.
         **/
        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {

            mUri = intentThatStartedThisActivity.getData();

            if(mUri == null) {
                throw new NullPointerException("uri is null");
            }
                String movieId = mUri.getLastPathSegment();
                MovieSyncUtils.startSyncForReviews(this, Integer.parseInt(movieId));
                MovieSyncUtils.startSyncForTrailers(this, Integer.parseInt(movieId));
                getSupportLoaderManager().initLoader(ID_DETAIL_ACTIVITY_LOADER, null, this);

        }

        mTrailerPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                Intent implicit = new Intent(Intent.ACTION_VIEW, Uri.parse(YoutubeTrailerLink));
                startActivity(implicit);
            }
        });

        mFavorite.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                addToFavorites();
                Toast.makeText(MovieDetailActivity.this, "You've added this movie to your favorites.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id){
            case ID_DETAIL_ACTIVITY_LOADER:
                return new CursorLoader(this,
                        mUri,
                        DATA_COLUMNS_FROM_CONTENT_PROVIDER,
                        null,
                        null,
                        null);
                default:
                    throw new RuntimeException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        String cursorTrailer;

        if(data == null) return;

        data.moveToNext();

        assignValues(data);

        cursorTrailer = data.getString(MOVIE_INDEX_TRAILERS);

        trailerLinkBuilder(cursorTrailer);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader = null;

    }

    public void assignValues(Cursor data){

        cursorTitle = data.getString(MOVIE_INDEX_TITLE);
        cursorReleaseDate = data.getString(MOVIE_INDEX_RELEASE_DATE);
        cursorPoster = data.getString(MOVIE_INDEX_MOVIE_POSTER);
        cursorRating = data.getInt(MOVIE_INDEX_VOTE_AVG);
        cursorSynopsis = data.getString(MOVIE_INDEX_SYNOPSIS);
        cursorReviews = data.getString(MOVIE_INDEX_REVIEWS);
        cursorMovieId = data.getInt(MOVIE_INDEX_MOVIE_ID);
        cursorTrailer = data.getString(MOVIE_INDEX_TRAILERS);


        mMovieTitle.setText(cursorTitle);
        mMovieReleaseDate.setText(cursorReleaseDate);
        String posterUrl = PopularMoviesUtils.imageUrlBuilder(cursorPoster);
        Picasso.with(this).load(posterUrl).into(mMoviePoster);
        mMovieRating.setText(Integer.toString(cursorRating));
        mMovieSynopsis.setText(cursorSynopsis);
        mReviews.setText(cursorReviews);

    }

    private void trailerLinkBuilder(String youtubeKey){
        String youtubeLink = "https://www.youtube.com/watch?v=";
        String trailerLink = youtubeLink + youtubeKey;
        YoutubeTrailerLink = trailerLink;
    }

    private void addToFavorites() {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, cursorMovieId);
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, cursorTitle);
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, cursorPoster);
        values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVG, cursorRating);
        values.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, cursorSynopsis);
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, cursorReleaseDate);
        values.put(MovieContract.MovieEntry.COLUMN_TRAILERS, cursorTrailer);
        values.put(MovieContract.MovieEntry.COLUMN_REVIEWS, cursorReviews);

        Uri newUri = getContentResolver().insert(
                MovieContract.MovieEntry.CONTENT_URI_FAVORITES, values);

        if (newUri == null){
            Toast.makeText(this, "Error adding to favorites",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
