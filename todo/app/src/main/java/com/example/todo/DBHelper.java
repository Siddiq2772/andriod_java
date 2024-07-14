package com.example.todo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    public static final String TABLE_NAME = "mytable";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_VARIABLE = "variable";

    // Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create table query
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_VARIABLE + " TEXT" +
                ")";
        db.execSQL(createTableQuery);
    }

    // Upgrade database if needed
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exists and recreate
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to fetch a single variable from the database
    public String getVariable() {
        String variableValue = null;
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {COLUMN_VARIABLE};

        Cursor cursor = db.query(
                TABLE_NAME,        // The table to query
                projection,        // The array of columns to return
                null,              // The columns for the WHERE clause
                null,              // The values for the WHERE clause
                null,              // don't group the rows
                null,              // don't filter by row groups
                null               // The sort order
        );

        // Retrieve the variable value if cursor is not empty
        if (cursor != null && cursor.moveToFirst()) {
            variableValue = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VARIABLE));
            cursor.close();
        }

        return variableValue;
    }
}
