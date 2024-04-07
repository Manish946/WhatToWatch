package com.example.whattowatch.ui.profile;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.whattowatch.R;
import com.example.whattowatch.databinding.FragmentProfileBinding;
import com.example.whattowatch.src.Domain.User;
import com.example.whattowatch.ui.Authentication.LoginActivity;
import com.example.whattowatch.ui.Authentication.RegisterActivity;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Bind UI elements
        TextView textViewFullName = binding.textViewFullName;
        TextView textViewEmail = binding.textViewEmail;
        Button buttonLogout = binding.buttonLogout;

        // Get user data from session manager or another source
        User user = getCurrentUser();

        // Update UI with user data
        textViewFullName.setText(user.getFullName());
        textViewEmail.setText(user.getEmail());

        // Set click listener for logout button
        buttonLogout.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
}