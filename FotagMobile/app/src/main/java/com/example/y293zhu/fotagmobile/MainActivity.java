package com.example.y293zhu.fotagmobile;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{
    private Model model;
    private boolean imageShow;
    private boolean enlarge;
    private boolean showEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.model = new Model(this);
        setContentView(R.layout.activity_main);
        initialize();
    }

    @Override
    public void onConfigurationChanged (Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        int orientation = getResources().getConfiguration().orientation;
        ViewGroup MainView = (ViewGroup)getWindow().getDecorView();
        TextView appName = (TextView) MainView.getChildAt(0).findViewById(R.id.appName);
        EditText edittext  = (EditText)MainView.getChildAt(0).findViewById(R.id.url);
        ScrollView container = (ScrollView)MainView.getChildAt(0).findViewById(R.id.imageContainer);
        model.setOrientation(orientation);
        container.removeAllViews();
        RelativeLayout.LayoutParams lparams = (RelativeLayout.LayoutParams)edittext.getLayoutParams();
        if(imageShow)
        {
            container.addView(model.getViewContainer());
        }
        switch (orientation)
        {
            case Configuration.ORIENTATION_LANDSCAPE:
                appName.setText("Fotag Mobile");
                lparams.width = 500;
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                appName.setText("");
                lparams.width = 240;
                break;
        }
        if (this.showEditText)
        {
            edittext.setLayoutParams(lparams);
        }
    }

    public void initialize(){
        this.imageShow = false;
        this.enlarge = false;
        this.showEditText = false;
        int orientation = getResources().getConfiguration().orientation;
        for(int i = 0; i < model.getOriginSize(); i++)
        {
            int imageID = model.getIDbyIndex(i);
            View image = getLayoutInflater().inflate(R.layout.item_image,null);
            ImageView imageView = (ImageView)image.findViewById(R.id.imageView);
            imageView.setImageResource(imageID);
            model.addImageItem(image, imageID);
        }
        ViewGroup MainView = (ViewGroup)getWindow().getDecorView();
        TextView appName = (TextView) MainView.getChildAt(0).findViewById(R.id.appName);
        RatingBar ratingBar = (RatingBar)MainView.getChildAt(0).findViewById(R.id.toolbarRating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int value = (int) v;
                updateRating(value);
            }
        });

        ScrollView container = (ScrollView)MainView.getChildAt(0).findViewById(R.id.imageContainer);
        container.removeAllViews();
        View landscapeContainer = getLayoutInflater().inflate(R.layout.landscape_container,null);
        View portraitContainer = getLayoutInflater().inflate(R.layout.portrait_container,null);
        View landscapeLarge = getLayoutInflater().inflate(R.layout.large_landscape,null);
        View portraitLarge = getLayoutInflater().inflate(R.layout.large_portrait,null);
        switch (orientation)
        {
            case Configuration.ORIENTATION_LANDSCAPE:
                appName.setText("Fotag Mobile");
                break;
            default:
                appName.setText("");
                break;
        }
        model.initialize(landscapeContainer, portraitContainer,landscapeLarge,portraitLarge);
        model.setOrientation(orientation);
    }

    public void updateRating(int rate)
    {
        if (!imageShow){ return; }
        ViewGroup MainView = (ViewGroup)getWindow().getDecorView();
        model.setRate(rate);
    }

    public void enlargeImage(int id)
    {
        if(enlarge){ return; }
        ViewGroup MainView = (ViewGroup)getWindow().getDecorView();
        MainView.addView(model.getEnlarge());
        enlarge = true;
    }

    public void enlargeImage(Bitmap bitmap)
    {
        if(enlarge){ return; }
        ViewGroup MainView = (ViewGroup)getWindow().getDecorView();
        MainView.addView(model.getEnlarge());
        enlarge = true;
    }

    public void removeEnlargeImage()
    {
        if (!enlarge) { return; }
        ViewGroup MainView = (ViewGroup)getWindow().getDecorView();
        MainView.removeView(model.getEnlarge());
        enlarge = false;
    }

    public void onRateClearClick(View view) {
        ViewGroup MainView = (ViewGroup)getWindow().getDecorView();
        RatingBar ratingBar = (RatingBar)MainView.getChildAt(0).findViewById(R.id.toolbarRating);
        ratingBar.setRating(0);
    }

    public void onClearClick(View view) {
        imageShow = false;
        ViewGroup MainView = (ViewGroup)getWindow().getDecorView();
        ScrollView container = (ScrollView)MainView.getChildAt(0).findViewById(R.id.imageContainer);
        container.removeAllViews();
    }

    public void onLoadClick(View view) {
        imageShow = true;
        int rate = model.getRate();
        updateRating(rate);
        ViewGroup MainView = (ViewGroup)getWindow().getDecorView();
        ScrollView container = (ScrollView)MainView.getChildAt(0).findViewById(R.id.imageContainer);
        container.removeAllViews();
        container.addView(model.getViewContainer());
    }

    public void loadImageByURL(Bitmap bitmap)
    {
        ViewGroup MainView = (ViewGroup)getWindow().getDecorView();
        EditText edittext  = (EditText)MainView.getChildAt(0).findViewById(R.id.url);
        View image = getLayoutInflater().inflate(R.layout.item_image,null);
        ImageView imageview = image.findViewById(R.id.imageView);
        imageview.setImageBitmap(bitmap);
        this.model.addImageItemByURL(image, bitmap);
        updateRating(model.getRate());
        edittext.getText().clear();
    }

    public void onSearchClick(View view)
    {
        ViewGroup MainView = (ViewGroup)getWindow().getDecorView();
        EditText edittext  = (EditText)MainView.getChildAt(0).findViewById(R.id.url);
        if(!this.showEditText){
            RelativeLayout.LayoutParams lparams = (RelativeLayout.LayoutParams)edittext.getLayoutParams();
            int orientation = getResources().getConfiguration().orientation;
            switch (orientation)
            {
                case Configuration.ORIENTATION_PORTRAIT:
                    lparams.width = 240;
                    break;
                case Configuration.ORIENTATION_LANDSCAPE:
                    lparams.width = 500;
                    break;
            }
            edittext.setLayoutParams(lparams);
            this.showEditText = true;
            return;
        }

        String url = edittext.getText().toString();
        LoadImage newTask = new LoadImage();
        newTask.storeView(this);
        newTask.execute(url);
    }
}
