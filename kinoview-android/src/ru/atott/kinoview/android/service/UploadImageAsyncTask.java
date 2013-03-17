package ru.atott.kinoview.android.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;

public class UploadImageAsyncTask extends AsyncTask<String, Long, Bitmap> {
    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        try {
            return BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
        } catch (Exception e) {
            Log.e("UploadImageAsyncTask", "Could not load Bitmap from: " + url);
        }
        return null;
    }
}
