package com.example.robot.popularmoviesstage1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Robot on 11/14/2017.
 */


public class Movies implements Parcelable{

    // Strings for Picasso
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE= "w185";

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

    private String mReleaseDate;

    private String mPosterUrl;

    private String mPlotSynopsis;

    private double mVoteAverage;

    public Movies (String title, String posterUrl, String plotSynopsis, String releaseDate, double voteAverage){
        mTitle = title;
        mPosterUrl = posterUrl;
        mPlotSynopsis = plotSynopsis;
        mReleaseDate = releaseDate;
        mVoteAverage = voteAverage;
    }

    public String getmTitle() {
        return mTitle;
    }



    public String getmReleaseDate() {
        return mReleaseDate;
    }



    public String getmPosterUrl() {
        return mPosterUrl;
    }



    public String getmPlotSynopsis() {
        return mPlotSynopsis;
    }



    public double getmVoteAverage() {
        return mVoteAverage;
    }



    public Movies(Parcel in) {
        mTitle = in.readString();
        mPosterUrl = in.readString();
        mPlotSynopsis = in.readString();
        mReleaseDate = in.readString();
        mVoteAverage = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        // Write data in any order
        dest.writeString(mTitle);
        dest.writeString(mPosterUrl);
        dest.writeString(mPlotSynopsis);
        dest.writeString(mReleaseDate);
        dest.writeDouble(mVoteAverage);
    }


    /**
     * Method builds an image url for picasso.
     * This one returns a photo used in the
     * Main layout
     * @param imagePath
     * @return
     */
    public static String imageUrlBuilder (String imagePath){
        String finishedImagePath =  IMAGE_BASE_URL +
                IMAGE_SIZE +
                imagePath;

        return finishedImagePath;
    }

}
