package com.tnt.scoreboard.dataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.tnt.scoreboard.models.Player;

public class PlayerDAO extends BaseDAO<Player> {

    public PlayerDAO(Context context) {
        super(context, Player.TABLE_NAME, new String[]{
                Player.COLUMN_ID,
                Player.COLUMN_GAME_ID,
                Player.COLUMN_NAME,
                Player.COLUMN_SCORE,
                Player.COLUMN_COLOR
        });
    }

    protected ContentValues baseToValues(Player player) {
        ContentValues values = new ContentValues();
        values.put(Player.COLUMN_GAME_ID, player.getGameId());
        values.put(Player.COLUMN_NAME, player.getName());
        values.put(Player.COLUMN_SCORE, player.getScore());
        values.put(Player.COLUMN_COLOR, player.getColor());
        return values;
    }

    @Override
    protected ContentValues baseToValues(Player player, String... selections) {
        ContentValues values = new ContentValues();
        for (String s : selections) {
            switch (s) {
                case Player.COLUMN_GAME_ID:
                    values.put(Player.COLUMN_GAME_ID, player.getGameId());
                    break;
                case Player.COLUMN_NAME:
                    values.put(Player.COLUMN_NAME, player.getName());
                    break;
                case Player.COLUMN_SCORE:
                    values.put(Player.COLUMN_SCORE, player.getScore());
                    break;
                case Player.COLUMN_COLOR:
                    values.put(Player.COLUMN_COLOR, player.getColor());
                    break;
            }
        }
        return values;
    }

    protected Player cursorToBase(Cursor cursor) {
        return new Player(
                cursor.getLong(cursor.getColumnIndexOrThrow(Player.COLUMN_ID)),
                cursor.getLong(cursor.getColumnIndexOrThrow(Player.COLUMN_GAME_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(Player.COLUMN_NAME)),
                cursor.getLong(cursor.getColumnIndexOrThrow(Player.COLUMN_SCORE)),
                cursor.getLong(cursor.getColumnIndexOrThrow(Player.COLUMN_COLOR))
        );
    }
}
