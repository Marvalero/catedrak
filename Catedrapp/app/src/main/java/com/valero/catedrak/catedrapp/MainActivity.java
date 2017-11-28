package com.valero.catedrak.catedrapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.valero.catedrak.catedrapp.data.CatedrappContract;
import com.valero.catedrak.catedrapp.data.CatedrappDbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListRecyclerAdapter mAdapter;
    private RecyclerView mListRecyclerView;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListRecyclerView = (RecyclerView) findViewById(R.id.main_rv_lists);

        CatedrappDbHelper dbHelper = new CatedrappDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        Cursor cursor = getAllLists();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new ListRecyclerAdapter(cursor);
        mListRecyclerView.setAdapter(mAdapter);
    }

    private Cursor getAllLists() {
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

    public void addNewList(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("List Name");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String listName = input.getText().toString();
                ContentValues cv = new ContentValues();
                cv.put(CatedrappContract.ListEntry.COLUMN_LIST_NAME, listName);
                mDb.insert(CatedrappContract.ListEntry.TABLE_NAME, null, cv);
                mAdapter.swapCursor(getAllLists());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
