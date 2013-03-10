package ru.atott.kinoview.android.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import ru.atott.kinoview.android.R;
import ru.atott.kinoview.android.provider.CinemaProvider;
import ru.atott.kinoview.android.service.GetCinemasAsyncTask;
import ru.atott.kinoview.android.service.GetJsonAsyncTask;

public class WelcomeActivity extends Activity {
    private Cursor cinemasCursor;
    private ProgressDialog loadCinemasProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        ListView cinemaList = (ListView) findViewById(R.id.cinema_list);
        cinemaList.addHeaderView(getLayoutInflater().inflate(R.layout.welcome_activity_head_part, null), null, false);

        fillCinemaList(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cinemasCursor != null && !cinemasCursor.isClosed()) {
            cinemasCursor.close();
        }
    }

    private void fillCinemaList(boolean loadIfNoData) {
        ListView cinemaList = (ListView) findViewById(R.id.cinema_list);

        cinemasCursor = getContentResolver().query(CinemaProvider.Vendor.CONTENT_URI, null, null, null, null);
        if (cinemasCursor.getCount() == 0) {
            cinemasCursor.close();
            if (loadIfNoData) {
                getCinemasAsyncTask.execute(getResources().getString(R.string.kinoview_service_getCinemasUrl));
                return;
            }
        }
        CursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.cinema_list_item, cinemasCursor,
                new String[] {CinemaProvider.Cinema.NAME}, new int[] {R.id.name}, 0);
        cinemaList.setAdapter(adapter);
    }

    private GetJsonAsyncTask getCinemasAsyncTask = new GetCinemasAsyncTask() {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadCinemasProgressDialog = new ProgressDialog(WelcomeActivity.this);
            loadCinemasProgressDialog.setMessage(getResources().getString(R.string.ui_cinemas_load));
            loadCinemasProgressDialog.show();
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (result != null) {
                fillContentProvider(getContentResolver());
            }
            loadCinemasProgressDialog.dismiss();
            fillCinemaList(false);
        }
    };
}
