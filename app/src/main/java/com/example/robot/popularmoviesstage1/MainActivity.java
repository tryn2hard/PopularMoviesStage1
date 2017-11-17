package com.example.robot.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.robot.popularmoviesstage1.utilities.NetworkUtils;
import com.example.robot.popularmoviesstage1.utilities.TMDBJsonUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    // Member Variable declarations
    private String sort_request;

    private static ArrayList<Movies> mMovieData;

    private RecyclerView mMovieRecyclerView;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private MovieAdapter mMovieAdapter;

    private static final String USER_PREF = "pref";

    private static final String SORT_PREF_KEY = "sort";

    public static final String SORT_PREF_VOTE = "top_rated?";

    public static final String SORT_PREF_POP = "popular?";

    public static final String INTENT_EXTRA_KEY = "data";

    public static final String SAVED_INSTANCE_STATE_KEY = "saved";

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This method call will load up the user's preferences for sorting
        sort_request = readPref();

        // Linking the recyclerView to the view and layout manager
        mMovieRecyclerView = findViewById(R.id.recyclerview_movies);


        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        else{
            mMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

        mMovieRecyclerView.setHasFixedSize(true);

        // Giving the adapter a new click handler
        mMovieAdapter = new MovieAdapter(this);

        // Attaching the adapter to the recyclerView
        mMovieRecyclerView.setAdapter(mMovieAdapter);

        // This little snippet checks to see if we have any saved data from a
        // previous session and loads it into the adapter.
        if(savedInstanceState != null){
            mMovieData = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_STATE_KEY);
            mMovieAdapter.setMovieData(mMovieData);
        }

        // More linking of views to their variables
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        // Here we have our network connectivity check. If the connectivity is no good
        // then we display an error message.
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        @SuppressWarnings("ConstantConditions") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected ){
            loadMovieData();
        }
        else{
            mLoadingIndicator.setVisibility(View.GONE);
            showErrorMessage();
        }


    }

    // First turns on the right views to display the data, and then calls AsyncTask to do it's thing
    private void loadMovieData(){

        showMovieDataView();
        new FetchMovieTask().execute();
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
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
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
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mMovieRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /**
     * This block of code takes care of all the network stuff that needs to be done.
     * Also, passes the data from the network to the adapter for loading.
     */
    public class FetchMovieTask extends AsyncTask<Void, Void, ArrayList<Movies>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movies> doInBackground(Void...voids) {

            URL movieRequestUrl = NetworkUtils.buildUrl(sort_request);

            try{
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

                mMovieData = TMDBJsonUtils.getMovieFromJson(jsonMovieResponse);

                return mMovieData;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movies> movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if(movies != null){
                showMovieDataView();
                mMovieAdapter.setMovieData(mMovieData);
            } else{
                showErrorMessage();
            }
        }
    }

    /**
     * Here's our menu creator.
     * This let's us see our sort options
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
     * @param item
     * @return
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int sortSelect = item.getItemId();

        String sort_select;

        if (sortSelect == R.id.sort_by_highest){
           sort_select = SORT_PREF_VOTE;
           storePref(sort_select);
           sort_request = SORT_PREF_VOTE;
           mMovieAdapter.setMovieData(null);
           loadMovieData();
        }
        if (sortSelect == R.id.sort_by_popularity){
            sort_select = SORT_PREF_POP;
            storePref(sort_select);
            sort_request = SORT_PREF_POP;
            mMovieAdapter.setMovieData(null);
            loadMovieData();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Here we handle the click on a movie poster. It starts another activity.
     * Also, we make sure to put in a little extra for our next activity to work
     * with. It's basically the movie object, but as a 'parcelable.
     * @param selectedMovie
     */
    @Override
    public void onClick(Movies selectedMovie) {
        Context context = this;
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartMovieDetailActivity = new Intent(context, destinationClass);
        intentToStartMovieDetailActivity.putExtra(INTENT_EXTRA_KEY, selectedMovie);
        startActivity(intentToStartMovieDetailActivity);
    }

    /**
     * When a menu option is clicked we walk over here and save the choice into shared
     * preferences so we can handle a rotation change and still display the user's preference.
     * @param sort_request_store
     */
    public void storePref (String sort_request_store){

        SharedPreferences.Editor editor = getSharedPreferences(USER_PREF, MODE_PRIVATE).edit();
        editor.putString(SORT_PREF_KEY, sort_request_store);
        editor.apply();

    }

    /**
     * Well if we're saving the data above, then this method is probably a way to read it.
     * @return
     */
    public String readPref () {
        SharedPreferences prefs = getSharedPreferences(USER_PREF, MODE_PRIVATE);
        String pref_request = prefs.getString(SORT_PREF_KEY, null);
        if (pref_request == null) {
            pref_request = prefs.getString(SORT_PREF_KEY, SORT_PREF_POP);
        }

        return pref_request;
    }

    /** Here we save all state of mMovieData for later viewer needs.
     * We're handling the problem with losing the position of the adapter
     * when coming back to it after rotating.
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_INSTANCE_STATE_KEY, mMovieData);
    }
}
