package com.moutimid.musicapp;

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

import com.example.musicnewapp.R;
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
        AdView adView = new AdView(this, "2722927698006061_2722935724671925", AdSize.BANNER_HEIGHT_50);
        adContainer.addView(adView);
        AdSettings.turnOnSDKDebugger(getApplicationContext());
        AdSettings.setTestMode(true);
        adView.loadAd();

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(getApplicationContext(), "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("TAG", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d("TAG", loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
        RelativeLayout imageView = findViewById(R.id.imageView);

        // Get the current Y position of the imageView
        float startY = imageView.getY();

        // Calculate the end Y position which is 100dp down from the top
        float endY = startY + getResources().getDimensionPixelSize(R.dimen.move_distance);

        // Create an ObjectAnimator to animate the translationY property
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", startY, endY);
        animator.setDuration(1000); // Set the duration of the animation in milliseconds (e.g., 1000ms = 1 second)
        animator.start(); // Start the animation
    }

    public void start(View view) {
        mInterstitialAd.show(SplashActivity.this);
        startActivity(new Intent(this, MainActivity.class));
    }

    public void fav(View view) {
        startActivity(new Intent(this, FavouriteScreen.class));
//        startActivity(new Intent(this, Ad_Banner.class));
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