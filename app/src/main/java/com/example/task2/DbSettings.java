package com.example.task2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbSettings extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "timers.db";
    private static final int SCHEMA = 1;
    static final String TABLE = "timers";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_COLOR = "color";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PREPARATION = "preparation";
    public static final String COLUMN_WORKING_TIME = "workintTime";
    public static final String COLUMN_REST = "rest";
    public static final String COLUMN_CYCLES = "cycles";
    public static final String COLUMN_SETS = "sets";
    public static final String COLUMN_REST_BETWEEN_SETS = "restBetweenSets";


    public DbSettings(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        db.execSQL("CREATE TABLE " + TABLE + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_COLOR
                + " INTEGER, " + COLUMN_NAME
                + " TEXT, " + COLUMN_PREPARATION
                + " INTEGER, " + COLUMN_WORKING_TIME
                + " INTEGER, " + COLUMN_REST
                + " INTEGER, " + COLUMN_CYCLES
                + " INTEGER, " + COLUMN_SETS
                + " INTEGER, " + COLUMN_REST_BETWEEN_SETS + " INTEGER);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
}
