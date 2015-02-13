package com.tnt.scoreboard.dataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.tnt.scoreboard.models.Game;

public class GameDAO extends BaseDAO<Game> {

    public GameDAO(Context context) {
        super(context, Game.TABLE_NAME, new String[]{
                Game.COLUMN_ID,
                Game.COLUMN_TITLE,
                Game.COLUMN_NUMBER_OF_PLAYERS,
                Game.COLUMN_NUMBER_OF_ROUNDS,
                Game.COLUMN_ENDING_SCORE,
                Game.COLUMN_FIRST_TO_WIN,
                Game.COLUMN_INFINITE,
                Game.COLUMN_CREATED_DATE,
                Game.COLUMN_STATE
        });
    }

    @Override
    protected ContentValues baseToValues(Game game) {
        ContentValues values = new ContentValues();
        values.put(Game.COLUMN_TITLE, game.getTitle());
        values.put(Game.COLUMN_NUMBER_OF_PLAYERS, game.getNumberOfPlayers());
        values.put(Game.COLUMN_NUMBER_OF_ROUNDS, game.getNumberOfRounds());
        values.put(Game.COLUMN_ENDING_SCORE, game.getEndingScore());
        values.put(Game.COLUMN_FIRST_TO_WIN, game.isFirstToWin() ? 1 : 0);
        values.put(Game.COLUMN_INFINITE, game.isInfinite() ? 1 : 0);
        values.put(Game.COLUMN_STATE, game.getState().ordinal());
        return values;
    }

    @Override
    protected Game cursorToBase(Cursor cursor) {
        return new Game(
                cursor.getLong(cursor.getColumnIndexOrThrow(Game.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(Game.COLUMN_TITLE)),
                cursor.getLong(cursor.getColumnIndexOrThrow(Game.COLUMN_NUMBER_OF_PLAYERS)),
                cursor.getLong(cursor.getColumnIndexOrThrow(Game.COLUMN_NUMBER_OF_ROUNDS)),
                cursor.getLong(cursor.getColumnIndexOrThrow(Game.COLUMN_ENDING_SCORE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(Game.COLUMN_FIRST_TO_WIN)) != 0,
                cursor.getInt(cursor.getColumnIndexOrThrow(Game.COLUMN_INFINITE)) != 0,
                cursorGetDate(cursor, cursor.getColumnIndexOrThrow(Game.COLUMN_CREATED_DATE)),
                Game.State.values()[cursor.getInt(cursor.getColumnIndex(Game.COLUMN_STATE))]);
    }
}
