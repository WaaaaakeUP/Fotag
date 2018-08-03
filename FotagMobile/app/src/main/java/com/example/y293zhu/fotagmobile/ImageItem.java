package com.example.y293zhu.fotagmobile;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;

public class ImageItem {
    private int mode;
    private Bitmap bitmap;
    private int rate;
    private int id;
    private View view;
    private ImageView imageview;
    private RatingBar ratingbar;
    private ImageButton clearrate;
    private Model controller;

    ImageItem(int id, View view, Model controller, int mode) {
        this.id = id;
        this.view = view;
        this.rate = 0;
        this.controller = controller;
        this.mode = mode;
        initialize();
    }

    ImageItem(Bitmap bitmap, View view, Model controller, int mode) {
        this.bitmap = bitmap;
        this.view = view;
        this.rate = 0;
        this.controller = controller;
        this.mode = mode;
        initialize();
    }

    private void initialize() {
        this.imageview = (ImageView) this.view.findViewById(R.id.imageView);
        this.ratingbar =  (RatingBar) this.view.findViewById(R.id.imageRaingBar);
        this.clearrate = (ImageButton) this.view.findViewById(R.id.imageRateClear);

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mode)
                {
                    case 1:
                        controller.enlargeImage(id);
                        break;
                    case 2:
                        controller.enlargeImage(bitmap);
                        break;
                }
            }
        });

        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int vRate = (int) v;
                rate = vRate;
                controller.updateRating();
            }
        });

        clearrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingbar.setRating(0);
            }
        });
    }

    public int getRate(){ return this.rate; }

    public View getView() { return this.view; }
}
