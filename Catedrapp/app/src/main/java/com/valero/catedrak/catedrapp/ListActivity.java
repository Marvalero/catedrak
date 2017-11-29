package com.valero.catedrak.catedrapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.valero.catedrak.catedrapp.data.CatedrappContract;
import com.valero.catedrak.catedrapp.data.CatedrappDbHelper;
import com.valero.catedrak.catedrapp.helper.ListDatabase;
import com.valero.catedrak.catedrapp.helper.Network;

import java.net.URL;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    public static final String LIST_ID_KEY = "list_id_key";
    private ItemRecyclerAdapter mAdapter;
    private RecyclerView mListRecyclerView;
    private TextView mListNameTextView;
    private long mListID;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mListNameTextView = (TextView) findViewById(R.id.tv_list_name);
        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra(LIST_ID_KEY) && intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            mListID = intentThatStartedThisActivity.getLongExtra(LIST_ID_KEY, 0);
            String nameEntered = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
            mListNameTextView.setText(nameEntered + " Items");

            mListRecyclerView = (RecyclerView) findViewById(R.id.main_rv_lists);

            CatedrappDbHelper dbHelper = new CatedrappDbHelper(this);
            mDb = dbHelper.getWritableDatabase();
            Cursor cursor = ListDatabase.getAllItems(mDb, mListID);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mListRecyclerView.setLayoutManager(linearLayoutManager);

            mAdapter = new ItemRecyclerAdapter(cursor);
            mListRecyclerView.setAdapter(mAdapter);

            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    onAdapterSwiped((ItemRecyclerAdapter.ItemViewHolder) viewHolder, direction);
                }

                public void onAdapterSwiped(ItemRecyclerAdapter.ItemViewHolder viewHolder, int swipeDir) {
                    long id = (long) viewHolder.itemView.getTag();
                    removeItem(id);
                }
            }).attachToRecyclerView(mListRecyclerView);
        }
    }


    public void removeItem(long itemListId) {
        ListDatabase.deleteItem(mDb, itemListId);
        mAdapter.swapCursor(ListDatabase.getAllItems(mDb, mListID));
    }

    public void addItem(String itemName) {
        ListDatabase.addItem(mDb, itemName, null, null, mListID);
        mAdapter.swapCursor(ListDatabase.getAllItems(mDb, mListID));
    }


    public void addNewItem(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.lit_item_name);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(R.string.lit_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addItem(input.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.lit_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ac_add) {
            addNewItem(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
