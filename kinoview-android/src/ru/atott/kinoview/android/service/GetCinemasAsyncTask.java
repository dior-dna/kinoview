package ru.atott.kinoview.android.service;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.atott.kinoview.android.provider.CinemaProvider;
import ru.atott.kinoview.android.service.to.CinemaInfo;

import java.util.ArrayList;
import java.util.List;

public class GetCinemasAsyncTask extends GetJsonAsyncTask {
    private List<CinemaInfo> cinemas = null;

    public List<CinemaInfo> getCinemas() {
        return cinemas;
    }

    public void setCinemas(List<CinemaInfo> cinemas) {
        this.cinemas = cinemas;
    }

    public void fillContentProvider(ContentResolver contentResolver) {
        contentResolver.delete(CinemaProvider.CONTENT_URI, null, null);
        for (int i = 0; i < cinemas.size(); i++) {
            CinemaInfo cinemaInfo = cinemas.get(i);
            ContentValues values = new ContentValues();
            values.put(CinemaProvider.Cinema.NAME, cinemaInfo.getName());
            values.put(CinemaProvider.Cinema.ADDRESS, cinemaInfo.getAddress());
            values.put(CinemaProvider.Cinema.DESCRIPTION, cinemaInfo.getDescription());
            values.put(CinemaProvider.Cinema.VENDOR_ID, cinemaInfo.getVendorId());
            contentResolver.insert(CinemaProvider.CONTENT_URI, values);
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        try {
            if (result != null) {
                List<CinemaInfo> cinemas = new ArrayList<CinemaInfo>();
                JSONArray array = (JSONArray) result;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject cinema = (JSONObject) array.get(i);
                    CinemaInfo cinemaInfo = new CinemaInfo();
                    cinemaInfo.setAddress(cinema.getString("address"));
                    cinemaInfo.setCinemaId(cinema.getLong("cinemaId"));
                    cinemaInfo.setDescription(cinema.getString("description"));
                    cinemaInfo.setName(cinema.getString("name"));
                    cinemaInfo.setVendorId(cinema.getLong("vendorId"));
                    cinemas.add(cinemaInfo);
                }
                setCinemas(cinemas);
            }
        } catch (JSONException e) {
            Log.e("JSON Parser", e.getMessage());
        }
    }
}
