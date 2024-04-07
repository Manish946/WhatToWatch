package com.example.whattowatch.ui.watchList;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.whattowatch.src.Domain.Movie;
import com.example.whattowatch.src.Helper.ContextDb;

import java.util.List;

public class WatchListViewModel extends ViewModel {
    private ContextDb contextDb;
    private MutableLiveData<List<Movie>> watchlistLiveData = new MutableLiveData<>();
    private String userId;

    public WatchListViewModel(Context context) {
        contextDb = new ContextDb(context);
        loadWatchlist();
    }
    private void loadWatchlist() {
        List<Movie> watchlist = contextDb.getWatchlist(userId);
        watchlistLiveData.setValue(watchlist);
    }

    public LiveData<List<Movie>> getWatchlistLiveData() {
        return watchlistLiveData;
    }
}