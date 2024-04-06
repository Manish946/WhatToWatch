package com.example.whattowatch.src.Helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.whattowatch.src.Domain.User;

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
}
