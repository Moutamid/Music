package com.moutimid.musicapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.musicnewapp.R;


public class RateDialogClass extends Dialog implements
        View.OnClickListener {
    double rating = 0;
    public Activity c;
    public RateDialogClass d;
    public TextView rate;
    public TextView later;
    RatingBar simpleRatingBar;
    ImageView close_btn;

    public RateDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.rate_us);
        later = (TextView) findViewById(R.id.later);
        rate = (TextView) findViewById(R.id.startbtn);
        close_btn = (ImageView) findViewById(R.id.close_btn);
        simpleRatingBar = (RatingBar) findViewById(R.id.simpleRatingBar);
        d = new RateDialogClass(c);
        rate.setOnClickListener(this);
        later.setOnClickListener(this);
        close_btn.setOnClickListener(this);

        d.setCanceledOnTouchOutside(false);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.startbtn) {
            rating = simpleRatingBar.getRating();
            if (rating != 0) {
                if (rating > 3) {
                    Uri uri = Uri.parse("market://details?id=" + c.getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        c.startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        c.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + c.getPackageName())));
                    }

                }
            }
        }
        if (v.getId() == R.id.later) {
            dismiss();
        }
        if (v.getId() == R.id.close_btn) {

            dismiss();
        };
}
}