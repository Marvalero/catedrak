package com.valero.catedrak.catedrapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.valero.catedrak.catedrapp.data.CatedrappContract;
import com.valero.catedrak.catedrapp.data.CatedrappDbHelper;

public class ListActivity extends AppCompatActivity {
    public static final String LIST_ID_KEY = "list_id_key";
    private ItemRecyclerAdapter mAdapter;
    private RecyclerView mListRecyclerView;
    private TextView mListNameTextView;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mListNameTextView = (TextView) findViewById(R.id.tv_list_name);
        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra(LIST_ID_KEY) && intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            Long idEntered = intentThatStartedThisActivity.getLongExtra(LIST_ID_KEY, 0);
            String nameEntered = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
            mListNameTextView.setText(nameEntered + " Items");

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
