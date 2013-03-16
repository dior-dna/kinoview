package ru.atott.kinoview.android.service;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.atott.kinoview.android.provider.FilmProvider;
import ru.atott.kinoview.android.service.to.FilmInfo;

import java.util.ArrayList;
import java.util.List;

public class GetFilmsAsyncTask extends GetJsonAsyncTask {
    private List<FilmInfo> films = null;

    public List<FilmInfo> getFilms() {
        return films;
    }

    public void setFilms(List<FilmInfo> films) {
        this.films = films;
    }

    public void fillContentProvider(ContentResolver contentResolver) {
        for (int i = 0; i < films.size(); i++) {
            FilmInfo filmInfo = films.get(i);
            Cursor items = contentResolver.query(FilmProvider.Film.CONTENT_URI, FilmProvider.Film.PROJECTION_ALL,
                    FilmProvider.Film.EXTERNAL_ID + " = " + filmInfo.getEid(), null, null);
            if (items.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put(FilmProvider.Film.TITLE, filmInfo.getTitle());
                values.put(FilmProvider.Film.DIRECTOR, filmInfo.getDirector());
                values.put(FilmProvider.Film.GENRE, filmInfo.getGenre());
                values.put(FilmProvider.Film.ACTORS, filmInfo.getActors());
                values.put(FilmProvider.Film.DURATION, filmInfo.getDuration());
                values.put(FilmProvider.Film.EXTERNAL_ID, filmInfo.getEid());
                contentResolver.insert(FilmProvider.Film.CONTENT_URI, values);
            }
            items.close();
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        try {
            if (result != null) {
                List<FilmInfo> filmInfos = new ArrayList<FilmInfo>();
                JSONArray array = (JSONArray) result;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject film = (JSONObject) array.get(i);
                    FilmInfo filmInfo = new FilmInfo();
                    filmInfo.setActors(film.getString("actors"));
                    filmInfo.setDirector(film.getString("director"));
                    filmInfo.setDuration(film.getLong("duration"));
                    filmInfo.setEid(film.getLong("eid"));
                    filmInfo.setGenre(film.getString("genre"));
                    filmInfo.setTitle(film.getString("title"));
                    filmInfos.add(filmInfo);
                }
                setFilms(filmInfos);
            }
        } catch (JSONException e) {
            Log.e("JSON Parser", e.getMessage());
        }
    }
}
