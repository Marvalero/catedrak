package com.valero.catedrak.catedrapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.valero.catedrak.catedrapp.data.CatedrappContract;
import com.valero.catedrak.catedrapp.models.List;

import java.util.ArrayList;

/**
 * Created by valero on 27/11/2017.
 */

public class ListRecyclerAdapter extends RecyclerView.Adapter<ListRecyclerAdapter.ListViewHolder> {
    private Cursor mCursor;

    public ListRecyclerAdapter(Cursor cursor) {
        this.mCursor = cursor;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        Log.i("Adapter", "onCreateViewHolder");
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_rv_item, parent, false);
        ListViewHolder listViewHolder = new ListViewHolder(view);
        return listViewHolder;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position))
            return;

        long id = mCursor.getLong(mCursor.getColumnIndex(CatedrappContract.ListEntry._ID));
        String name = mCursor.getString(mCursor.getColumnIndex(CatedrappContract.ListEntry.COLUMN_LIST_NAME));
        int timestamp = mCursor.getInt(mCursor.getColumnIndex(CatedrappContract.ListEntry.COLUMN_TIMESTAMP));

        holder.nameTextView.setText(name);
        holder.timestampTextView.setText("timestamp: " + timestamp);
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameTextView;
        public TextView timestampTextView;

        public ListViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.lisit_tv_name);
            timestampTextView = (TextView) itemView.findViewById(R.id.lisit_tv_timestamp);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent startSettingsActivity = new Intent(context, ListActivity.class);
            context.startActivity(startSettingsActivity);
        }
    }
}
