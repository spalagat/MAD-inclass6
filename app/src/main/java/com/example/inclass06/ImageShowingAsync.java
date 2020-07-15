package com.example.inclass06;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ImageShowingAsync extends AsyncTask<String,Void,Bitmap> {

    MainActivity activity;

    public ImageShowingAsync (MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    Bitmap getImageBitmap(String... strings) {
        try {
            Log.d("check",""+strings[0]);
            if(strings[0]==null){
                Log.d("sandeep","is a hero");
                return null;
            }
            URL url = new URL(strings[0]);
            if(strings[0]==null){
                Log.d("sandeep","is a hero");
                return null;
            }
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (MalformedURLException e) {

            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bitmap = getImageBitmap(strings[0]);


        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        //Log.d("bitmap",bitmap.toString());
        //activity.article_image.setImageBitmap(bitmap);
        activity.display_image(bitmap);
    }
}
