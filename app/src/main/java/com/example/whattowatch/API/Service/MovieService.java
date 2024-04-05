package com.example.whattowatch.API.Service;
import com.example.whattowatch.API.Api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class MovieService {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2ZjQ2NjgwZmI1NDhkMjg2ZWJlYTQyMjk4OGFiZjRkMyIsInN1YiI6IjY2MGU1ZDhiOTVjZTI0MDE3ZDZmZjY1NSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.7K8rHXld10rYjIBwiKo8pfPg6L4cI4JkdebTU839ptI";

    private  static Api apiService;

    public static Api getApiService() {
        if (apiService == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(Api.class);
        }
        return apiService;
    }
}
