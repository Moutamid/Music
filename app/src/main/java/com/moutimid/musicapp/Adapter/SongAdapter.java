package com.moutimid.musicapp.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicnewapp.R;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.moutimid.musicapp.Model.DatabaseHelper;
import com.moutimid.musicapp.Model.Song;
import com.moutimid.musicapp.MusicPlayerActivity;

import java.io.Serializable;
import java.util.List;


    public class SongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int AD_TYPE = 1;
        private static final int CONTENT_TYPE = 2;
        private Context context;
    private List<Song> songList;
    InterstitialAd mInterstitialAd;
    Activity activity;


    public SongAdapter(Activity activity, Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == AD_TYPE) {
            adViewHolder madViewHolder = new adViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item_ad, parent, false));
            return madViewHolder;
        }
            ViewHolder mYourViewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item, parent, false));
            return mYourViewHolder;

    }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


            if (getItemViewType(position) == CONTENT_TYPE) {
                Song song = songList.get(position);
                ((ViewHolder) holder).songNameTextView.setText(song.getName());
                ((ViewHolder) holder).song_details_text_view.setText(song.getDescription());
                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(context, context.getString(R.string.admob_interstitial_ad), adRequest,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                mInterstitialAd = interstitialAd;
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                mInterstitialAd = null;
                            }
                        });
                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                boolean songFavorite = databaseHelper.isSongFavorite(song.getName());
                Log.d("data", songFavorite + "  " + song.getName());
                if (songFavorite) {
                    ((ViewHolder) holder).song_fav_view.setVisibility(View.VISIBLE);
                    ((ViewHolder) holder).song_unfav_view.setVisibility(View.GONE);
                } else {
                    ((ViewHolder) holder).song_fav_view.setVisibility(View.GONE);
                    ((ViewHolder) holder).song_unfav_view.setVisibility(View.VISIBLE);
                }
                ((ViewHolder) holder).song_fav_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseHelper databaseHelper = new DatabaseHelper(context);
                        databaseHelper.deleteSongFromFavorites(song.getName());
                        ((ViewHolder) holder).song_fav_view.setVisibility(View.GONE);
                        ((ViewHolder) holder).song_unfav_view.setVisibility(View.VISIBLE);
                    }
                });
                ((ViewHolder) holder).song_unfav_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatabaseHelper databaseHelper = new DatabaseHelper(context);
                        databaseHelper.addSongToFavorites(song);

                        ((ViewHolder) holder).song_fav_view.setVisibility(View.VISIBLE);
                        ((ViewHolder) holder).song_unfav_view.setVisibility(View.GONE);
                    }
                });
                holder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(context, MusicPlayerActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("songList", (Serializable) songList);
                    context.startActivity(intent);

                    //Show interstitial Ads
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(activity);
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
                    }
                });
            }
            else {
                MobileAds.initialize(context);
                AdLoader adLoader = new AdLoader.Builder(context, context.getString(R.string.native_ads_id))
                        .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                            @Override
                            public void onNativeAdLoaded(NativeAd nativeAd) {
                                NativeTemplateStyle styles = new
                                        NativeTemplateStyle.Builder().withMainBackgroundColor(convertColorToDrawable(R.color.white)).build();
                                ((adViewHolder) holder).Adtemplate.setStyles(styles);
                                ((adViewHolder) holder).Adtemplate.setNativeAd(nativeAd);
                            }
                        })
                        .build();
                adLoader.loadAd(new AdRequest.Builder().build());
            }
        }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 15 == 0)
            return AD_TYPE;
        return CONTENT_TYPE;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView songNameTextView, song_details_text_view;
        ImageView songImageView, song_fav_view, song_unfav_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songNameTextView = itemView.findViewById(R.id.song_name_text_view);
            song_details_text_view = itemView.findViewById(R.id.song_details_text_view);
            songImageView = itemView.findViewById(R.id.song_image_view);
            song_fav_view = itemView.findViewById(R.id.song_fav_view);
            song_unfav_view = itemView.findViewById(R.id.song_unfav_view);
        }
    }

        static class adViewHolder extends RecyclerView.ViewHolder {
            TemplateView Adtemplate;

            public adViewHolder(@NonNull View itemView) {
                super(itemView);
                Adtemplate = itemView.findViewById(R.id.nativeTemplateView);
                Adtemplate = itemView.findViewById(R.id.nativeTemplateView);
            }
        }
    public static ColorDrawable convertColorToDrawable(int color) {
        return new ColorDrawable(color);
    }
}
