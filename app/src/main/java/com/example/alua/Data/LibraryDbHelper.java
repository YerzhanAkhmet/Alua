package com.example.alua.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.alua.Data.LibraryContract.BookCreate;

public class LibraryDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = LibraryDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "library1.db";
    private static final int DATABASE_VERSION = 1;

    public LibraryDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_GUESTS_TABLE = "CREATE TABLE " + BookCreate.TABLE_NAME + " ("
                + BookCreate._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookCreate.COLUMN_NAME + " TEXT NOT NULL, "
                + BookCreate.COLUMN_AUTHOR + " TEXT NOT NULL, "
                + BookCreate.COLUMN_CONTENT + " TEXT NOT NULL, "
                + BookCreate.COLUMN_GENRE + " INTEGER NOT NULL DEFAULT 4, "
                + BookCreate.COLUMN_AVATAR + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_GUESTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }
}
