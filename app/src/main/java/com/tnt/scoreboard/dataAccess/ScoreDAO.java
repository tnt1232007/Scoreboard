package com.tnt.scoreboard.dataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.tnt.scoreboard.models.Score;

public class ScoreDAO extends BaseDAO<Score> {

    public ScoreDAO(Context context) {
        super(context, Score.TABLE_NAME, new String[]{
                Score.COLUMN_ID,
                Score.COLUMN_PLAYER_ID,
                Score.COLUMN_SCORE
        });
    }

    @Override
    protected ContentValues baseToValues(Score score) {
        ContentValues values = new ContentValues();
        values.put(Score.COLUMN_PLAYER_ID, score.getPlayerId());
        values.put(Score.COLUMN_SCORE, score.getScore());
        return values;
    }

    @Override
    protected Score cursorToBase(Cursor cursor) {
        return new Score(
                cursor.getLong(cursor.getColumnIndexOrThrow(Score.COLUMN_ID)),
                cursor.getLong(cursor.getColumnIndexOrThrow(Score.COLUMN_PLAYER_ID)),
                cursor.getLong(cursor.getColumnIndexOrThrow(Score.COLUMN_SCORE))
        );
    }
}
