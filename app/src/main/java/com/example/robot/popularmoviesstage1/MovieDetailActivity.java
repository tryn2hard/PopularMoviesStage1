package com.example.robot.popularmoviesstage1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewDebug;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robot.popularmoviesstage1.data.MovieContract;
import com.example.robot.popularmoviesstage1.data.MovieDbHelper;
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
            MovieContract.MovieEntry.COLUMN_REVIEWS,
            MovieContract.MovieEntry.COLUMN_FAVORITES};

    private static final int MOVIE_INDEX_MOVIE_ID = 0;
    private static final int MOVIE_INDEX_TITLE = 1;
    private static final int MOVIE_INDEX_RELEASE_DATE = 2;
    private static final int MOVIE_INDEX_MOVIE_POSTER = 3;
    private static final int MOVIE_INDEX_VOTE_AVG = 4;
    private static final int MOVIE_INDEX_SYNOPSIS = 5;
    private static final int MOVIE_INDEX_TRAILERS = 6;
    private static final int MOVIE_INDEX_REVIEWS = 7;
    private static final int MOVIE_INDEX_FAVORITES = 8;

    private static final int ID_DETAIL_ACTIVITY_LOADER = 23;

    private Uri mUri;

    // Variable declarations
    TextView mMovieTitle;
    ImageView mMoviePoster;
    TextView mMovieSynopsis;
    TextView mMovieReleaseDate;
    TextView mMovieRating;


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

        /**
         *   Let's get the intent from Main, and if it has data
         *   pull it out and attach it to the view parts.
         **/
        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {

            if (intentThatStartedThisActivity.hasExtra(MainActivity.INTENT_EXTRA_KEY)) {
                int id = getIntent().getIntExtra(MainActivity.INTENT_EXTRA_KEY, 0);

                String path_id = Integer.toString(id);

                Uri movieUri = MovieContract.MovieEntry.CONTENT_URI.buildUpon()
                        .appendPath(path_id)
                        .build();

                mUri = movieUri;

                getSupportLoaderManager().initLoader(ID_DETAIL_ACTIVITY_LOADER, null, this);

            }
        }
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
        String title = data.getString(MOVIE_INDEX_TITLE);
        mMovieTitle.setContentDescription(title);
        String date = data.getString(MOVIE_INDEX_RELEASE_DATE);
        mMovieReleaseDate.setText(date);
        String poster = data.getString(MOVIE_INDEX_MOVIE_POSTER);
        String posterUrl = PopularMoviesUtils.imageUrlBuilder(poster);
        Picasso.with(this).load(posterUrl).into(mMoviePoster);
        int voteAvg = data.getInt(MOVIE_INDEX_VOTE_AVG);
        mMovieRating.setText(voteAvg);
        String synopsis = data.getString(MOVIE_INDEX_SYNOPSIS);
        mMovieSynopsis.setText(synopsis);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
