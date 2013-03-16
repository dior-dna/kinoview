package ru.atott.kinoview.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DB extends SQLiteOpenHelper {
    public static interface Cinemas {
        String TABLE_NAME = "cinema";
        String COLUMN_ID = "_id";
        String COLUMN_NAME = "name";
        String COLUMN_DESCRIPTION = "description";
        String COLUMN_IMAGE_URI = "image";
        String COLUMN_ADDRESS = "address";
        String COLUMN_VENDOR_ID = "vendorId";
    }

    public static interface Films {
        String TABLE_NAME = "film";
        String COLUMN_ID = "_id";
        String COLUMN_TITLE = "title";
        String COLUMN_DURATION = "duration";
        String COLUMN_DIRECTOR = "director";
        String COLUMN_EXTERNAL_ID = "eid";
        String COLUMN_ACTORS = "actors";
        String COLUMN_GENRE = "genre";
    }

    public static interface FilmsUpdates {
        String TABLE_NAME = "filmupdate";
        String COLUMN_ID = "_id";
        String COLUMN_DATE = "date";
    }

    private static final String DATABASE_NAME = "kinoview.db";
    private static final int DATABASE_VERSION = 16;

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table cinema ( " +
                "_id integer primary key autoincrement, " +
                "name text not null, " +
                "description text not null, " +
                "image text null, " +
                "address text not null, " +
                "vendorId integer not null )");
        db.execSQL("create table film ( " +
                "_id integer primary key autoincrement, " +
                "title text not null, " +
                "duration integer null, " +
                "director text null, " +
                "eid integer not null, " +
                "actors text null, " +
                "genre text null )");
        db.execSQL("create table filmupdate ( " +
                "_id integer primary key autoincrement, " +
                "date integer null )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS cinema");
        db.execSQL("DROP TABLE IF EXISTS film");
        db.execSQL("DROP TABLE IF EXISTS filmupdate");
        onCreate(db);
    }
}
