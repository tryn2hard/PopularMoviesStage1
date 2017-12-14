package com.example.robot.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.robot.popularmoviesstage1.data.MovieContract;
import com.example.robot.popularmoviesstage1.data.MovieDbHelper;
import com.example.robot.popularmoviesstage1.utilities.TestUtil;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    // Member Variable declarations
    private String sort_request;

    private static final String USER_PREF = "pref";

    private static final String SORT_PREF_KEY = "sort";

    public static final String SORT_PREF_VOTE = "top_rated?";

    public static final String SORT_PREF_POP = "popular?";

    public static final String INTENT_EXTRA_KEY = "data";

    public static final String SAVED_INSTANCE_STATE_KEY = "saved";

    private static final String TAG = MainActivity.class.getSimpleName();

    private SQLiteDatabase mDb;

    public static final String[] MAIN_MOVIE_PROJECTION = {MovieContract.MovieEntry.COLUMN_MOVIE_POSTER};

    public static final int INDEX_MOVIE_POSTER = 0;

    private static final int ID_MOVIE_LOADER = 44;

    private RecyclerView mMovieRecyclerView;

    private ProgressBar mLoadingIndicator;

    private MovieAdapter mMovieAdapter;

    private int mPosition = RecyclerView.NO_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This method call will load up the user's preferences for sorting
        sort_request = readPref();

        // Linking the recyclerView to the view and layout manager
        mMovieRecyclerView = findViewById(R.id.recyclerview_movies);


        // Checks orientation and displays either two or three posters in port and land respectively
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

        mMovieRecyclerView.setHasFixedSize(true);

        // Creating a new instance of the MovieDbHelper
        MovieDbHelper dbHelper = new MovieDbHelper(this);

        // Getting a writable database
        mDb = dbHelper.getWritableDatabase();

        // Inserting fake data to test the database
        TestUtil.insertFakeData(mDb);

        // Getting all the data stored in the database
        Cursor cursor = getAllMovies();

        // Giving the adapter a new click handler
        mMovieAdapter = new MovieAdapter(this, this);

        // Attaching the adapter to the recyclerView
        mMovieRecyclerView.setAdapter(mMovieAdapter);


        // This little snippet checks to see if we have any saved data from a
        // previous session and loads it into the adapter.
//        if(savedInstanceState != null){
//            mMovieData = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_STATE_KEY);
//            mMovieAdapter.setMovieData(mMovieData);
//        }

        // More linking of views to their variables

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        showLoading();

        getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);

        // Here we have our network connectivity check. If the connectivity is no good
        // then we display an error message.
//        ConnectivityManager cm =
//                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        @SuppressWarnings("ConstantConditions") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork != null &&
//                activeNetwork.isConnectedOrConnecting();
//
//        if(isConnected ){
//            loadMovieData();
//        }
//        else{
//            mLoadingIndicator.setVisibility(View.GONE);
//            showErrorMessage();
//        }


    }


    /**
     * This method will make the View for the movie data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie data is visible */
        mMovieRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the movie
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showLoading() {
        /* First, hide the currently visible data */
        mMovieRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private Cursor getAllMovies() {
        return mDb.query(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MovieContract.MovieEntry._ID
        );
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle loaderArgs) {
        switch (id) {
            case ID_MOVIE_LOADER:
                Uri movieQueryUri = MovieContract.MovieEntry.CONTENT_URI;

                return new CursorLoader(this,
                        movieQueryUri,
                        MAIN_MOVIE_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mMovieAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;

        mMovieRecyclerView.smoothScrollToPosition(mPosition);

        if (data.getCount() != 0) {
            showMovieDataView();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }

    @Override
    public void onClick(int id) {
//        Context context = this;
//        //Class destinationClass = MovieDetailActivity.class;
//        Intent intentToStartMovieDetailActivity = new Intent(context, destinationClass);
//        intentToStartMovieDetailActivity.putExtra(INTENT_EXTRA_KEY, id);
//        startActivity(intentToStartMovieDetailActivity);
    }

    /**
     * Here's our menu creator.
     * This let's us see our sort options
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.sort_by, menu);

        return true;
    }

    /**
     * This method checks to see which item in the menu has been selected.
     * When an option is picked, we save the selection into shared preferences,
     * clear the adapter data, and load up new data based on the preferred
     * sort parameter.
     *
     * @param item
     * @return
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int sortSelect = item.getItemId();

        String sort_select;

        if (sortSelect == R.id.sort_by_highest) {
            sort_select = SORT_PREF_VOTE;
            storePref(sort_select);
            sort_request = SORT_PREF_VOTE;

        }
        if (sortSelect == R.id.sort_by_popularity) {
            sort_select = SORT_PREF_POP;
            storePref(sort_select);
            sort_request = SORT_PREF_POP;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Here we save all state of mMovieData for later viewer needs.
     * We're handling the problem with losing the position of the adapter
     * when coming back to it after rotating.
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelableArrayList(SAVED_INSTANCE_STATE_KEY, mCursor);
    }


    /**
     * When a menu option is clicked we walk over here and save the choice into shared
     * preferences so we can handle a rotation change and still display the user's preference.
     *
     * @param sort_request_store
     */
    public void storePref(String sort_request_store) {

        SharedPreferences.Editor editor = getSharedPreferences(USER_PREF, MODE_PRIVATE).edit();
        editor.putString(SORT_PREF_KEY, sort_request_store);
        editor.apply();

    }

    /**
     * Well if we're saving the data above, then this method is probably a way to read it.
     *
     * @return
     */
    public String readPref() {
        SharedPreferences prefs = getSharedPreferences(USER_PREF, MODE_PRIVATE);
        String pref_request = prefs.getString(SORT_PREF_KEY, null);
        if (pref_request == null) {
            pref_request = prefs.getString(SORT_PREF_KEY, SORT_PREF_POP);
        }

        return pref_request;
    }
}
