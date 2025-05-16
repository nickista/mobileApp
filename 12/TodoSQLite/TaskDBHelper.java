package com.example.todo2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class TaskDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "tasks";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TEXT = "task_text";
    public static final String COLUMN_CHECKED = "checked";

    public TaskDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TEXT + " TEXT NOT NULL, " +
                COLUMN_CHECKED + " INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertTask(String text) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXT, text);
        values.put(COLUMN_CHECKED, 0);
        db.insert(TABLE_NAME, null, values);
    }

    public ArrayList<MainActivity.TaskItem> getAllTasks() {
        ArrayList<MainActivity.TaskItem> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String text = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEXT));
            int checked = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CHECKED));
            taskList.add(new MainActivity.TaskItem(text, checked == 1));
        }
        cursor.close();
        return taskList;
    }

    public void deleteTask(String text) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_TEXT + " = ?", new String[]{text});
    }
}
