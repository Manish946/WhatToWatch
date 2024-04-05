package com.example.whattowatch.src;

import com.example.whattowatch.src.Domain.MovieResponse;

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
