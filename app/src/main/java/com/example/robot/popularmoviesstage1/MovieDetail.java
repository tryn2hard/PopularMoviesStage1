package com.example.robot.popularmoviesstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MovieDetail extends AppCompatActivity {

    private static final String TAG = MovieDetail.class.getSimpleName();

    private TextView mTextView;
    private String mMovieTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);



        mTextView = findViewById(R.id.tv_movie_title);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mMovieTitle = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                Log.d(TAG, mMovieTitle);
                mTextView.setText(mMovieTitle);
            }
        }
    }
}
