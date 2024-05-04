package com.moutamid.musicapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.moutamid.musicapp.Model.DatabaseHelper;
import com.moutamid.musicapp.Model.Song;
import com.moutamid.musicapp.Model.SongsModel;
import com.moutamid.musicapp.MusicPlayerActivity;
import com.moutamid.musicapp.R;

import java.io.Serializable;
import java.util.List;

public class FavoriteSongsAdapter extends RecyclerView.Adapter<FavoriteSongsAdapter.FavoriteSongViewHolder> {

    private List<SongsModel> favoriteSongs;
    Context context;
    InterstitialAd interstitialAdFB;
    boolean fb1 = false;

    public FavoriteSongsAdapter(List<SongsModel> favoriteSongs, Context context) {
        this.favoriteSongs = favoriteSongs;
        this.context = context;
    }


    public FavoriteSongsAdapter(List<SongsModel> favoriteSongs) {
        this.favoriteSongs = favoriteSongs;
    }

    @NonNull
    @Override
    public FavoriteSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_song, parent, false);
        return new FavoriteSongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteSongViewHolder holder, int position) {
        SongsModel song = favoriteSongs.get(position);
        holder.bind(song);
        loadFbInterstitialAd();
        holder.song_fav_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "testt", Toast.LENGTH_SHORT).show();
                showFBInterstitial();
                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                databaseHelper.deleteSongFromFavorites(song.getName());
                notifyItemChanged(position);
                favoriteSongs.remove(position);
            }
        });
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, MusicPlayerActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("songList", (Serializable) favoriteSongs);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return favoriteSongs.size();
    }

    static class FavoriteSongViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView artistTextView;
        ImageView song_fav_view;

        public FavoriteSongViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.song_name_text_view);
            artistTextView = itemView.findViewById(R.id.song_details_text_view);
            song_fav_view = itemView.findViewById(R.id.song_fav_view);
        }

        public void bind(SongsModel song) {
            nameTextView.setText(song.getName());
            artistTextView.setText(song.getDetails());
        }
    }

    private void loadFbInterstitialAd() {
        interstitialAdFB = new InterstitialAd(context, "IMG_16_9_APP_INSTALL#2722927698006061_2722934531338711");
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            private Ad ad;
            private AdError adError;

            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                fb1 = false;
                interstitialAdFB.loadAd();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                StringBuilder sb = new StringBuilder();
                sb.append("Interstitial ad failed to load: ");
                sb.append(adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

        };
        interstitialAdFB.loadAd(
                interstitialAdFB.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build()
        );

    }

    public void showFBInterstitial() {
        InterstitialAd interstitialAd = this.interstitialAdFB;
        if (interstitialAd != null && interstitialAd.isAdLoaded()) {
            this.interstitialAdFB.show();
        }

    }
}
