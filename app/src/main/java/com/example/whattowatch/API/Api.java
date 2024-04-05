package com.example.whattowatch.API;

import com.example.whattowatch.API.Domain.Movie;
import com.example.whattowatch.API.Domain.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
public interface Api {
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Header("Authorization") String authToken,
            @Query("language") String language,
            @Query("page") int page
    );
}
