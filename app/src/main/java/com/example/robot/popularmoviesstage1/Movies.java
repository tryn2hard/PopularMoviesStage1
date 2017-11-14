package com.example.robot.popularmoviesstage1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Robot on 11/14/2017.
 */

public class Movies implements Parcelable{

    public static final Parcelable.Creator<Movies> CREATOR = new Parcelable.Creator<Movies>(){
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    // member variables
    private String mTitle;

    private int mReleaseDate;

    private String mPosterUrl;

    private String mPlotSynopsis;

    private int mVoteAverage;

    public Movies (String title, String posterUrl, String plotSynopsis, int releaseDate, int voteAverage){
        mTitle = title;
        mPosterUrl = posterUrl;
        mPlotSynopsis = plotSynopsis;
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

    public Movies(Parcel in) {
        mTitle = in.readString();
        mPosterUrl = in.readString();
        mPlotSynopsis = in.readString();
        mReleaseDate = in.readInt();
        mVoteAverage = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        // Write data in any order
        dest.writeString(mTitle);
        dest.writeString(mPlotSynopsis);
        dest.writeString(mPosterUrl);
        dest.writeInt(mReleaseDate);
        dest.writeInt(mVoteAverage);
    }

}
