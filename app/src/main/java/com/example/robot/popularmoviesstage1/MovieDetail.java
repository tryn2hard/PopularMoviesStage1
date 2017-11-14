package com.example.robot.popularmoviesstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MovieDetail extends AppCompatActivity {

    private static final String TAG = MovieDetail.class.getSimpleName();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Movies mMovieDetailData;
        TextView mMovieTitle;
        TextView mMoviePosterUrl;
        TextView mMovieSynopsis;
        TextView mMovieReleaseDate;
        TextView mMovieRating;


        mMovieTitle = findViewById(R.id.tv_movie_title);

        mMovieSynopsis = findViewById(R.id.tv_detail_synopsis);

        mMovieReleaseDate = findViewById(R.id.tv_detail_release_date);

        mMovieRating = findViewById(R.id.tv_detail_rating);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            Log.d(TAG, "falling through the first if statement");
            if (intentThatStartedThisActivity.hasExtra("Test")) {
                 Bundle data = getIntent().getExtras();
                 mMovieDetailData = data.getParcelable("Test");

                 mMovieTitle.setText(mMovieDetailData.getmTitle());

                 mMovieRating.setText(Integer.toString(mMovieDetailData.getmVoteAverage()));

                 mMovieReleaseDate.setText(mMovieDetailData.getmReleaseDate());

                 mMovieSynopsis.setText(mMovieDetailData.getmPlotSynopsis());
            }
        }
    }
}
