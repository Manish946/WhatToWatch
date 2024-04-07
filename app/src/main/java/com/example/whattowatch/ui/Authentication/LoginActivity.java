package com.example.whattowatch.ui.Authentication;

import android.os.Bundle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whattowatch.MainActivity;
import com.example.whattowatch.R;
import com.example.whattowatch.src.Domain.User;
import com.example.whattowatch.src.Helper.ContextDb;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonRegister;
    private ContextDb contextDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        contextDb = new ContextDb(this);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        User user = contextDb.getUserByEmail(email);
        // Check if email or password is empty
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if(user != null) {
           BCrypt.Result passwordValidResult = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
           if(passwordValidResult.verified) {
               Toast.makeText(this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
               saveUserData(user);
               Intent intent = new Intent(LoginActivity.this, MainActivity.class);
               startActivity(intent);
               finish();
           } else {
               Toast.makeText(this, "Password or Email is incorrect", Toast.LENGTH_SHORT).show();
           }
        } else {
            Toast.makeText(this, "User Not Found, Try Again!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserData(User user) {
        SharedPreferences sharedPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", user.getUserId());
        editor.putString("email", user.getEmail());
        editor.putString("fullName", user.getFullName());
        editor.apply();
    }
}