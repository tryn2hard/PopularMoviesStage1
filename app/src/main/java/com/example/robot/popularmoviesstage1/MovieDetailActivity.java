//package com.example.robot.popularmoviesstage1;
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.net.Uri;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.ViewDebug;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.example.robot.popularmoviesstage1.data.MovieContract;
//import com.example.robot.popularmoviesstage1.data.MovieDbHelper;
//import com.squareup.picasso.Picasso;
//
//public class MovieDetailActivity extends AppCompatActivity {
//
//    private static final String TAG = MovieDetailActivity.class.getSimpleName();
//
//    private static SQLiteDatabase mDb;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_movie_detail);
//
//        // Initialize db
//        MovieDbHelper dbHelper = new MovieDbHelper(this);
//
//        mDb = dbHelper.getReadableDatabase();
//
//        // Variable declarations
//        Movies mMovieDetailData;
//        TextView mMovieTitle;
//        ImageView mMoviePoster;
//        TextView mMovieSynopsis;
//        TextView mMovieReleaseDate;
//        TextView mMovieRating;
//
//        // Linking the variables to their respective view parts.
//        mMovieTitle = findViewById(R.id.tv_movie_title);
//        mMovieSynopsis = findViewById(R.id.tv_detail_synopsis);
//        mMovieReleaseDate = findViewById(R.id.tv_detail_release_date);
//        mMovieRating = findViewById(R.id.tv_detail_rating);
//        mMoviePoster = findViewById(R.id.iv_detail_movie_poster);
//
//        /**
//         *   Let's get the intent from Main, and if it has data
//         *   pull it out and attach it to the view parts.
//         **/
//        Intent intentThatStartedThisActivity = getIntent();
//
//        if (intentThatStartedThisActivity != null) {
//
//            if (intentThatStartedThisActivity.hasExtra(MainActivity.INTENT_EXTRA_KEY)) {
//
//                int id = getIntent().getIntExtra(MainActivity.INTENT_EXTRA_KEY, 0);
//
//                String path_id = Integer.toString(id);
//
//                Uri movieUri = MovieContract.MovieEntry.CONTENT_URI.buildUpon()
//                        .appendPath(path_id)
//                        .build();
//
//                Cursor cursor = mDb.query(
//                        MovieContract.MovieEntry.TABLE_NAME,
//                        null,
//                        null,
//                        null,
//                        null,
//                        null,
//                        null);
//
//
////                mMovieDetailData = data.getParcelable(MainActivity.INTENT_EXTRA_KEY);
////
////               mMovieTitle.setText(mMovieDetailData.getmTitle());
////
////                mMovieRating.setText(Double.toString(mMovieDetailData.getmVoteAverage()) + "/10");
////
////                mMovieReleaseDate.setText(mMovieDetailData.getmReleaseDate().substring(0, 4));
////
////                mMovieSynopsis.setText(mMovieDetailData.getmPlotSynopsis());
////
////                String urlForPicasso = Movies.imageUrlBuilder(mMovieDetailData.getmPosterUrl());
////
////                Picasso.with(this).load(urlForPicasso).into(mMoviePoster);
//            }
//        }
//    }
//}
