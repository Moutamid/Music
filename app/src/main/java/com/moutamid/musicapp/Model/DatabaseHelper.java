package com.moutamid.musicapp.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

// DatabaseHelper.java
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "music_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_FAVORITES = "favorites";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_ARTIST = "artist";
    private static final String COLUMN_AUDIO = "audio";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create favorites table
        String createFavoritesTable = "CREATE TABLE " + TABLE_FAVORITES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT," +
                COLUMN_ARTIST + " TEXT," +
                COLUMN_AUDIO + " TEXT)";
        db.execSQL(createFavoritesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        // Create tables again
        onCreate(db);
    }

    // Add song to favorites
    public void addSongToFavorites(SongsModel song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, song.getName());
        values.put(COLUMN_ARTIST, song.getDetails());
        values.put(COLUMN_AUDIO, song.getUrl());
        db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }

    // Retrieve all favorite songs
    public List<SongsModel> getAllFavoriteSongs() {
        List<SongsModel> favoriteSongs = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_FAVORITES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SongsModel song = new SongsModel();
                song.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                song.setDetails(cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST)));
                song.setUrl(cursor.getString(cursor.getColumnIndex(COLUMN_AUDIO)));
                // Adding song to list
                favoriteSongs.add(song);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return songs list
        return favoriteSongs;
    }

    // Delete song from favorites
    public void deleteSongFromFavorites(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, COLUMN_NAME + " = ?", new String[]{name});
        db.close();
    }

    // Check if a song exists in favorites
    public boolean isSongFavorite(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITES, new String[]{COLUMN_NAME},
                COLUMN_NAME + " = ?", new String[]{name}, null, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }
}
