package com.example.whattowatch.src.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
            "MovieId INTEGER," +
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
}
