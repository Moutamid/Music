package com.moutimid.musicapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicnewapp.R;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

/**
 *  https://github.com/vimalcvs
 *
 *  Date: 19/11/2020
 */

public class Ad_Banner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_banner);

//         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadAdFifty();
        loadAdNinety();
    }

    private void loadAdFifty() {
        final RelativeLayout adContainer = findViewById(R.id.ad_banner_50);
        AdView adView = new AdView(this, "2722927698006061_2722935724671925", AdSize.BANNER_HEIGHT_50);
        adContainer.addView(adView);
        AdSettings.turnOnSDKDebugger(getApplicationContext());
        AdSettings.setTestMode(true);// for get test ad in your device
        adView.loadAd();

    }

    private void loadAdNinety() {
        final RelativeLayout adContainer = findViewById(R.id.ad_banner_90);
        AdView adView = new AdView(this,  "2722927698006061_2722935724671925", AdSize.BANNER_HEIGHT_90);
        adContainer.addView(adView);
        adView.loadAd();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}