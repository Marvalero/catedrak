package com.valero.catedrak.catedrapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by valero on 27/11/2017.
 */

public class CatedrappDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "catedrapp.db";
    private static final int DATABASE_VERSION = 1;

    public CatedrappDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + CatedrappContract.ListEntry.TABLE_NAME + " (" +
                CatedrappContract.ListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CatedrappContract.ListEntry.COLUMN_LIST_NAME + " TEXT NOT NULL, " +
                CatedrappContract.ListEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CatedrappContract.ListEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
