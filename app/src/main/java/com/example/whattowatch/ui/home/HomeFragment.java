package com.example.whattowatch.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.whattowatch.src.Adapter.MovieAdapter;
import com.example.whattowatch.src.Api;
import com.example.whattowatch.src.Domain.MovieResponse;
import com.example.whattowatch.src.Service.MovieService;
import com.example.whattowatch.databinding.FragmentHomeBinding;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private MovieAdapter movieAdapter;
    private int currentPage = 1;
    private boolean isLoading = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupRecyclerView();
        fetchMovies(currentPage);

        return root;
    }

    private void setupRecyclerView() {
        movieAdapter = new MovieAdapter(getContext(), new ArrayList<>());
        binding.recyclerViewMovies.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewMovies.setAdapter(movieAdapter);

        movieAdapter.setOnLoadMoreListener(() -> {
            if (!isLoading) {
                fetchMovies(++currentPage);
            }
        });
    }

    private void fetchMovies(int page) {
        isLoading = true;
        Api movieService = MovieService.getApiService();
        Call<MovieResponse> call = movieService.getPopularMovies(MovieService.AUTH_TOKEN, "en-US", page);
        call.enqueue(new Callback<MovieResponse>() {
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movies = response.body();
                    if (movies != null && movies.getMovies() != null && !movies.getMovies().isEmpty()) {
                        movieAdapter.addMovies(movies.getMovies());
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to fetch movies", Toast.LENGTH_SHORT).show();
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                isLoading = false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}