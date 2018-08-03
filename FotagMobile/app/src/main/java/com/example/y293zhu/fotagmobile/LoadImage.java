package com.example.y293zhu.fotagmobile;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.graphics.*;

import java.io.InputStream;

public class LoadImage extends AsyncTask<String,Void,Bitmap>{
    private MainActivity listener;

    public void storeView(MainActivity listener)
    {
        this.listener = listener;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        Bitmap bitmap = null;
        try {
            InputStream is = new java.net.URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    protected void onPostExecute(Bitmap bitmap) {
        if(bitmap != null)
        {
            listener.loadImageByURL(bitmap);
        }
    }
}