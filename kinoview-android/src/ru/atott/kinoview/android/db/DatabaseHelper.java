package ru.atott.kinoview.android.db;

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
    public static final String CINEMA_COLUMN_VENDOR_ID = "vendorId";

    private static final String DATABASE_NAME = "kinoview.db";
    private static final int DATABASE_VERSION = 12;

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
                "address text not null, " +
                "vendorId integer not null )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS cinema");
        onCreate(db);
    }
}
