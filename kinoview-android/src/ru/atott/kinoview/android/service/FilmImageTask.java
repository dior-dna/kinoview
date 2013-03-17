package ru.atott.kinoview.android.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public class FilmImageTask extends UploadImageAsyncTask {
    private Long imageEId;
    private String url;
    private String cacheFolder;

    public FilmImageTask(Long imageEId, String url, String cacheFolder) {
        this.imageEId = imageEId;
        this.url = url;
        this.cacheFolder = cacheFolder;
    }

    public Long getImageEId() {
        return imageEId;
    }

    public void setImageEId(Long imageEId) {
        this.imageEId = imageEId;
    }

    public File getImageCacheFile() {
        return new File(cacheFolder, "film_" + this.imageEId + ".png");
    }

    public String getUrl() {
        return url;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        File imageCacheFile = getImageCacheFile();
        Log.i("FilmImageTask", "imageCacheFile = " + imageCacheFile.getPath());
        if (imageCacheFile.exists()) {
            Log.i("FilmImageTask", "Get Image from cache.");
            return BitmapFactory.decodeFile(imageCacheFile.getPath());
        } else {
            Log.i("FilmImageTask", "Get Image from network.");
            Bitmap bitmap = super.doInBackground(getUrl());
            if (bitmap != null) {
                try {
                    FileOutputStream out = new FileOutputStream(imageCacheFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                } catch (Exception e) {
                    Log.e("FilmImageTask", "Cannot save image at: " + imageCacheFile.getPath());
                    Log.e("FilmImageTask", e.toString());
                }
            }
            return bitmap;
        }
    }
}
