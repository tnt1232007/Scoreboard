package com.tnt.scoreboard.dataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tnt.scoreboard.models.Base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class BaseDAO<T extends Base> {

    private static final String TAG = "BaseDAO";
    protected SQLiteDatabase db;
    protected SQLiteHelper daoHelper;
    private String tableName;
    private String[] allColumns;

    public BaseDAO(Context context, String tableName, String[] allColumns) {
        daoHelper = new SQLiteHelper(context);
        this.tableName = tableName;
        this.allColumns = allColumns;
    }

    public void open() {
        db = daoHelper.getWritableDatabase();
        db.beginTransaction();
    }

    public void close() {
        db.setTransactionSuccessful();
        db.endTransaction();
        daoHelper.close();
    }

    public T create(T base) {
        long insertId = db.insert(tableName, null, baseToValues(base));
        Cursor cursor = db.query(tableName, allColumns,
                Base.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        base = cursorToBase(cursor);
        cursor.close();
        return base;
    }

    public T get(long selectId) {
        Cursor cursor = db.query(tableName, allColumns,
                Base.COLUMN_ID + " = " + selectId, null, null, null, null);
        if (cursor.getCount() == 0) return null;
        cursor.moveToFirst();
        T base = cursorToBase(cursor);
        cursor.close();
        return base;
    }

    public List<T> get(String where) {
        return get(where, null, null, null, null, null);
    }

    public List<T> get(String selection, String[] selectionArgs, String groupBy,
                       String having, String orderBy, String limit) {
        List<T> bases = new ArrayList<>();
        Cursor cursor = db.query(tableName, allColumns, selection, selectionArgs, groupBy, having, orderBy, limit);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            bases.add(cursorToBase(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return bases;
    }

    public void update(T base) {
        db.update(tableName, baseToValues(base), Base.COLUMN_ID + " = " + base.getId(), null);
    }

    public void delete(long deleteId) {
        db.delete(tableName, Base.COLUMN_ID + " = " + deleteId, null);
    }

    protected abstract ContentValues baseToValues(T base);

    protected abstract T cursorToBase(Cursor cursor);

    protected Date cursorGetDate(Cursor cursor, int columnIndex) {
        String sqlDate = cursor.getString(columnIndex);
        SimpleDateFormat dateFormat = new SimpleDateFormat(SQLiteHelper.SQLITE_DATE_FORMAT);
        try {
            return dateFormat.parse(sqlDate);
        } catch (ParseException e) {
            Log.e(TAG, "Parsing datetime failed", e);
            return new Date(0);
        }
    }
}
