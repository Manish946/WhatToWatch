package com.example.whattowatch.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.whattowatch.src.Domain.MovieResponse;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<MovieResponse> movieResponse;

    public HomeViewModel() {
        movieResponse = new MutableLiveData<>();
    }

    public LiveData<MovieResponse> getMovieResponseLiveData() {
        return movieResponse;
    }

    public void setMovieResponse(MovieResponse movieResponse) {
        movieResponse.setMovies(movieResponse.getMovies());
    }
}