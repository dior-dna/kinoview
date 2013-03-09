package ru.atott.kinoview.android.activity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import ru.atott.kinoview.android.R;
import ru.atott.kinoview.android.provider.CinemaProvider;

public class WelcomeActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        ListView cinemaList = (ListView) findViewById(R.id.cinema_list);
        cinemaList.addHeaderView(getLayoutInflater().inflate(R.layout.welcome_activity_head_part, null), null, false);

        initCinemaList();
    }

    private void initCinemaList() {
        ListView cinemaList = (ListView) findViewById(R.id.cinema_list);
        String[] projection = {CinemaProvider.CinemaItem._ID, CinemaProvider.CinemaItem.NAME};
        String[] from = {CinemaProvider.CinemaItem.NAME};
        int[] to = {R.id.name};
        Cursor cinemas = managedQuery(CinemaProvider.CONTENT_URI, projection, null, null, null);
        CursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.cinema_list_item, cinemas, from, to, 0);
        cinemaList.setAdapter(adapter);
    }
}
