package com.example.y293zhu.fotagmobile;

import android.view.*;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.graphics.*;

import java.util.Vector;

public class Model {
    private Vector< ImageItem > images;
    private int rate;
    private int originSize;
    private int[] originID;
    private int enlargeID;
    private Bitmap enlargeBitmap;
    private MainActivity controller;
    private View landscapeLarge;
    private View portraitLarge;
    private View landscape;
    private View portrait;
    private int orientation;

    Model(MainActivity controller)
    {
        this.controller = controller;
        this.images = new Vector<>();
        this.originSize = 10;
        originID = new int[originSize];
        originID[0] = R.drawable.cat1;
        originID[1] = R.drawable.cat2;
        originID[2] = R.drawable.cat3;
        originID[3] = R.drawable.cat4;
        originID[4] = R.drawable.puppy1;
        originID[5] = R.drawable.puppy2;
        originID[6] = R.drawable.puppy3;
        originID[7] = R.drawable.puppy4;
        originID[8] = R.drawable.uw1;
        originID[9] = R.drawable.uw2;
        this.rate = 0;
    }

    public void initialize(View landscape, View portrait, View landscapeLarge, View portraitLarge)
    {
        this.landscape = landscape;
        this.portrait = portrait;
        this.landscapeLarge = landscapeLarge;
        this.portraitLarge = portraitLarge;
        landscapeLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeLarge();
            }
        });

        portraitLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeLarge();
            }
        });
    }

    public void setOrientation(int orientation)
    {
        if(0 != this.enlargeID)
        {
            int id = this.enlargeID;
            removeLarge();
            this.orientation = orientation;
            enlargeImage(id);
        }
        this.orientation = orientation;
        setRate(rate);
    }

    public void setRate(int rate) {
        this.rate = rate;
        ((GridLayout) landscape).removeAllViews();
        ((LinearLayout) portrait).removeAllViews();
        switch (orientation)
        {
            case 1:
                for(int i = 0; i < getSize(); i++)
                {
                    if(rate <= getRatebyIndex(i))
                    {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        ((LinearLayout) portrait).addView(getViewbyIndex(i),params);
                    }
                }
                break;
            case 2:
                int total = 0;
                for(int i = 0; i < getSize(); i++)
                {
                    if(rate <= getRatebyIndex(i))
                    {
                        GridLayout.Spec rowSpec =GridLayout.spec(total/2,1f);
                        GridLayout.Spec colSpec =GridLayout.spec(total%2,1f);
                        GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec,colSpec);
                        ((GridLayout) landscape).addView(getViewbyIndex(i),params);
                        total++;
                    }
                }
                break;
        }
    }

    public void removeLarge()
    {
        this.enlargeID = 0;
        controller.removeEnlargeImage();
    }

    public void enlargeImage(int id)
    {
        this.enlargeID = id;
        switch (orientation)
        {
            case 1:
                ImageView pic1 = (ImageView) portraitLarge.findViewById(R.id.largeImage);
                pic1.setImageResource(id);
                break;
            case 2:
                ImageView pic2 = (ImageView) landscapeLarge.findViewById(R.id.largeImage);
                pic2.setImageResource(id);
                break;
        }
        controller.enlargeImage(id);
    }

    public void enlargeImage(Bitmap bitmap)
    {
        this.enlargeBitmap = bitmap;
        switch (orientation)
        {
            case 1:
                ImageView pic1 = (ImageView) portraitLarge.findViewById(R.id.largeImage);
                pic1.setImageBitmap(bitmap);
                break;
            case 2:
                ImageView pic2 = (ImageView) landscapeLarge.findViewById(R.id.largeImage);
                pic2.setImageBitmap(bitmap);
                break;
        }
        controller.enlargeImage(bitmap);
    }


    public View getViewContainer(){
        if(1 == orientation)
        {
            return portrait;
        }
        return landscape;
    }

    public View getEnlarge()
    {
        if(1 == orientation)
        {
            return portraitLarge;
        }
        return landscapeLarge;

    }

    public void addImageItemByURL(View imageview, Bitmap bitmap)
    {
        ImageItem imageitem = new ImageItem(bitmap, imageview, this, 2);
        images.add(imageitem);
    }

    public void addImageItem(View imageview, int id)
    {
        ImageItem imageitem = new ImageItem(id, imageview, this, 1);
        images.add(imageitem);
    }

    public void updateRating(){ controller.updateRating(rate); }

    public int getRate(){ return this.rate; }

    public int getRatebyIndex(int index){ return images.elementAt(index).getRate(); }

    public int getIDbyIndex(int index){ return this.originID[index]; }

    public int getOriginSize(){ return this.originSize; }

    public int getSize(){ return images.size(); }

    public View getViewbyIndex(int index){ return images.elementAt(index).getView(); }

}
