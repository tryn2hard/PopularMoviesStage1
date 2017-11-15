package com.example.robot.popularmoviesstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {

    private static final String TAG = MovieDetail.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Movies mMovieDetailData;
        TextView mMovieTitle;
        ImageView mMoviePoster;
        TextView mMovieSynopsis;
        TextView mMovieReleaseDate;
        TextView mMovieRating;


        mMovieTitle = findViewById(R.id.tv_movie_title);

        mMovieSynopsis = findViewById(R.id.tv_detail_synopsis);

        mMovieReleaseDate = findViewById(R.id.tv_detail_release_date);

        mMovieRating = findViewById(R.id.tv_detail_rating);

        mMoviePoster = findViewById(R.id.iv_detail_movie_poster);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            Log.d(TAG, "falling through the first if statement");
            if (intentThatStartedThisActivity.hasExtra("Test")) {
                 Bundle data = getIntent().getExtras();
                 mMovieDetailData = data.getParcelable("Test");

                 mMovieTitle.setText(mMovieDetailData.getmTitle());

                 mMovieRating.setText(Double.toString(mMovieDetailData.getmVoteAverage()) + "/10");

                 mMovieReleaseDate.setText(mMovieDetailData.getmReleaseDate());

                 mMovieSynopsis.setText(mMovieDetailData.getmPlotSynopsis());

                 String urlForPicasso = Movies.imageUrlBuilderMediumSize(mMovieDetailData.getmPosterUrl());

                 Log.d("Image test", urlForPicasso);

                Picasso.with(this).load(urlForPicasso).into(mMoviePoster);
            }
        }
    }

}
