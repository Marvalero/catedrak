package com.valero.catedrak.catedrapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.valero.catedrak.catedrapp.data.CatedrappDbHelper;
import com.valero.catedrak.catedrapp.helper.ListDatabase;

public class ItemActivity extends AppCompatActivity {
    public static final String ITEM_ID_KEY = "item_id_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final AutoCompleteTextView nameAutocomplete = (AutoCompleteTextView) findViewById(R.id.atc_name);
        final AutoCompleteTextView identifierAutocomplete = (AutoCompleteTextView) findViewById(R.id.atc_identifier);
        final AutoCompleteTextView noteAutocomplete = (AutoCompleteTextView) findViewById(R.id.atc_note);

        Button saveButton = (Button) findViewById(R.id.bt_save);
        Button cancelButton = (Button) findViewById(R.id.bt_cancel);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra(ITEM_ID_KEY)) {
            final long itemID = intentThatStartedThisActivity.getLongExtra(ITEM_ID_KEY, 0);
            CatedrappDbHelper dbHelper = new CatedrappDbHelper(this);
            final SQLiteDatabase itemsDb = dbHelper.getWritableDatabase();

            String[] item = ListDatabase.findItemById(itemsDb, itemID);

            nameAutocomplete.setText(item[0]);
            identifierAutocomplete.setText(item[1]);
            noteAutocomplete.setText(item[2]);

            final Activity itemActivity = this;

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ListDatabase.updateItem(itemsDb,
                            itemID,
                            nameAutocomplete.getText().toString(),
                            identifierAutocomplete.getText().toString(),
                            noteAutocomplete.getText().toString());
                    itemActivity.finish();
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemActivity.finish();
                }
            });
        }


    }
}
