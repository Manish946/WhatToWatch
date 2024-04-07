package com.example.whattowatch.src.Helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.whattowatch.src.Domain.Movie;
import com.example.whattowatch.src.Domain.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ContextDb extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "WhatToWatch.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_USER = "CREATE TABLE User (" +
            "UserId Text PRIMARY KEY," +
            "FullName TEXT," +
            "Email TEXT," +
            "HashedPassword TEXT)";

    private static final String CREATE_TABLE_WATCHLIST = "CREATE TABLE WatchList (" +
            "WatchListId INTEGER PRIMARY KEY AUTOINCREMENT," +
            "UserId INTEGER," +
            "Movie TEXT," +
            "FOREIGN KEY(UserId) REFERENCES User(UserId))";

    public ContextDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
        sqLiteDatabase.execSQL(CREATE_TABLE_WATCHLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS User");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS WatchList");
        onCreate(sqLiteDatabase);
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase dbContext = this.getReadableDatabase();
        User user = null;

        String query = "SELECT * FROM User Where Email = ?";

        Cursor cursor = dbContext.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst()) {
            // Retrieve data from the cursor
            int userIdIndex = cursor.getColumnIndex("UserId");
            int fullNameIndex = cursor.getColumnIndex("FullName");
            int hashedPasswordIndex = cursor.getColumnIndex("HashedPassword");

            // Check if columns exist in the cursor
            if (userIdIndex != -1 && fullNameIndex != -1 && hashedPasswordIndex != -1) {
                String userId = cursor.getString(userIdIndex);
                String fullName = cursor.getString(fullNameIndex);
                String hashedPassword = cursor.getString(hashedPasswordIndex);
                // Create a User object
                user = new User(userId, fullName, email, hashedPassword);
            }
        }
        // Close cursor and database
        cursor.close();
        dbContext.close();
        return user;
    }
    public void addMovieToWatchList(String userId, Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Convert relevant Movie object fields to JSON
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", movie.getId());
            jsonObject.put("title", movie.getTitle());
            jsonObject.put("releaseDate", movie.getReleaseDate());
            jsonObject.put("overview", movie.getOverview());
            jsonObject.put("posterPath", movie.getPosterPath());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Insert movie data into WatchList table
        ContentValues values = new ContentValues();
        values.put("UserId", userId);
        values.put("Movie", jsonObject.toString());
        db.insert("WatchList", null, values);

        db.close();
    }

    public void removeFromWatchlist(String userId, int movieId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Define the WHERE clause to delete the specific movie for the user
        String whereClause = "UserId = ? AND Movie LIKE ?";
        String[] whereArgs = {userId, "%" + movieId + "%"};

        // Perform the delete operation
        db.delete("WatchList", whereClause, whereArgs);

        db.close();
    }

    // Method to retrieve watchlist for a user
    public List<Movie> getWatchlist(String userId) {
        List<Movie> watchlist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define query to get watchlist for the user
        String query = "SELECT Movie FROM WatchList WHERE UserId = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{userId});

        // Iterate through the cursor and parse each movie data JSON
        if (cursor.moveToFirst()) {
            do {
                try {
                    @SuppressLint("Range") String movieDataJsonString = cursor.getString(cursor.getColumnIndex("Movie"));
                    JSONObject movieData = new JSONObject(movieDataJsonString);
                    String id = movieData.getString("id");
                    String title = movieData.getString("title");
                    String releaseDate = movieData.getString("releaseDate");
                    String overview = movieData.getString("overview");
                    String posterPath = movieData.getString("posterPath");
                    // Create a new Movie object with the retrieved data
                    Movie movie = new Movie();
                    movie.setTitle(title);
                    movie.setReleaseDate(releaseDate);
                    movie.setOverview(overview);
                    movie.setPosterPath(posterPath);
                    movie.setId(Integer.parseInt(id));
                    // Add the Movie object to the watchlist
                    watchlist.add(movie);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        // Close cursor and database
        cursor.close();
        db.close();

        return watchlist;
    }

}
