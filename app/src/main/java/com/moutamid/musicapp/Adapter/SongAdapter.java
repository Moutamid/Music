package com.moutamid.musicapp.Adapter;

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
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.moutamid.musicapp.Model.Config;
import com.moutamid.musicapp.Model.DatabaseHelper;
import com.moutamid.musicapp.Model.SongsModel;
import com.moutamid.musicapp.MusicPlayerActivity;
import com.moutamid.musicapp.R;
import java.io.Serializable;
import java.util.List;
    public class SongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
   private static final int AD_TYPE = 1;
    private static final int CONTENT_TYPE = 2;
    private Context context;
    private List<SongsModel> songList;
    InterstitialAd mInterstitialAd;
    Activity activity;


    public SongAdapter(Activity activity, Context context, List<SongsModel> songList) {
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
        else {
            ViewHolder mYourViewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item, parent, false));
            return mYourViewHolder;
        }
    }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


            if (getItemViewType(position) == CONTENT_TYPE) {
                SongsModel song = songList.get(position);
                ((ViewHolder) holder).songNameTextView.setText(song.getName());
                ((ViewHolder) holder).song_details_text_view.setText(song.getDetails());
                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(context, Config.admob_interstitial_ad, adRequest,
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
                    public void onClick(View view)
                    {
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

                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(activity);
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                Intent intent = new Intent(context, MusicPlayerActivity.class);
                                intent.putExtra("position", position);
                                intent.putExtra("songList", (Serializable) songList);
                                context.startActivity(intent);
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);
                            }

                            @Override
                            public void onAdImpression() {
                                super.onAdImpression();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                            }
                        });
                    } else {
                        Intent intent = new Intent(context, MusicPlayerActivity.class);
                        intent.putExtra("position", position);
                        intent.putExtra("songList", (Serializable) songList);
                        context.startActivity(intent);
                    }
                });

            }
            else {
                SongsModel song = songList.get(position);
                ((adViewHolder) holder).songNameTextView.setText(song.getName());
                ((adViewHolder) holder).song_details_text_view.setText(song.getDetails());
                 DatabaseHelper databaseHelper = new DatabaseHelper(context);
                boolean songFavorite = databaseHelper.isSongFavorite(song.getName());
                Log.d("data", songFavorite + "  " + song.getName());
                if (songFavorite) {
                    ((adViewHolder) holder).song_fav_view.setVisibility(View.VISIBLE);
                    ((adViewHolder) holder).song_unfav_view.setVisibility(View.GONE);
                } else {
                    ((adViewHolder) holder).song_fav_view.setVisibility(View.GONE);
                    ((adViewHolder) holder).song_unfav_view.setVisibility(View.VISIBLE);
                }
                ((adViewHolder) holder).song_fav_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        DatabaseHelper databaseHelper = new DatabaseHelper(context);
                        databaseHelper.deleteSongFromFavorites(song.getName());
                        ((adViewHolder) holder).song_fav_view.setVisibility(View.GONE);
                        ((adViewHolder) holder).song_unfav_view.setVisibility(View.VISIBLE);
                    }
                });
                ((adViewHolder) holder).song_unfav_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseHelper databaseHelper = new DatabaseHelper(context);
                        databaseHelper.addSongToFavorites(song);
                        ((adViewHolder) holder).song_fav_view.setVisibility(View.VISIBLE);
                        ((adViewHolder) holder).song_unfav_view.setVisibility(View.GONE);
                    }
                });
                holder.itemView.setOnClickListener(view -> {

                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(activity);
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                Intent intent = new Intent(context, MusicPlayerActivity.class);
                                intent.putExtra("position", position);
                                intent.putExtra("songList", (Serializable) songList);
                                context.startActivity(intent);
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);
                            }

                            @Override
                            public void onAdImpression() {
                                super.onAdImpression();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                            }
                        });
                    } else {
                        Intent intent = new Intent(context, MusicPlayerActivity.class);
                        intent.putExtra("position", position);
                        intent.putExtra("songList", (Serializable) songList);
                        context.startActivity(intent);
                    }
                });
                MobileAds.initialize(context);
                AdLoader adLoader = new AdLoader.Builder(context, Config.native_ads_id)
                        .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                            @Override
                            public void onNativeAdLoaded(NativeAd nativeAd) {
                                NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(convertColorToDrawable(R.color.white)).build();
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
            TextView songNameTextView, song_details_text_view;
            ImageView songImageView, song_fav_view, song_unfav_view;

            public adViewHolder(@NonNull View itemView) {
                super(itemView);
                Adtemplate = itemView.findViewById(R.id.nativeTemplateView);
                Adtemplate = itemView.findViewById(R.id.nativeTemplateView);
                songNameTextView = itemView.findViewById(R.id.song_name_text_view);
                song_details_text_view = itemView.findViewById(R.id.song_details_text_view);
                songImageView = itemView.findViewById(R.id.song_image_view);
                song_fav_view = itemView.findViewById(R.id.song_fav_view);
                song_unfav_view = itemView.findViewById(R.id.song_unfav_view);
            }
        }
    public static ColorDrawable convertColorToDrawable(int color) {
        return new ColorDrawable(color);
    }
}
