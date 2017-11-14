package com.example.robot.popularmoviesstage1;

/**
 * Created by Robot on 11/14/2017.
 */

public class Movies {
    private String mTitle;

    private int mReleaseDate;

    private String mPosterUrl;

    private String mPlotSynopsis;

    private int mVoteAverage;

    public Movies(String title, String posterUrl, String plot, int voteAverage, int releaseDate) {
        mTitle = title;
        mPosterUrl = posterUrl;
        mPlotSynopsis = plot;
        mReleaseDate = releaseDate;
        mVoteAverage = voteAverage;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(int mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getmPosterUrl() {
        return mPosterUrl;
    }

    public void setmPosterUrl(String mPosterUrl) {
        this.mPosterUrl = mPosterUrl;
    }

    public String getmPlotSynopsis() {
        return mPlotSynopsis;
    }

    public void setmPlotSynopsis(String mPlotSynopsis) {
        this.mPlotSynopsis = mPlotSynopsis;
    }

    public int getmVoteAverage() {
        return mVoteAverage;
    }

    public void setmVoteAverage(int mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

}
