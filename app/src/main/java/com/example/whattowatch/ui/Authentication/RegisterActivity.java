package com.example.whattowatch.ui.Authentication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.whattowatch.R;
import com.example.whattowatch.src.Helper.ContextDb;

import java.util.UUID;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextFullname, editTextPassword, editTextEmail;
    private Button buttonRegister, buttonLogin;
    private ContextDb contextDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        contextDb = new ContextDb(this);

        editTextFullname = findViewById(R.id.editTextFullName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    private void registerUser() {
        String fullName = editTextFullname.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Check if email or password is empty
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        // Hash Password
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        // Add user to database
        SQLiteDatabase db = contextDb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserId", generateGuid());
        values.put("FullName", fullName);
        values.put("Email", email);
        values.put("HashedPassword", hashedPassword);

        long newRowId = db.insert("User", null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
            finish(); // Close registration activity after successful registration
        } else {
            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    public static String generateGuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}