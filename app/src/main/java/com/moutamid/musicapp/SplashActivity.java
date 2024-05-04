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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class SplashActivity extends AppCompatActivity {
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        final RelativeLayout adContainer = findViewById(R.id.banner_container);
        RelativeLayout imageView = findViewById(R.id.imageView);
        float startY = imageView.getY();
        float endY = startY + getResources().getDimensionPixelSize(R.dimen.move_distance);
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", startY, endY);
        animator.setDuration(1000);
        animator.start();

        // Banner Ads Code
        AdView adView = new AdView(this, getString(R.string.facebook_banner_ad), AdSize.BANNER_HEIGHT_50);
        adContainer.addView(adView);
        AdSettings.turnOnSDKDebugger(getApplicationContext());
        AdSettings.setTestMode(true);
        adView.loadAd();
        // Complete

        // Interstitial Ads Code
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(getApplicationContext(), getString(R.string.admob_interstitial_ad), adRequest,
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
}