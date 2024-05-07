package com.moutamid.musicapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.musicapp.Model.Config;

public class SplashActivity extends AppCompatActivity {
    InterstitialAd mInterstitialAd;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        RelativeLayout imageView = findViewById(R.id.imageView);
        float startY = imageView.getY();
        float endY = startY + getResources().getDimensionPixelSize(R.dimen.move_distance);
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", startY, endY);
        animator.setDuration(1000);
        animator.start();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("MusicApp").child("Ads");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("facebook_interstitial_ad")) {
                    Config.facebook_interstitial_ad = snapshot.child("facebook_interstitial_ad").getValue().toString();
                }
                if (snapshot.hasChild("facebook_banner_ad")) {
                    Config.facebook_banner_ad = snapshot.child("facebook_banner_ad").getValue().toString();
                }
                if (snapshot.hasChild("admob_app_id")) {
                    Config.admob_app_id = snapshot.child("admob_app_id").getValue().toString();
                }
                if (snapshot.hasChild("admob_banner_id")) {
                    Config.admob_banner_id = snapshot.child("admob_banner_id").getValue().toString();
                }
                if (snapshot.hasChild("admob_interstitial_ad")) {
                    Config.admob_interstitial_ad = snapshot.child("admob_interstitial_ad").getValue().toString();
                }
                if (snapshot.hasChild("admob_native_ads_id")) {
                    Config.native_ads_id = snapshot.child("admob_native_ads_id").getValue().toString();
                }
                show_ads();
                Toast.makeText(SplashActivity.this, "ids: "+Config.admob_app_id+"\n"+Config.facebook_interstitial_ad+"\n"+Config.facebook_banner_ad+"\n"+Config.admob_banner_id+"\n"+Config.admob_interstitial_ad+"\n"+Config.native_ads_id, Toast.LENGTH_SHORT).show();
                Log.d("ads_id", "ids:   "+Config.admob_app_id+"\n"+Config.facebook_interstitial_ad+"\n"+Config.facebook_banner_ad+"\n"+Config.admob_banner_id+"\n"+Config.admob_interstitial_ad+"\n"+Config.native_ads_id);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void start(View view) {
        startActivity(new Intent(this, MainActivity.class));
//        mInterstitialAd.show(SplashActivity.this);
    }

    public void fav(View view) {
        startActivity(new Intent(this, FavouriteScreen.class));
    }

    public void our_application(View view) {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void rate_us(View view) {
        RateDialogClass rateDialogClass = new RateDialogClass(SplashActivity.this);
        rateDialogClass.show();
    }
    public  void show_ads()
    {
        if(Config.facebook_banner_ad != null && Config.admob_interstitial_ad != null) {
            // Banner Ads Code
            final RelativeLayout adContainer = findViewById(R.id.banner_container);
            AdView adView = new AdView(this, Config.facebook_banner_ad, AdSize.BANNER_HEIGHT_50);
            adContainer.addView(adView);
            AdSettings.turnOnSDKDebugger(getApplicationContext());
            AdSettings.setTestMode(true);
            adView.loadAd();
            // Complete

            // Interstitial Ads Code
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(getApplicationContext(), Config.admob_interstitial_ad, adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            mInterstitialAd = interstitialAd;
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.d("TAG", loadAdError.toString());
                            mInterstitialAd = null;
                        }
                    });
            // Complete
        }

    }
}