package com.example.robot.popularmoviesstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MovieDetail extends AppCompatActivity {

    private static final String TAG = MovieDetail.class.getSimpleName();

    private TextView mTextView;
    private Movies mMovieDetailData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);



        mTextView = findViewById(R.id.tv_movie_title);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            Log.d(TAG, "falling through the first if statement");
            if (intentThatStartedThisActivity.hasExtra("Test")) {
                 Bundle data = getIntent().getExtras();
                 mMovieDetailData = data.getParcelable("Test");
                 Log.d(TAG, mMovieDetailData.getmTitle());
                 mTextView.setText(mMovieDetailData.getmTitle() + "\n"
                                    + mMovieDetailData.getmPlotSynopsis() + "\n"
                                    + mMovieDetailData.getmPosterUrl() + "\n"
                                    + mMovieDetailData.getmReleaseDate() + "\n"
                                    + mMovieDetailData.getmVoteAverage());
            }
        }
    }
}
