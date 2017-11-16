
package com.example.robot.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.View.OnClickListener;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Robot on 11/13/2017.
 */

@SuppressWarnings("DefaultFileTemplate")
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private ArrayList<Movies> mMovieData;

    /**
    * An on-click handler that we've defined to make it easy for an Activity to interface with
    * our RecyclerView
    */
    private final MovieAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieAdapterOnClickHandler {
        void onClick(Movies selectedMovie);
    }

    /**
     * Creates a MovieAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        public final ImageView mMovieImageView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = view.findViewById(R.id.iv_movie_poster);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movies movieSelected = mMovieData.get(adapterPosition);
            mClickHandler.onClick(movieSelected);

        }
    }

    /**
     * This makes a view feeds it into the adapter.
     * @param viewGroup
     * @param viewType
     * @return
     */
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    /**
     * Here we're attaching all the movie data to the different parts of the view.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Movies movieForThisPosition = mMovieData.get(position);
        String posterUrl = movieForThisPosition.getmPosterUrl();
        String finishedUrl = Movies.imageUrlBuilderLargeSize(posterUrl);
        Context context = holder.mMovieImageView.getContext();
        Picasso.with(context).load(finishedUrl).into(holder.mMovieImageView);

    }


    /**
     * This let's adapter know how many things we're gonna try to display.
     * @return
     */
    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.size();
    }

    /**
     * This method is used to set the movie data on a MovieAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new ForecastAdapter to display it.
     *
     * @param movieData The new weather data to be displayed.
     */
    public void setMovieData(ArrayList<Movies> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

}
