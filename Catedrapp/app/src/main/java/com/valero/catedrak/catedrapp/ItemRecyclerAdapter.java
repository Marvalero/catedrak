package com.valero.catedrak.catedrapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.valero.catedrak.catedrapp.data.CatedrappContract;

/**
 * Created by valero on 27/11/2017.
 */

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemViewHolder> {
    private Cursor mCursor;
    private ProgressBar mLoadingIndicator;
    final private ListItemListener mOnClickListener;

    public interface ListItemListener {
        void onCompletedItem(View view);
        void onUndoCompletedItem(View view);
    }

    public ItemRecyclerAdapter(Cursor cursor, ListItemListener listener) {
        this.mCursor = cursor;
        mOnClickListener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.listitem_rv_item, parent, false);
        ItemViewHolder listViewHolder = new ItemViewHolder(view);
        return listViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position))
            return;

        long id = mCursor.getLong(mCursor.getColumnIndex(CatedrappContract.ItemListEntry._ID));
        String name = mCursor.getString(mCursor.getColumnIndex(CatedrappContract.ItemListEntry.COLUMN_ITEM_NAME));
        String identifier = mCursor.getString(mCursor.getColumnIndex(CatedrappContract.ItemListEntry.COLUMN_IDENTIFIER));
        String notes = mCursor.getString(mCursor.getColumnIndex(CatedrappContract.ItemListEntry.COLUMN_NOTES));
        String completedAt = mCursor.getString(mCursor.getColumnIndex(CatedrappContract.ItemListEntry.COLUMN_COMPLETED_AT));

        holder.nameTextView.setText(name);
        holder.identifierTextView.setText(identifier);
        holder.notesTextView.setText(notes);
        holder.itemView.setTag(id);
        if(completedAt != null && completedAt != "") {
            holder.completedCheckBox.setChecked(true);
            holder.nameTextView.setPaintFlags(holder.nameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameTextView;
        public TextView identifierTextView;
        public TextView notesTextView;
        public CheckBox completedCheckBox;

        public ItemViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.tv_name);
            identifierTextView = (TextView) itemView.findViewById(R.id.tv_indentifier);
            notesTextView = (TextView) itemView.findViewById(R.id.tv_notes);
            completedCheckBox = (CheckBox) itemView.findViewById(R.id.cb_completed);
            completedCheckBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(completedCheckBox.isChecked()) {
                nameTextView.setPaintFlags(nameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mOnClickListener.onCompletedItem(itemView);
            } else {
                nameTextView.setPaintFlags(nameTextView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                mOnClickListener.onUndoCompletedItem(itemView);
            }
        }
    }
}
