package com.valero.catedrak.catedrapp.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.valero.catedrak.catedrapp.data.CatedrappContract;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by valero on 29/11/2017.
 */

public class ListDatabase {
    public static long addLists(SQLiteDatabase mDb, String listName) {
        ContentValues cv = new ContentValues();
        cv.put(CatedrappContract.ListEntry.COLUMN_LIST_NAME, listName);
        return mDb.insert(CatedrappContract.ListEntry.TABLE_NAME, null, cv);
    }

    public static long addItem(SQLiteDatabase mDb, String itName, String itIdentifier, String itNotes, long listId) {
        ContentValues cv = new ContentValues();
        cv.put(CatedrappContract.ItemListEntry.COLUMN_ITEM_NAME, itName);
        cv.put(CatedrappContract.ItemListEntry.COLUMN_IDENTIFIER, itIdentifier);
        cv.put(CatedrappContract.ItemListEntry.COLUMN_LIST_ID, listId);
        cv.put(CatedrappContract.ItemListEntry.COLUMN_NOTES, itNotes);
        return mDb.insert(CatedrappContract.ItemListEntry.TABLE_NAME, null, cv);
    }

    public static long addItem(SQLiteDatabase mDb, String itName, String itIdentifier, String itLastName, String itNotes, long listId) {
        return addItem(mDb, itLastName + ", " + itName, itIdentifier, itNotes, listId);
    }

    public static void deleteListAndItems(SQLiteDatabase mDb, long listId) {
        mDb.delete(CatedrappContract.ItemListEntry.TABLE_NAME, CatedrappContract.ItemListEntry.COLUMN_LIST_ID + "=" + listId, null);
        mDb.delete(CatedrappContract.ListEntry.TABLE_NAME, CatedrappContract.ListEntry._ID + "=" + listId, null);
    }

    public static void deleteItem(SQLiteDatabase mDb, long itemId) {
        mDb.delete(CatedrappContract.ItemListEntry.TABLE_NAME, CatedrappContract.ItemListEntry._ID + "=" + itemId, null);
    }

    public static Cursor getAllLists(SQLiteDatabase mDb) {
        return mDb.query(
                CatedrappContract.ListEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                CatedrappContract.ListEntry.COLUMN_TIMESTAMP
        );
    }

    public static Cursor getAllItems(SQLiteDatabase dbList, long listId) {
        String whereClause = CatedrappContract.ItemListEntry.COLUMN_LIST_ID + " = " + listId;
        return dbList.query(
                CatedrappContract.ItemListEntry.TABLE_NAME,
                null,
                whereClause,
                null,
                null,
                null,
                CatedrappContract.ItemListEntry.COLUMN_ITEM_NAME
        );
    }

    public static void completeItem(SQLiteDatabase dbList, long itemId) {
        ContentValues cv = new ContentValues();
        cv.put(CatedrappContract.ItemListEntry.COLUMN_COMPLETED_AT,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        dbList.update(CatedrappContract.ItemListEntry.TABLE_NAME, cv,
                CatedrappContract.ItemListEntry._ID +" = ?", new String[]{String.valueOf(itemId)});
    }

    public static void undoCompleteItem(SQLiteDatabase dbList, long itemId) {
        ContentValues cv = new ContentValues();
        cv.putNull(CatedrappContract.ItemListEntry.COLUMN_COMPLETED_AT);

        dbList.update(CatedrappContract.ItemListEntry.TABLE_NAME, cv,
                CatedrappContract.ItemListEntry._ID +" = ?", new String[]{String.valueOf(itemId)});
    }
}
