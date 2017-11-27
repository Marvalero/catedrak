package com.valero.catedrak.catedrapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.valero.catedrak.catedrapp.models.List;

import java.util.ArrayList;

/**
 * Created by valero on 27/11/2017.
 */

public class ListRecyclerAdapter extends RecyclerView.Adapter<ListRecyclerAdapter.ListViewHolder> {

    public ListRecyclerAdapter() {
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
        holder.nameTextView.setText("List " + position + ".");
        holder.timestampTextView.setText("timestamp");
    }

    @Override
    public int getItemCount() {
        return 10;
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
