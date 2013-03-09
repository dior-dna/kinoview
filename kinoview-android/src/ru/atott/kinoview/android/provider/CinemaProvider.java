package ru.atott.kinoview.android.provider;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import ru.atott.kinoview.android.db.DatabaseHelper;
import ru.atott.kinoview.android.exception.AppException;

public class CinemaProvider extends ContentProvider {
    public static final String AUTHORITY = "ru.atott.kinoview.android.provider.CinemaProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CinemaItem.CONTENT_PATH);

    private static final int CINEMA_LIST_TYPE = 1;
    private static final int CINEMA_ITEM_TYPE = 2;
    private static final UriMatcher URI_MATCHER;

    private SQLiteDatabase db = null;

    public static interface CinemaItem extends BaseColumns {
        public static final Uri CONTENT_URI = CinemaProvider.CONTENT_URI;
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.kinoview.cinema";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.kinoview.cinema";
        public static final String CONTENT_PATH = "cinema";

        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String IMAGE_URI = "image";
        public static final String ADDRESS = "address";
        public static final String SORT_ORDER_DEFAULT = NAME + " ASC";
        public static final String[] PROJECTION_ALL = {_ID, NAME, DESCRIPTION, IMAGE_URI, ADDRESS};
    }

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, CinemaItem.CONTENT_PATH, CINEMA_LIST_TYPE);
        URI_MATCHER.addURI(AUTHORITY, CinemaItem.CONTENT_PATH + "/#", CINEMA_ITEM_TYPE);
    }

    @Override
    public boolean onCreate() {
        this.db = new DatabaseHelper(getContext()).getWritableDatabase();
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
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DatabaseHelper.CINEMA_TABLE);
        if (TextUtils.isEmpty(sortOrder)) {
            sortOrder = CinemaItem.SORT_ORDER_DEFAULT;
        }
        switch (URI_MATCHER.match(uri)) {
            case CINEMA_LIST_TYPE:
                break;
            case CINEMA_ITEM_TYPE:
                builder.appendWhere(CinemaItem._ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case CINEMA_LIST_TYPE:
                return CinemaItem.CONTENT_TYPE;
            case CINEMA_ITEM_TYPE:
                return CinemaItem.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (URI_MATCHER.match(uri) != CINEMA_ITEM_TYPE) {
            throw new IllegalArgumentException("Unsupported URI for insertion: " + uri);
        }
        long id = db.insert(DatabaseHelper.CINEMA_TABLE, null, values);
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(itemUri, null);
            return itemUri;
        }
        throw new AppException("Problem while inserting into " + DatabaseHelper.CINEMA_TABLE + ", uri: " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int delCount = 0;
        switch (URI_MATCHER.match(uri)) {
            case CINEMA_LIST_TYPE:
                delCount = db.delete(DatabaseHelper.CINEMA_TABLE, selection, selectionArgs);
                break;
            case CINEMA_ITEM_TYPE:
                String id = uri.getLastPathSegment();
                String where = CinemaItem._ID + " = " + id;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }
                delCount = db.delete(DatabaseHelper.CINEMA_TABLE, where, selectionArgs);
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
            case CINEMA_LIST_TYPE:
                updateCount = db.update(DatabaseHelper.CINEMA_TABLE, values, selection, selectionArgs);
                break;
            case CINEMA_ITEM_TYPE:
                String id = uri.getLastPathSegment();
                String where = CinemaItem._ID + " = " + id;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }
                updateCount = db.update(DatabaseHelper.CINEMA_TABLE, values, where, selectionArgs);
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
