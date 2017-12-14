package com.example.robot.popularmoviesstage1.utilities;

/**
 * Created by Robot on 12/14/2017.
 */

public class PopularMoviesUtils {

    private static final String LOG_TAG = PopularMoviesUtils.class.getSimpleName();

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE= "w185";

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
