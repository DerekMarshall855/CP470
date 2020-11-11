package com.example.androidassignments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class ChatDatabaseHelper extends SQLiteOpenHelper {

    protected static final String ACTIVITY_NAME = "ChatDatabaseHelper";
    protected static final String DATABASE_NAME = "Messages.db";
    protected static final int VERSION_NUM = 3;
    protected static final String KEY_ID = "ID";
    public static final String KEY_MESSAGE = "MESSAGE";
    public static final String TABLE_NAME = "Entry";

    private String sqlString;

    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(ACTIVITY_NAME, "Calling onCreate");
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_MESSAGE + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String oldVer = Integer.toString(db.getVersion());
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVersion);
        sqlString = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sqlString);

        onCreate(db);
    }
}
