package ru.atott.kinoview.android.provider;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import ru.atott.kinoview.android.db.DB;
import ru.atott.kinoview.android.exception.AppException;

public class FilmProvider extends ContentProvider {
    public static final String AUTHORITY = "ru.atott.kinoview.android.provider.FilmProvider";

    public static interface Film extends BaseColumns {
        Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + Film.CONTENT_PATH);
        String CONTENT_PATH = "film";
        String FILM_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.kinoview.film";
        String FILMS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.kinoview.films";

        String TITLE = "title";
        String DURATION = "duration";
        String DIRECTOR = "director";
        String EXTERNAL_ID = "eid";
        String ACTORS = "actors";
        String GENRE = "genre";

        String[] PROJECTION_ALL = {_ID, TITLE, DIRECTOR, DURATION, EXTERNAL_ID, ACTORS, GENRE};
    }

    private static final int MATCH_FILMS = 1;
    private static final int MATCH_FILM = 2;
    private static final UriMatcher URI_MATCHER;
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, Film.CONTENT_PATH, MATCH_FILMS);
        URI_MATCHER.addURI(AUTHORITY, Film.CONTENT_PATH + "/#", MATCH_FILM);
    }

    private SQLiteDatabase db = null;

    @Override
    public boolean onCreate() {
        this.db = new DB(getContext()).getWritableDatabase();
        if (this.db == null) {
            return false;
        }
        if (this.db.isReadOnly()) {
            this.db.close();
            this.db = null;
            return false;
        }
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int matched = URI_MATCHER.match(uri);
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DB.Films.TABLE_NAME);
        switch (matched) {
            case MATCH_FILMS:
                break;
            case MATCH_FILM:
                builder.appendWhere(Film._ID + " = " + uri.getLastPathSegment());
                break;
        }
        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case MATCH_FILMS:
                return Film.FILMS_CONTENT_TYPE;
            case MATCH_FILM:
                return Film.FILM_CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (URI_MATCHER.match(uri) != MATCH_FILMS) {
            throw new IllegalArgumentException("Unsupported URI for insertion: " + uri + "; matched: " + URI_MATCHER.match(uri));
        }
        long id = db.insert(DB.Films.TABLE_NAME, null, values);
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(itemUri, null);
            return itemUri;
        }
        throw new AppException("Problem while inserting into " + DB.Films.TABLE_NAME + ", uri: " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int delCount = 0;
        switch (URI_MATCHER.match(uri)) {
            case MATCH_FILMS:
                delCount = db.delete(DB.Films.TABLE_NAME, selection, selectionArgs);
                break;
            case MATCH_FILM:
                String id = uri.getLastPathSegment();
                String where = Film._ID + " = " + id;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }
                delCount = db.delete(DB.Films.TABLE_NAME, where, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        if (delCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return delCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updateCount = 0;
        switch (URI_MATCHER.match(uri)) {
            case MATCH_FILMS:
                updateCount = db.update(DB.Films.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MATCH_FILM:
                String id = uri.getLastPathSegment();
                String where = Film._ID + " = " + id;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }
                updateCount = db.update(DB.Films.TABLE_NAME, values, where, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        if (updateCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updateCount;
    }
}
