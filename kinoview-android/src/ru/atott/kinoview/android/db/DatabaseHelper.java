package ru.atott.kinoview.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String CINEMA_TABLE = "cinema";
    public static final String CINEMA_COLUMN_ID = "_id";
    public static final String CINEMA_COLUMN_NAME = "name";
    public static final String CINEMA_COLUMN_DESCRIPTION = "description";
    public static final String CINEMA_COLUMN_IMAGE_URI = "image";
    public static final String CINEMA_COLUMN_ADDRESS = "address";

    private static final String DATABASE_NAME = "kinoview.db";
    private static final int DATABASE_VERSION = 4;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table cinema ( " +
                "_id integer primary key autoincrement, " +
                "name text not null, " +
                "description text not null, " +
                "image text null, " +
                "address text not null )");
        addCinema(db, "KINOMAX");
        addCinema(db, "KINOMAX 2");
        addCinema(db, "KINOMAX 3");
        addCinema(db, "KINOMAX 4");
        addCinema(db, "KINOMAX 5");
        addCinema(db, "KINOMAX 6");
        addCinema(db, "KINOMAX 7");
        addCinema(db, "KINOMAX 8");
        addCinema(db, "KINOMAX 9");
        addCinema(db, "KINOMAX 10");
        addCinema(db, "KINOMAX 11");
        addCinema(db, "KINOMAX 12");
        addCinema(db, "KINOMAX 13");
        addCinema(db, "KINOMAX 14");
        addCinema(db, "KINOMAX 15");
        addCinema(db, "KINOMAX 16");
        addCinema(db, "KINOMAX 17");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS cinema");
        onCreate(db);
    }

    private void addCinema(SQLiteDatabase db, String name) {
        ContentValues values = new ContentValues();
        values.put(CINEMA_COLUMN_NAME, name);
        values.put(CINEMA_COLUMN_DESCRIPTION, name + " description");
        values.put(CINEMA_COLUMN_ADDRESS, name + " address");
        db.insert(CINEMA_TABLE, null, values);
    }
}
