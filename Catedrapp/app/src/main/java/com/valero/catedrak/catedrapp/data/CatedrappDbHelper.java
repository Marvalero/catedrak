package com.valero.catedrak.catedrapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by valero on 27/11/2017.
 */

public class CatedrappDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "catedrapp.db";
    private static final int DATABASE_VERSION = 2;

    public CatedrappDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_LIST_TABLE = "CREATE TABLE " + CatedrappContract.ListEntry.TABLE_NAME + " (" +
                CatedrappContract.ListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CatedrappContract.ListEntry.COLUMN_LIST_NAME + " TEXT NOT NULL, " +
                CatedrappContract.ListEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_LIST_TABLE);

        final String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + CatedrappContract.ItemListEntry.TABLE_NAME + " (" +
                CatedrappContract.ItemListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CatedrappContract.ItemListEntry.COLUMN_ITEM_NAME + " TEXT NOT NULL, " +
                CatedrappContract.ItemListEntry.COLUMN_COMPLETED_AT + " TIMESTAMP, " +
                CatedrappContract.ItemListEntry.COLUMN_IDENTIFIER + " TEXT, " +
                CatedrappContract.ItemListEntry.COLUMN_NOTES + " TEXT, " +
                CatedrappContract.ItemListEntry.COLUMN_LIST_ID + " TEXT, " +
                " FOREIGN KEY (" + CatedrappContract.ItemListEntry.COLUMN_LIST_ID +
                ") REFERENCES " + CatedrappContract.ListEntry.TABLE_NAME + "(" + CatedrappContract.ListEntry._ID + ")" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CatedrappContract.ListEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
