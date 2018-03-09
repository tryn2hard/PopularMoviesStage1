package com.example.robot.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.robot.popularmoviesstage1.data.MovieContract;
import com.example.robot.popularmoviesstage1.data.MovieDbHelper;
import com.example.robot.popularmoviesstage1.sync.MovieSyncUtils;
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

    public static final String SORT_PREF_FAV = "favorites";

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String[] MAIN_MOVIE_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry._ID};

    private static final int ID_MOVIE_LOADER = 44;

    private RecyclerView mMovieRecyclerView;

    private ProgressBar mLoadingIndicator;

    private MovieAdapter mMovieAdapter;

    private static RecyclerView.LayoutManager mLayoutManager;

    private final String KEY_RECYCLER_STATE = "recycler_state";

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
            mLayoutManager = new GridLayoutManager(this, 2);
            mMovieRecyclerView.setLayoutManager(mLayoutManager);
        } else {
            mLayoutManager = new GridLayoutManager(this, 3);
            mMovieRecyclerView.setLayoutManager(mLayoutManager);
        }

        mMovieRecyclerView.setHasFixedSize(true);

        // Giving the adapter a new click handler
        mMovieAdapter = new MovieAdapter(this, this);

        // Attaching the adapter to the recyclerView
        mMovieRecyclerView.setAdapter(mMovieAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int id = (int) viewHolder.itemView.getTag();

                String stringId = Integer.toString(id);
                Uri uri = MovieContract.MovieEntry.CONTENT_URI_FAVORITES;
                uri = uri.buildUpon().appendPath(stringId).build();

                getContentResolver().delete(uri, null, null);
                getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER, null, MainActivity.this);
            }
        }).attachToRecyclerView(mMovieRecyclerView);


        // More linking of views to their variables
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        // Show the progress bar
        showLoading();

        if(savedInstanceState != null){
            Parcelable mListState = savedInstanceState.getParcelable(KEY_RECYCLER_STATE);
            Log.d("onCreate", "State has been restored");
            mLayoutManager.onRestoreInstanceState(mListState);
        }

        // if the sort request is not equal to favorites launch the service
        if (!sort_request.equals(SORT_PREF_FAV) && savedInstanceState == null) {

            ConnectivityManager cm =
                    (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

            @SuppressWarnings("ConstantConditions") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if(isConnected) {
                Log.d("Sync", "Movies have been sync'd");
                MovieSyncUtils.startImmediateSync(this, sort_request);
            }
        }

        // Start the cursorLoader
        getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);

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


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle loaderArgs) {
        Uri contentUri;
        switch (id) {
            case ID_MOVIE_LOADER:

                //Which table do I want to query from? Movies or Favorites
                if (sort_request.equals(SORT_PREF_FAV)) {
                    contentUri = MovieContract.MovieEntry.CONTENT_URI_FAVORITES;
                } else {
                    contentUri = MovieContract.MovieEntry.CONTENT_URI;
                }
                return new CursorLoader(this,
                        contentUri,
                        MAIN_MOVIE_PROJECTION,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID,
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
        if (data.getCount() != 0) showMovieDataView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }

    /**
     * On click MovieDetailActivity will start. It will be supplied with the proper uri
     * to query the correct database. The uri will be built using the id passed in.
     *
     * @param id
     */
    @Override
    public void onClick (int id) {
        Uri passingUri;
        Context context = this;
        Log.d("debug Main", "movie id " + id);
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartMovieDetailActivity = new Intent(context, destinationClass);
        if (sort_request.equals(SORT_PREF_FAV)) {
            passingUri = MovieContract.MovieEntry.buildFavMovieUriWithId(id);
        } else {
            passingUri = MovieContract.MovieEntry.buildMovieUriWithId(id);
        }
        intentToStartMovieDetailActivity.setData(passingUri);
        startActivity(intentToStartMovieDetailActivity);
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
            MovieSyncUtils.startImmediateSync(this, sort_request);
            getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER, null, this);

        }
        if (sortSelect == R.id.sort_by_popularity) {
            sort_select = SORT_PREF_POP;
            storePref(sort_select);
            sort_request = SORT_PREF_POP;
            MovieSyncUtils.startImmediateSync(this, sort_request);
            getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER, null, this);
        }
        if (sortSelect == R.id.sort_by_favorites) {
            sort_select = SORT_PREF_FAV;
            storePref(sort_select);
            sort_request = SORT_PREF_FAV;
            getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER, null, this);
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
        Log.d("onSaveInstanceState", "State has been saved");
        outState.putParcelable(KEY_RECYCLER_STATE, mLayoutManager.onSaveInstanceState());
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
        String pref_request = prefs.getString(SORT_PREF_KEY, SORT_PREF_POP);
        if (pref_request == null) {
            pref_request = prefs.getString(SORT_PREF_KEY, SORT_PREF_POP);
        }

        return pref_request;
    }
}
