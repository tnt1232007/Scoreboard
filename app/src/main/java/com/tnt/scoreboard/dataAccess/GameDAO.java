package com.tnt.scoreboard.dataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.tnt.scoreboard.models.Game;


public class GameDAO extends BaseDAO<Game> {

    public GameDAO(Context context) {
        super(context, Game.TABLE_NAME, new String[]{
                Game.COLUMN_ID,
                Game.COLUMN_CREATED_DATE,
                Game.COLUMN_NUMBER_OF_PLAYERS,
                Game.COLUMN_CURRENT_ROUND_NUMBER,
                Game.COLUMN_STATE
        });
    }

    @Override
    protected ContentValues baseToValues(Game game) {
        ContentValues values = new ContentValues();
        values.put(Game.COLUMN_NUMBER_OF_PLAYERS, game.getNumberOfPlayers());
        values.put(Game.COLUMN_CURRENT_ROUND_NUMBER, game.getCurrentRoundNumber());
        values.put(Game.COLUMN_STATE, game.getState().ordinal());
        return values;
    }

    @Override
    protected Game cursorToBase(Cursor cursor) {
        return new Game(
                cursor.getLong(cursor.getColumnIndexOrThrow(Game.COLUMN_ID)),
                cursor.getLong(cursor.getColumnIndexOrThrow(Game.COLUMN_NUMBER_OF_PLAYERS)),
                cursor.getLong(cursor.getColumnIndexOrThrow(Game.COLUMN_CURRENT_ROUND_NUMBER)),
                cursorGetDate(cursor, cursor.getColumnIndexOrThrow(Game.COLUMN_CREATED_DATE)),
                Game.State.values()[cursor.getInt(cursor.getColumnIndex(Game.COLUMN_STATE))]);
    }
}
