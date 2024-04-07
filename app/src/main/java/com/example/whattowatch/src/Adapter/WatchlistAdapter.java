package com.example.whattowatch.src.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.whattowatch.R;
import com.example.whattowatch.src.Domain.Movie;
import com.example.whattowatch.src.Helper.ContextDb;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.WatchlistViewHolder> {
    private Context context;
    private static String userId;
    private List<Movie> watchlist;
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public WatchlistAdapter(Context context, List<Movie> watchlist) {
        this.context = context;
        this.watchlist = watchlist;
    }


    public void setWatchlist(List<Movie> watchlist) {
        this.watchlist = watchlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WatchlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.watchlist_item, parent, false);
        return new WatchlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchlistViewHolder holder, int position) {
        Movie movie = watchlist.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return watchlist.size();
    }

    public class WatchlistViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        ImageView imageView;
        Button removeButton;

        public WatchlistViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.watchlist_item_title);
            removeButton = itemView.findViewById(R.id.watchlist_item_remove_button);
            imageView = itemView.findViewById(R.id.watchlist_item_image);
            descriptionTextView = itemView.findViewById(R.id.watchlist_item_description);
            removeButton.setBackgroundColor(itemView.getResources().getColor(android.R.color.holo_red_dark));
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Movie clickedMovie = watchlist.get(position);
                    if (position != RecyclerView.NO_POSITION) {
                        ContextDb db = new ContextDb(itemView.getContext());
                        db.removeFromWatchlist(userId,clickedMovie.getId());
                        // Remove the item from the list and notify adapter
                        watchlist.remove(position);
                        notifyItemRemoved(position);
                    }
                }
            });
        }

        public void bind(Movie movie) {
            titleTextView.setText(movie.getTitle());
            descriptionTextView.setText(movie.getOverview());
            Picasso.get().load("https://image.tmdb.org/t/p/w600_and_h900_bestv2"+ movie.getPosterPath()).into(imageView);
        }
    }
}

