package com.valero.catedrak.catedrapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.valero.catedrak.catedrapp.data.CatedrappContract;
import com.valero.catedrak.catedrapp.data.CatedrappDbHelper;

public class ListActivity extends AppCompatActivity {
    private ItemRecyclerAdapter mAdapter;
    private RecyclerView mListRecyclerView;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mListRecyclerView = (RecyclerView) findViewById(R.id.main_rv_lists);

        CatedrappDbHelper dbHelper = new CatedrappDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        Cursor cursor = getAllItems();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new ItemRecyclerAdapter(cursor);
        mListRecyclerView.setAdapter(mAdapter);
    }

    private Cursor getAllItems() {
        return mDb.query(
                CatedrappContract.ItemListEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                CatedrappContract.ItemListEntry.COLUMN_ITEM_NAME
        );
    }}
