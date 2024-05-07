package com.moutamid.musicapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.musicapp.Adapter.FavoriteSongsAdapter;
import com.moutamid.musicapp.Model.DatabaseHelper;
import com.moutamid.musicapp.Model.Song;
import com.moutamid.musicapp.Model.SongsModel;

import java.util.List;

public class FavouriteScreen extends AppCompatActivity {

    private RecyclerView favoriteSongsRecyclerView;
    private FavoriteSongsAdapter adapter;
    TextView no_songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_screen);
        favoriteSongsRecyclerView = findViewById(R.id.recycler_view);
        no_songs = findViewById(R.id.no_songs);
        favoriteSongsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DatabaseHelper databaseHelper = new DatabaseHelper(FavouriteScreen.this);
        List<SongsModel> favoriteSongs = databaseHelper.getAllFavoriteSongs();
        if(favoriteSongs.size()>0)
        {
            no_songs.setVisibility(View.GONE);
        }

        else
        {
            no_songs.setVisibility(View.VISIBLE);

        }
        adapter = new FavoriteSongsAdapter(favoriteSongs, FavouriteScreen.this);
        favoriteSongsRecyclerView.setAdapter(adapter);
    }

    public void onBack(View view) {
        onBackPressed();
    }
}
