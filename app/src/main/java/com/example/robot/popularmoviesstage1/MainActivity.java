package com.example.robot.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robot.popularmoviesstage1.MovieAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static ArrayList<Movies> mFakeData;

    private RecyclerView mMovieRecyclerView;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private MovieAdapter mMovieAdapter;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieRecyclerView = findViewById(R.id.recyclerview_movies);
        mMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mMovieRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);

        mMovieRecyclerView.setAdapter(mMovieAdapter);

        loadFakeMovieData();

        mMovieAdapter.setMovieData(mFakeData);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);


    }

    private void loadFakeMovieData(){

        mFakeData = new ArrayList<Movies>();

        mFakeData.add(new Movies(
                "Thor: Ragnarok",
                "Nothing",
                "Thor is imprisoned on the other side of the universe and finds himself in a race against time to get back to Asgard to stop Ragnarok, " +
                        "the destruction of his homeworld and the end of Asgardian civilization, at the hands of an all-powerful new threat, the ruthless Hela.\n" +
                        "Featured Crew",
                77,
                2017));

        mFakeData.add(new Movies(
                "Minions",
                "Nothing",
                "Minions Stuart, Kevin and Bob are recruited by Scarlet Overkill, a super-villain who, alongside her inventor husband Herb," +
                        " hatches a plot to take over the world.",
                64,
                2015));
        mFakeData.add(new Movies(
                "Thor: Ragnarok",
                "Nothing",
                "Thor is imprisoned on the other side of the universe and finds himself in a race against time to get back to Asgard to stop Ragnarok, " +
                        "the destruction of his homeworld and the end of Asgardian civilization, at the hands of an all-powerful new threat, the ruthless Hela.\n" +
                        "Featured Crew",
                77,
                2017));

        mFakeData.add(new Movies(
                "Minions",
                "Nothing",
                "Minions Stuart, Kevin and Bob are recruited by Scarlet Overkill, a super-villain who, alongside her inventor husband Herb," +
                        " hatches a plot to take over the world.",
                64,
                2015));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.sort_by, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int sortSelect = item.getItemId();

        if (sortSelect == R.id.sort_by_highest){
            Toast.makeText(this, "Highest Selected", Toast.LENGTH_LONG).show();
        }
        if (sortSelect == R.id.sort_by_popularity){
            Toast.makeText(this, "Popularity Selected", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Movies selectedMovie) {
        Context context = this;
        Class destinationClass = MovieDetail.class;
        Intent intentToStartMovieDetailActivity = new Intent(context, destinationClass);
        intentToStartMovieDetailActivity.putExtra("Test", selectedMovie);
        startActivity(intentToStartMovieDetailActivity);
    }
}
