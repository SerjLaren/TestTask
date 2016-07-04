package com.example.sergey.testtask.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sergey.testtask.R;

import java.util.ArrayList;

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ViewHolder> {

    private ArrayList dataSet;
    private Context ctx;
    private int position;
    private RecyclerView myRV;
    private static String ctxMenuEdit, ctxMenuDelete;

    private final View.OnClickListener mOnclick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            position = myRV.getChildAdapterPosition(v);
            onRVItemClickListener listener = (onRVItemClickListener) ctx;
            listener.onItemClick(position);

        }
    };

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public interface onRVItemClickListener {
        public void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView itemTextView;
        public ImageView itemImageView;
        public CheckBox itemCheckBox;

        public ViewHolder(View v) {
            super(v);
            itemImageView = (ImageView) v.findViewById(R.id.itemImageView);
            itemImageView.setBackgroundResource(R.drawable.list_item_1);
            itemTextView = (TextView) v.findViewById(R.id.itemTextView);
            itemCheckBox = (CheckBox) v.findViewById(R.id.checkBoxItem);
            v.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, 1,
                    Menu.NONE, ctxMenuEdit);
            menu.add(Menu.NONE, 2,
                    Menu.NONE, ctxMenuDelete);
        }
    }

    public RecyclerListAdapter(Context context, ArrayList dataSet, RecyclerView rv) {
        ctx = context;
        this.dataSet = dataSet;
        myRV = rv;
        ctxMenuDelete = ctx.getString(R.string.ctxMenuDelete);
        ctxMenuEdit = ctx.getString(R.string.ctxMenuEdit);
    }

    @Override
    public RecyclerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_rv_item, parent, false);
        v.setOnClickListener(mOnclick);
        v.setOnLongClickListener(null);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemTextView.setText(dataSet.get(position).toString());
        holder.itemCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.itemImageView.setBackgroundResource(R.drawable.list_item_2);
                } else {
                    holder.itemImageView.setBackgroundResource(R.drawable.list_item_1);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getPosition());
                return false;
            }
        });
    }

    public void updateRV(ArrayList data) {
        dataSet = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
