package ru.atott.kinoview.android.fragment;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import ru.atott.kinoview.android.R;
import ru.atott.kinoview.android.Utils;
import ru.atott.kinoview.android.provider.FilmProvider;
import ru.atott.kinoview.android.service.GetFilmsAsyncTask;

import java.util.Date;

public class FilmsFragment extends Fragment {
    private Cursor filmsCursor;
    private ProgressDialog loadFilmsProgressDialog;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.films_fragment, container, false);
        fillFilmsList(true);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        if (filmsCursor != null && !filmsCursor.isClosed()) {
            filmsCursor.close();
        }
        super.onDestroyView();
    }

    private void fillFilmsList(boolean loadIfNoData) {
        ListView filmList = (ListView) rootView.findViewById(R.id.film_list);
        filmsCursor = getActivity().getContentResolver().query(FilmProvider.Film.CONTENT_URI, null, null, null, null);
        if (filmsCursor.getCount() == 0) {
            filmsCursor.close();
            if (loadIfNoData) {
                String date = Utils.getApiFormattedDate(new Date());
                String url = getResources().getString(R.string.kinoview_service_getFilmsUrl, date);
                getFilmsAsyncTask.execute(url);
                return;
            }
        }
        CursorAdapter adapater = new SimpleCursorAdapter(getActivity(), R.layout.film_list_item, filmsCursor,
                new String[] {FilmProvider.Film.TITLE}, new int[] {R.id.title}, 0);
        filmList.setAdapter(adapater);
    }

    private GetFilmsAsyncTask getFilmsAsyncTask = new GetFilmsAsyncTask() {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadFilmsProgressDialog = new ProgressDialog(getActivity());
            loadFilmsProgressDialog.setMessage(getResources().getString(R.string.ui_cinemas_load));
            loadFilmsProgressDialog.show();
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (result != null) {
                fillContentProvider(getActivity().getContentResolver());
            }
            loadFilmsProgressDialog.dismiss();
            fillFilmsList(false);
        }
    };
}
