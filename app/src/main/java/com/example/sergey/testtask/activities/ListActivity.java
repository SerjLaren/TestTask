package com.example.sergey.testtask.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.sergey.testtask.R;
import com.example.sergey.testtask.adapters.RecyclerListAdapter;
import com.example.sergey.testtask.helpers.DatabaseHelper;

import java.util.ArrayList;

public class ListActivity extends BaseActivity implements RecyclerListAdapter.onRVItemClickListener {

    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter myAdapter;
    private Cursor c;
    private ArrayList myData;
    private SQLiteDatabase db;
    private TextView tvNoData;
    private Intent intentEditItem;
    private static final String TABLE_NAME = "myListTable", DB_ITEM = "itemName", DB_ITEM_ID = "_id", ID_SELECTED_ITEM = "item_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initDB();
        initValues();
        initViews();
    }

    @Override
    void initValues() {

    }

    @Override
    void initViews() {
        myRecyclerView = (RecyclerView) findViewById(R.id.recyclerList);
        registerForContextMenu(myRecyclerView);
        RecyclerView.LayoutManager myLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(myLayoutManager);
        myAdapter = new RecyclerListAdapter(this, myData, myRecyclerView);
        myRecyclerView.setAdapter(myAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.toolbarBtnAdd) {
            intentEditItem = new Intent(this, ListEditActivity.class);
            startActivity(intentEditItem);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 1: // edit
                int positionInDataset = ((RecyclerListAdapter)myRecyclerView.getAdapter()).getPosition();
                intentEditItem = new Intent(this, ListEditActivity.class);
                intentEditItem.putExtra(ID_SELECTED_ITEM, positionInDataset);
                startActivity(intentEditItem);
                break;
            case 2: // delete
                positionInDataset = ((RecyclerListAdapter)myRecyclerView.getAdapter()).getPosition();
                c = db.query(TABLE_NAME, null, null, null, null, null, null);
                c.moveToPosition(positionInDataset);
                int idSelectedItem = c.getInt(c.getColumnIndex(DB_ITEM_ID));
                deleteElement(idSelectedItem, positionInDataset);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {
        intentEditItem = new Intent(this, ListEditActivity.class);
        intentEditItem.putExtra(ID_SELECTED_ITEM, position);
        startActivity(intentEditItem);
    }

    private void initDB() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        c = db.query(TABLE_NAME, null, null, null, null, null, null);
        getDataFromDB();
    }

    private void getDataFromDB() {
        myData = new ArrayList();
        tvNoData = (TextView) findViewById(R.id.tv_no_data);
        if (c.moveToFirst()) {
            tvNoData.setVisibility(View.INVISIBLE);
            do {
                myData.add(c.getString(c.getColumnIndex(DB_ITEM)));
            } while (c.moveToNext());
        } else {
            tvNoData.setVisibility(View.VISIBLE);
        }
    }

    private void deleteElement(int id, int position) {
        db.delete(TABLE_NAME, DB_ITEM_ID + "=" + String.valueOf(id), null);
        myData.remove(position);
        if (myData.size() == 0) {
            tvNoData.setVisibility(View.VISIBLE);
        } else {
            tvNoData.setVisibility(View.INVISIBLE);
        }
        myAdapter.notifyItemRemoved(position);
    }
}
