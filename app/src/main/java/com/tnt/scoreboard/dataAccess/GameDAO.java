package com.tnt.scoreboard.dataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.utils.DateTimeUtils;

import org.joda.time.DateTime;

public class GameDAO extends BaseDAO<Game> {

    public GameDAO(Context context) {
        super(context, Game.TABLE_NAME, new String[]{
                Game.COLUMN_ID,
                Game.COLUMN_TITLE,
                Game.COLUMN_NUMBER_OF_PLAYERS,
                Game.COLUMN_NUMBER_OF_ROUNDS,
                Game.COLUMN_STARTING_SCORE,
                Game.COLUMN_ENDING_SCORE,
                Game.COLUMN_STATE,
                Game.COLUMN_CREATED_DATE,
                Game.COLUMN_UPDATED_DATE
        });
    }

    @Override
    protected ContentValues baseToValues(Game game) {
        ContentValues values = new ContentValues();
        values.put(Game.COLUMN_TITLE, game.getTitle());
        values.put(Game.COLUMN_NUMBER_OF_PLAYERS, game.getNumberOfPlayers());
        values.put(Game.COLUMN_NUMBER_OF_ROUNDS, game.getNumberOfRounds());
        values.put(Game.COLUMN_STARTING_SCORE, game.getStartingScore());
        values.put(Game.COLUMN_ENDING_SCORE, game.getEndingScore());
        values.put(Game.COLUMN_STATE, game.getState().ordinal());
        DateTime updatedDate = game.getUpdatedDate();
        if (updatedDate != null)
            values.put(Game.COLUMN_UPDATED_DATE, DateTimeUtils.formatUtc(updatedDate));
        return values;
    }

    @Override
    protected Game cursorToBase(Cursor cursor) {
        return new Game(
                cursor.getLong(cursor.getColumnIndexOrThrow(Game.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(Game.COLUMN_TITLE)),
                cursor.getLong(cursor.getColumnIndexOrThrow(Game.COLUMN_NUMBER_OF_PLAYERS)),
                cursor.getLong(cursor.getColumnIndexOrThrow(Game.COLUMN_NUMBER_OF_ROUNDS)),
                cursor.getLong(cursor.getColumnIndexOrThrow(Game.COLUMN_STARTING_SCORE)),
                cursor.getLong(cursor.getColumnIndexOrThrow(Game.COLUMN_ENDING_SCORE)),
                Game.State.values()[cursor.getInt(cursor.getColumnIndex(Game.COLUMN_STATE))],
                DateTimeUtils.parseLocalDate(
                        cursor.getString(cursor.getColumnIndexOrThrow(Game.COLUMN_CREATED_DATE))),
                DateTimeUtils.parseLocalDate(
                        cursor.getString(cursor.getColumnIndexOrThrow(Game.COLUMN_UPDATED_DATE)))
        );
    }
}
