package ru.atott.kinoview.android.fragment;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import ru.atott.kinoview.android.R;
import ru.atott.kinoview.android.Utils;
import ru.atott.kinoview.android.provider.FilmProvider;
import ru.atott.kinoview.android.service.GetFilmsAsyncTask;
import ru.atott.kinoview.android.view.ResisableImageView;

import java.util.Date;

public class FilmsFragment extends Fragment {
    private Cursor filmsCursor;
    private ProgressDialog loadFilmsProgressDialog;
    private View rootView;
    private DisplayImageOptions displayImageOptions;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.films_fragment, container, false);

        displayImageOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.film_none)
                .resetViewBeforeLoading()
                .cacheInMemory()
                .cacheOnDisc()
                .build();

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
        filmList.setAdapter(new FilmAdapter(getActivity(), filmsCursor));
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

    private class FilmAdapter extends SimpleCursorAdapter {
        private class ViewHolder {
            public ResisableImageView imageView;
            public TextView titleView;
            public TextView genreView;
            public TextView durationView;
        }

        public FilmAdapter(Context context, Cursor c) {
            super(context, R.layout.film_list_item, c, new String[] {FilmProvider.Film.TITLE}, new int[] {R.id.title}, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            final ViewHolder holder;
            if (convertView == null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.film_list_item, parent, false);
                holder = new ViewHolder();
                holder.imageView = (ResisableImageView) view.findViewById(R.id.image);
                holder.titleView = (TextView) view.findViewById(R.id.title);
                holder.genreView = (TextView) view.findViewById(R.id.genre);
                holder.durationView = (TextView) view.findViewById(R.id.duration);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            getCursor().moveToPosition(position);
            String title = getCursor().getString(getCursor().getColumnIndex(FilmProvider.Film.TITLE));
            Long eid = getCursor().getLong(getCursor().getColumnIndex(FilmProvider.Film.EXTERNAL_ID));
            String imageUrl = getResources().getString(R.string.kinoview_service_getFilmImageUrl, eid);
            String genre = getCursor().getString(getCursor().getColumnIndex(FilmProvider.Film.GENRE));
            Long duration = getCursor().getLong(getCursor().getColumnIndex(FilmProvider.Film.DURATION));
            holder.titleView.setText(title);
            holder.genreView.setText(genre);
            holder.durationView.setText(Utils.getDurationString(duration));
            imageLoader.displayImage(imageUrl, holder.imageView, displayImageOptions);
            return view;
        }
    }
}
