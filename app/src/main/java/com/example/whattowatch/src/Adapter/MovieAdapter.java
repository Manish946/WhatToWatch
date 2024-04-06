package com.example.whattowatch.src.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whattowatch.src.Domain.Movie;
import com.example.whattowatch.R;
import com.example.whattowatch.src.Domain.User;
import com.example.whattowatch.src.Helper.ContextDb;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private static String userId;
    private List<Integer> watchlistMovieIds;
    private ContextDb contextDb;
    public void setUserId(String userId) {
        this.userId = userId;
    }
    private Context context;
    private static List<Movie> movies;
    private OnLoadMoreListener onLoadMoreListener;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
        this.watchlistMovieIds = new ArrayList<>();
        this.contextDb = new ContextDb(context);
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged(); // Notify adapter of data change
    }

    public void addMovies(List<Movie> newMovies) {
        movies.addAll(newMovies);
        updateWatchlistMovieIds();
        notifyDataSetChanged();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        boolean isInWatchlist = isInWatchlist(movie.getId());
        holder.bind(movie, isInWatchlist);
        if (position == movies.size() - 1 && onLoadMoreListener != null) {
            onLoadMoreListener.onLoadMore();
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView releaseDateTextView;
        TextView overviewTextView;
        Button watchListButton;
        boolean currentWatchListStatus;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_movie);
            titleTextView = itemView.findViewById(R.id.text_title);
            overviewTextView = itemView.findViewById(R.id.text_overview);
            releaseDateTextView = itemView.findViewById(R.id.text_releaseDate);
            watchListButton = itemView.findViewById(R.id.buttonAddToWatchList);

        }

        public void bind(Movie movie, boolean isInWatchlist) {
            currentWatchListStatus = isInWatchlist;
            titleTextView.setText(movie.getTitle());
            releaseDateTextView.setText(movie.getReleaseDate());
            overviewTextView.setText(movie.getOverview());
            Picasso.get().load("https://image.tmdb.org/t/p/w600_and_h900_bestv2"+ movie.getPosterPath()).into(imageView);

            if (isInWatchlist) {
                watchListButton.setText("Remove Watchlist");
                watchListButton.setBackgroundColor(itemView.getResources().getColor(android.R.color.holo_red_dark));
            } else {
                watchListButton.setText("Add to Watchlist");
                watchListButton.setBackgroundColor(itemView.getResources().getColor(android.R.color.holo_purple));

            }
            watchListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition(); // Get the position of the item clicked
                    if (position != RecyclerView.NO_POSITION) {
                        Movie clickedMovie = movies.get(position);
                        ContextDb contextDb = new ContextDb(itemView.getContext());
                        if (currentWatchListStatus) {
                            contextDb.removeFromWatchlist(userId, clickedMovie.getId());
                            watchListButton.setText("Add to Watchlist");
                            watchListButton.setBackgroundColor(itemView.getResources().getColor(android.R.color.holo_purple));
                            currentWatchListStatus = false;
                        } else {
                            // If the movie is not in the watchlist, add it
                            contextDb.addMovieToWatchList(userId,clickedMovie);
                            watchListButton.setText("Remove Watchlist");
                            watchListButton.setBackgroundColor(itemView.getResources().getColor(android.R.color.holo_red_dark));
                            currentWatchListStatus = true;
                        }
                    }
                }
            });
        }
    }
    private void updateWatchlistMovieIds() {
        if (!userId.isEmpty()) {
            List<Movie> watchlist = contextDb.getWatchlist(userId);
            watchlistMovieIds.clear();
            for (Movie movie : watchlist) {
                watchlistMovieIds.add(movie.getId());
            }
        }
    }
    // Method to check if a movie is in the user's watchlist
    public boolean isInWatchlist(int movieId) {
        return watchlistMovieIds.contains(movieId);
    }
}

