package ru.atott.kinoview.android.fragment;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import ru.atott.kinoview.android.R;
import ru.atott.kinoview.android.provider.CinemaProvider;
import ru.atott.kinoview.android.service.GetCinemasAsyncTask;
import ru.atott.kinoview.android.service.GetJsonAsyncTask;

public class CinemasFragment extends Fragment {
    private Cursor cinemasCursor;
    private ProgressDialog loadCinemasProgressDialog;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.cinemas_fragment, container, false);
        fillCinemaList(true);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (cinemasCursor != null && !cinemasCursor.isClosed()) {
            cinemasCursor.close();
        }
    }

    private void fillCinemaList(boolean loadIfNoData) {
        ListView cinemaList = (ListView) rootView.findViewById(R.id.cinema_list);

        cinemasCursor = getActivity().getContentResolver().query(CinemaProvider.Vendor.CONTENT_URI, null, null, null, null);
        if (cinemasCursor.getCount() == 0) {
            cinemasCursor.close();
            if (loadIfNoData) {
                getCinemasAsyncTask.execute(getResources().getString(R.string.kinoview_service_getCinemasUrl));
                return;
            }
        }
        CursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.cinema_list_item, cinemasCursor,
                new String[] {CinemaProvider.Cinema.NAME}, new int[] {R.id.name}, 0);
        cinemaList.setAdapter(adapter);
    }

    private GetJsonAsyncTask getCinemasAsyncTask = new GetCinemasAsyncTask() {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadCinemasProgressDialog = new ProgressDialog(getActivity());
            loadCinemasProgressDialog.setMessage(getResources().getString(R.string.ui_cinemas_load));
            loadCinemasProgressDialog.show();
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (result != null) {
                fillContentProvider(getActivity().getContentResolver());
            }
            loadCinemasProgressDialog.dismiss();
            fillCinemaList(false);
        }
    };
}
