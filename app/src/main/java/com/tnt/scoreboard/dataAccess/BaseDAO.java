package com.tnt.scoreboard.dataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tnt.scoreboard.models.Base;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO<T extends Base> {

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
        db = null;
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
        return get(where, null);
    }

    public List<T> get(String selection, String orderBy) {
        List<T> bases = new ArrayList<>();
        Cursor cursor = db.query(tableName, allColumns, selection, null, null, null, orderBy, null);
        while (cursor.moveToNext()) bases.add(cursorToBase(cursor));
        cursor.close();
        return bases;
    }

    public void update(T base) {
        db.update(tableName, baseToValues(base), Base.COLUMN_ID + " = " + base.getId(), null);
    }

    public void update(T base, String... selections) {
        db.update(tableName, baseToValues(base, selections),
                Base.COLUMN_ID + " = " + base.getId(), null);
    }

    public void delete(long deleteId) {
        db.delete(tableName, Base.COLUMN_ID + " = " + deleteId, null);
    }

    protected abstract ContentValues baseToValues(T base);

    protected abstract ContentValues baseToValues(T base, String... selections);

    protected abstract T cursorToBase(Cursor cursor);
}
