package com.valero.catedrak.catedrapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.valero.catedrak.catedrapp.data.CatedrappContract;
import com.valero.catedrak.catedrapp.data.CatedrappDbHelper;
import com.valero.catedrak.catedrapp.helper.ListDatabase;
import com.valero.catedrak.catedrapp.helper.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListRecyclerAdapter mAdapter;
    private RecyclerView mListRecyclerView;
    private SQLiteDatabase mDb;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Open chrome://inspect/
        Stetho.initializeWithDefaults(this);

        mListRecyclerView = (RecyclerView) findViewById(R.id.main_rv_lists);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.main_pb_loanding);

        CatedrappDbHelper dbHelper = new CatedrappDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        Cursor cursor = ListDatabase.getAllLists(mDb);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new ListRecyclerAdapter(cursor);
        mListRecyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                onAdapterSwiped((ListRecyclerAdapter.ListViewHolder) viewHolder, direction);
            }

            public void onAdapterSwiped(ListRecyclerAdapter.ListViewHolder viewHolder, int swipeDir) {
                long id = (long) viewHolder.itemView.getTag();
                removeList(id, viewHolder.nameTextView.getText().toString());
            }
        }).attachToRecyclerView(mListRecyclerView);
    }

    public void removeList(long id, String name) {
        final long listId = id;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.lit_do_you_want_to_remove) + " " + name + "?");
        builder.setPositiveButton(R.string.lit_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListDatabase.deleteListAndItems(mDb, listId);
            }
        });
        builder.setNegativeButton(R.string.lit_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        mAdapter.swapCursor(ListDatabase.getAllLists(mDb));
        builder.show();
    }

    public long addList(String listName) {
        long id = ListDatabase.addLists(mDb, listName);
        mAdapter.swapCursor(ListDatabase.getAllLists(mDb));
        return id;
    }

    public void addNewList(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.lit_list_name);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton(R.string.lit_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addList(input.getText().toString());
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
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.maimen_import) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.lit_url);
            final EditText input = new EditText(this);
            input.setText(R.string.uri_servedrak);
            input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
            builder.setView(input);
            builder.setPositiveButton(R.string.lit_import, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String uri = input.getText().toString();
                    URL importListsUrl = Network.buildUrl(uri);
                    new ImportListsTask().execute(importListsUrl);
                }
            });
            builder.setNegativeButton(R.string.lit_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
            return true;
        } else if (id == R.id.maimen_add) {
            addNewList(this);
        }
        return super.onOptionsItemSelected(item);
    }

    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public class ImportListsTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String listsSearchResults = null;
            try {
                listsSearchResults = Network.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return listsSearchResults;
        }

        @Override
        protected void onPostExecute(String listsSearchResults) {
            mLoadingIndicator.setVisibility(View.GONE);
            if (listsSearchResults != null && !listsSearchResults.equals("")) {
                try {
                    JSONArray listsArray = new JSONArray(listsSearchResults);
                    for(int li_index=0; li_index<listsArray.length(); li_index++){
                        JSONObject listJSON = listsArray.getJSONObject(li_index);
                        String name = listJSON.getString("name");
                        long listId = addList(name);

                        JSONArray itemsArray = listJSON.getJSONArray("items");
                        for(int it_index=0; it_index<itemsArray.length(); it_index++) {
                            Log.i("ImportAc", "Importing Array: " + it_index);
                            JSONObject itemJSON = itemsArray.getJSONObject(it_index);
                            String itName = itemJSON.getString("name");
                            String itIdentifier = itemJSON.getString("id");
                            String itLastName = itemJSON.getString("last_name");
                            String itNotes = "Imported from Servedrak";
                            ListDatabase.addItem(mDb, itName, itIdentifier, itLastName, itNotes, listId);
                        }
                        showToastMessage("Imported: " + name);
                    }
                } catch (JSONException e) {
                    showToastMessage("Error parsing response");
                    e.printStackTrace();
                }
            } else {
                showToastMessage("Error: Bad response");
            }
        }
    }
}
