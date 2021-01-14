package com.example.task2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DbAdapter {

    private SQLiteDatabase database;
    private DbSettings dbSettings;

    public DbAdapter(Context context)
    {
        dbSettings = new DbSettings(context.getApplicationContext());
    }

    public DbAdapter Open()
    {
        database = dbSettings.getWritableDatabase();
        return this;
    }

    public void Close()
    {
        dbSettings.close();
    }

    private Cursor getAllEntries()
    {
        String[] columns = new String[] {DbSettings.COLUMN_ID, DbSettings.COLUMN_COLOR,
                DbSettings.COLUMN_NAME, DbSettings.COLUMN_PREPARATION, DbSettings.COLUMN_WORKING_TIME,
                DbSettings.COLUMN_REST, DbSettings.COLUMN_CYCLES, DbSettings.COLUMN_SETS,
                DbSettings.COLUMN_REST_BETWEEN_SETS};
        return  database.query(DbSettings.TABLE, columns, null, null,
                null, null, null);
    }

    public List<TimerSequence> GetItems()
    {
        ArrayList<TimerSequence> sequences = new ArrayList<>();
        Cursor cursor = getAllEntries();
        if(cursor.moveToFirst())
        {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DbSettings.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(DbSettings.COLUMN_NAME));
                int preparations = cursor.getInt(cursor.getColumnIndex(DbSettings.COLUMN_PREPARATION));
                int workingTime = cursor.getInt(cursor.getColumnIndex(DbSettings.COLUMN_WORKING_TIME));
                int rest = cursor.getInt(cursor.getColumnIndex(DbSettings.COLUMN_REST));
                int cycles = cursor.getInt(cursor.getColumnIndex(DbSettings.COLUMN_CYCLES));
                int sets = cursor.getInt(cursor.getColumnIndex(DbSettings.COLUMN_SETS));
                int restBetweenSets = cursor.getInt(cursor.getColumnIndex(DbSettings.COLUMN_REST_BETWEEN_SETS));
                int color = cursor.getInt(cursor.getColumnIndex(DbSettings.COLUMN_COLOR));
                sequences.add(new TimerSequence(id, name, preparations, workingTime, rest, cycles, sets, restBetweenSets, color));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return sequences;
    }

    public long GetAmount()
    {
        return DatabaseUtils.queryNumEntries(database, DbSettings.TABLE);
    }

    public TimerSequence GetItem(int id)
    {
        TimerSequence sequence = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DbSettings.TABLE, DbSettings.COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst())
        {
            int color = cursor.getInt(cursor.getColumnIndex(DbSettings.COLUMN_COLOR));
            String name = cursor.getString(cursor.getColumnIndex(DbSettings.COLUMN_NAME));
            int preparation = cursor.getInt(cursor.getColumnIndex(DbSettings.COLUMN_PREPARATION));
            int workingTime = cursor.getInt(cursor.getColumnIndex(DbSettings.COLUMN_WORKING_TIME));
            int rest = cursor.getInt(cursor.getColumnIndex(DbSettings.COLUMN_REST));
            int cycles = cursor.getInt(cursor.getColumnIndex(DbSettings.COLUMN_CYCLES));
            int sets = cursor.getInt(cursor.getColumnIndex(DbSettings.COLUMN_SETS));
            int restBetweenSets = cursor.getInt(cursor.getColumnIndex(DbSettings.COLUMN_REST_BETWEEN_SETS));
            sequence = new TimerSequence(id, name, preparation, workingTime, rest, cycles, sets, restBetweenSets, color);
        }
        cursor.close();
        return sequence;
    }

    public long InsertIntoDb(TimerSequence sequence)
    {

        ContentValues cv = new ContentValues();
        cv.put(DbSettings.COLUMN_COLOR, sequence.color);
        cv.put(DbSettings.COLUMN_NAME, sequence.name);
        cv.put(DbSettings.COLUMN_PREPARATION, sequence.preparation);
        cv.put(DbSettings.COLUMN_WORKING_TIME, sequence.workingTime);
        cv.put(DbSettings.COLUMN_REST, sequence.rest);
        cv.put(DbSettings.COLUMN_CYCLES, sequence.cycles);
        cv.put(DbSettings.COLUMN_SETS, sequence.sets);
        cv.put(DbSettings.COLUMN_REST_BETWEEN_SETS, sequence.restBetweenSets);
        return  database.insert(DbSettings.TABLE, null, cv);
    }

    public long DeleteItem(int id)
    {

        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        return database.delete(DbSettings.TABLE, whereClause, whereArgs);
    }

    public long UpdateDb(TimerSequence sequence)
    {
        String whereClause = DbSettings.COLUMN_ID + "=" + String.valueOf(sequence.id);
        ContentValues cv = new ContentValues();
        cv.put(DbSettings.COLUMN_COLOR, sequence.color);
        cv.put(DbSettings.COLUMN_NAME, sequence.name);
        cv.put(DbSettings.COLUMN_PREPARATION, sequence.preparation);
        cv.put(DbSettings.COLUMN_WORKING_TIME, sequence.workingTime);
        cv.put(DbSettings.COLUMN_REST, sequence.rest);
        cv.put(DbSettings.COLUMN_CYCLES, sequence.cycles);
        cv.put(DbSettings.COLUMN_SETS, sequence.sets);
        cv.put(DbSettings.COLUMN_REST_BETWEEN_SETS, sequence.restBetweenSets);
        return database.update(DbSettings.TABLE, cv, whereClause, null);
    }

    public void ClearDb()
    {
        dbSettings.onUpgrade(database, 0, 0);
    }
}
