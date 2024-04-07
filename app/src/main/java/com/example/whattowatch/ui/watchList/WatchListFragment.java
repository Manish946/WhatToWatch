package com.example.whattowatch.ui.watchList;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whattowatch.R;
import com.example.whattowatch.databinding.FragmentWatchlistBinding;
import com.example.whattowatch.src.Adapter.MovieAdapter;
import com.example.whattowatch.src.Adapter.WatchlistAdapter;
import com.example.whattowatch.src.Domain.Movie;
import com.example.whattowatch.src.Domain.User;
import com.example.whattowatch.src.Helper.ContextDb;

import java.util.ArrayList;
import java.util.List;

public class WatchListFragment extends Fragment {

    private WatchlistAdapter adapter;
    private RecyclerView recyclerView;
    private String userId;

    private User currentUser;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = getCurrentUser();
        userId = currentUser.getUserId();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_watchlist, container, false);
        recyclerView = root.findViewById(R.id.recycler_view_watchlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new WatchlistAdapter(getActivity(), new ArrayList<>());
        adapter.setUserId(userId);
        recyclerView.setAdapter(adapter);
        loadWatchlist();
        return root;
    }

    private void loadWatchlist() {
        // Initialize the ContextDb
        ContextDb contextDb = new ContextDb(getActivity());

        // Retrieve the watchlist data directly from the database
        List<Movie> watchlist = contextDb.getWatchlist(userId);

        // Update the adapter with the watchlist data
        adapter.setWatchlist(watchlist);
    }

    private User getCurrentUser() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("CurrentUser", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        String email = sharedPreferences.getString("email", "");
        String fullName = sharedPreferences.getString("fullName", "");
        if (!userId.isEmpty() && !email.isEmpty()) {
            return new User(userId,fullName, email, "");
        } else {
            return null; // Return null if user data is not found
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}