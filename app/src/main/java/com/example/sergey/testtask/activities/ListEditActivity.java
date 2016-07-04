package com.example.sergey.testtask.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sergey.testtask.R;
import com.example.sergey.testtask.helpers.DatabaseHelper;

public class ListEditActivity extends BaseActivity implements View.OnClickListener {

    private int positionSelectedItem;
    private Cursor c;
    private ContentValues cv;
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private EditText editTextItem;
    private String editTextData, titleAlert, btnAlertPos, btnAlertNeg;
    private Button btnRevert, btnDone;
    private AlertDialog.Builder builderAlert;
    private Toast toast;
    private String emptyDataErr;
    private int idEditedItem;
    private boolean createNewElement = false;
    private static final String TABLE_NAME = "myListTable", DB_ITEM = "itemName", DB_ITEM_ID = "_id", ID_SELECTED_ITEM = "item_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_edit);
        initDB();
        initValues();
        initViews();
    }

    @Override
    void initValues() {
        emptyDataErr = getString(R.string.dataEmptyErr);
        titleAlert = getString(R.string.alertTitle);
        btnAlertPos = getString(R.string.alertBtnPositive);
        btnAlertNeg = getString(R.string.alertBtnNegative);
        positionSelectedItem = getIntent().getIntExtra(ID_SELECTED_ITEM, -1);
        if (positionSelectedItem != -1) {
            createNewElement = false;
        } else {
            createNewElement = true;
        }
    }

    @Override
    void initViews() {
        editTextItem = (EditText) findViewById(R.id.editTextListItem);
        editTextData = editTextItem.getText().toString();
        btnRevert = (Button) findViewById(R.id.btnRevert);
        btnRevert.setOnClickListener(this);
        btnDone = (Button) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);
        if (!createNewElement) {
            c = db.query(TABLE_NAME, null, null, null, null, null, null);
            c.moveToPosition(positionSelectedItem);
            editTextItem.setText(c.getString(c.getColumnIndex(DB_ITEM)));
            editTextData = editTextItem.getText().toString();
        }
    }

    private void initDB() {
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRevert:
                finish();
                break;
            case R.id.btnDone:
                createOrEditElement();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!(editTextItem.getText().toString().equals(editTextData))) {
            runAlertDialog();
        } else {
            finish();
        }
    }

    private void runAlertDialog() {
        builderAlert = new AlertDialog.Builder(this);
        builderAlert.setTitle(titleAlert);
        builderAlert.setPositiveButton(btnAlertPos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createOrEditElement();
            }
        });
        builderAlert.setNegativeButton(btnAlertNeg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builderAlert.setCancelable(true);
        builderAlert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        builderAlert.show();
    }

    private void createOrEditElement() {
        if (createNewElement) {
            if (editTextItem.getText().toString().equals("")) {
                toast = Toast.makeText(getApplicationContext(),
                        emptyDataErr, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                cv = new ContentValues();
                cv.put(DB_ITEM, editTextItem.getText().toString());
                db.insert(TABLE_NAME, null, cv);
                returnToListAct();
            }
        } else {
            if (editTextItem.getText().toString().equals("")) {
                toast = Toast.makeText(getApplicationContext(),
                        emptyDataErr, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                c.moveToPosition(positionSelectedItem);
                idEditedItem = c.getInt(c.getColumnIndex(DB_ITEM_ID));
                cv = new ContentValues();
                cv.put(DB_ITEM, editTextItem.getText().toString());
                db.update(TABLE_NAME, cv, DB_ITEM_ID + "=" + String.valueOf(idEditedItem), null);
                returnToListAct();
            }
        }
    }

    private void returnToListAct() {
        Intent intentList = new Intent(this, ListActivity.class);
        intentList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentList);
    }

}
