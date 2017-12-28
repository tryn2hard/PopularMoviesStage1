package com.example.robot.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robot on 12/26/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder>{

    private final Context mContext;
    private ArrayList<String> trailers;

    private final TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void onClick(String trailerId);
    }



    public TrailerAdapter(
            Context context,
            ArrayList<String> trailers,
            TrailerAdapterOnClickHandler clickhandler){
        mContext = context;
        this.trailers = trailers;
        mClickHandler = clickhandler;
    }

    @Override
    public TrailerAdapter.TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutForListItem =  R.layout.movie_detail_trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmeditately = false;
        View view = inflater.inflate(layoutForListItem, parent, shouldAttachToParentImmeditately);
        view.setFocusable(true);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerAdapterViewHolder holder, int position) {
        holder.mTrailerNumber.setText(Integer.toString(position + 1));
    }

    @Override
    public int getItemCount() {
        if(trailers == null) return 0;
        return trailers.size();
    }


    class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       final ImageView mTrailerPlayView;
       final TextView mTrailerNumber;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            mTrailerPlayView = itemView.findViewById(R.id.iv_list_trailer);
            mTrailerNumber = itemView.findViewById(R.id.trailer_number);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterposition = getAdapterPosition();
            String trailerId = trailers.get(adapterposition);
            mClickHandler.onClick(trailerId);
        }
    }
}
