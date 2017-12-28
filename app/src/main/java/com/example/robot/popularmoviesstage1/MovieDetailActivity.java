package com.example.robot.popularmoviesstage1;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robot.popularmoviesstage1.databinding.ActivityMovieDetailBinding;
import com.example.robot.popularmoviesstage1.data.MovieContract;
import com.example.robot.popularmoviesstage1.data.MovieDbHelper;
import com.example.robot.popularmoviesstage1.sync.MovieSyncUtils;
import com.example.robot.popularmoviesstage1.utilities.PopularMoviesUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        TrailerAdapter.TrailerAdapterOnClickHandler {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    private static final String[] DATA_COLUMNS_FROM_CONTENT_PROVIDER = {
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER,
            MovieContract.MovieEntry.COLUMN_VOTE_AVG,
            MovieContract.MovieEntry.COLUMN_SYNOPSIS,
            MovieContract.MovieEntry.COLUMN_TRAILERS,
            MovieContract.MovieEntry.COLUMN_REVIEWS};

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

    private static Cursor mCursor;

    private ActivityMovieDetailBinding mBinding;

    private String cursorTitle;
    private String cursorPoster;
    private String cursorSynopsis;
    private String cursorReleaseDate;
    private int cursorRating;
    private String cursorReviews;
    private int cursorMovieId;
    private String cursorTrailer;

    private RecyclerView mTrailerRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        /**
         *   Let's get the intent from Main, and if it has data
         *   pull it out and attach it to the view parts
         **/

        if (savedInstanceState != null) {
            String savedUri = savedInstanceState.getString("savedUri");
            mUri = Uri.parse(savedUri);
            getSupportLoaderManager().restartLoader(ID_DETAIL_ACTIVITY_LOADER, null, this);
        }else {
            Intent intentThatStartedThisActivity = getIntent();
            if (intentThatStartedThisActivity != null) {
                mUri = intentThatStartedThisActivity.getData();
            }
        }

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

        if (mUri == null) {
            throw new NullPointerException("uri is null");
        }
        String movieId = mUri.getLastPathSegment();
        MovieSyncUtils.startSyncForTrailers(this, Integer.parseInt(movieId));
        MovieSyncUtils.startSyncForReviews(this, Integer.parseInt(movieId));

        getSupportLoaderManager().initLoader(ID_DETAIL_ACTIVITY_LOADER, null, this);

             mBinding.ivDetailFavorites.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addToFavorites();
                Toast.makeText(MovieDetailActivity.this, "You've added this movie to your favorites.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
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
        mCursor = data;

        if (mCursor == null) return;

        mCursor.moveToNext();

        fillUiWithData(mCursor);

        assignValuesFromCursor(mCursor);

        stringBreak(mCursor.getString(MOVIE_INDEX_TRAILERS));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader = null;

    }

    private void fillUiWithData(Cursor data) {
        mBinding.tvMovieTitle.setText(data.getString(MOVIE_INDEX_TITLE));
        mBinding.tvDetailReleaseDate.setText(data.getString(MOVIE_INDEX_RELEASE_DATE));
        String cursorPoster = data.getString(MOVIE_INDEX_MOVIE_POSTER);
        mBinding.tvDetailRating.setText(Integer.toString(data.getInt(MOVIE_INDEX_VOTE_AVG)));
        mBinding.tvDetailSynopsis.setText(data.getString(MOVIE_INDEX_SYNOPSIS));
        mBinding.tvDetailReviews.setText(data.getString(MOVIE_INDEX_REVIEWS));

        String posterUrl = PopularMoviesUtils.imageUrlBuilder(cursorPoster);
        Picasso.with(this).load(posterUrl).into(mBinding.ivDetailMoviePoster);
    }

    private void assignValuesFromCursor(Cursor data){
        cursorTitle = data.getString(MOVIE_INDEX_TITLE);
        cursorPoster = data.getString(MOVIE_INDEX_MOVIE_POSTER);
        cursorSynopsis = data.getString(MOVIE_INDEX_SYNOPSIS);
        cursorReleaseDate = data.getString(MOVIE_INDEX_RELEASE_DATE);
        cursorRating = data.getInt(MOVIE_INDEX_VOTE_AVG);
        cursorReviews = data.getString(MOVIE_INDEX_REVIEWS);
        cursorMovieId = data.getInt(MOVIE_INDEX_MOVIE_ID);
        cursorTrailer = data.getString(MOVIE_INDEX_TRAILERS);
    }


    private String trailerLinkBuilder(String youtubeKey) {
        String youtubeLink = "https://www.youtube.com/watch?v=";
        String trailerLink = youtubeLink + youtubeKey;
        return trailerLink;
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

        if (newUri == null) {
            Toast.makeText(this, "Error adding to favorites",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void stringBreak(String trailers) {
        if (trailers != null) {
            String[] elements = trailers.split(", ");
            List<String> fixedLengthList = Arrays.asList(elements);
            ArrayList<String> trailerList = new ArrayList<String>(fixedLengthList);
            Log.d("stringBreak", "Passed in string = " + trailers);
            Log.d("stringBreak_list", "List " + trailerList);
            adapterStarter(trailerList);
        }

    }

    private void adapterStarter(ArrayList<String> movieTrailers){

        mTrailerRecycler = mBinding.recyclerviewTrailers;

        mTrailerRecycler.setLayoutManager(new LinearLayoutManager(this));

        mTrailerRecycler.setHasFixedSize(true);

        if(movieTrailers != null) {
            TrailerAdapter adapter = new TrailerAdapter(this, movieTrailers, this);
            mTrailerRecycler.setAdapter(adapter);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String stringUri = mUri.toString();
        Log.d("uriSaving", "Uri = " + stringUri);
        outState.putString("savedUri", stringUri);
    }

    @Override
    public void onClick(String trailerId) {
        if(trailerId != null) {
            String trailerLink;
            trailerLink = trailerLinkBuilder(trailerId);
            Intent implicit = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerLink));
            startActivity(implicit);
        }

    }

}
