package com.tnt.scoreboard.models;

public abstract class Base {

    public static final String COLUMN_ID = "_id";
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
