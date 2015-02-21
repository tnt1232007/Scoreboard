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
                Score.COLUMN_ROUND_NUMBER,
                Score.COLUMN_CURRENT_SCORE,
                Score.COLUMN_SCORE
        });
    }

    @Override
    protected ContentValues baseToValues(Score score) {
        ContentValues values = new ContentValues();
        values.put(Score.COLUMN_PLAYER_ID, score.getPlayerId());
        values.put(Score.COLUMN_ROUND_NUMBER, score.getRoundNumber());
        values.put(Score.COLUMN_CURRENT_SCORE, score.getCurrentScore());
        values.put(Score.COLUMN_SCORE, score.getScore());
        return values;
    }

    @Override
    protected ContentValues baseToValues(Score score, String... selections) {
        ContentValues values = new ContentValues();
        for (String s : selections) {
            switch (s) {
                case Score.COLUMN_PLAYER_ID:
                    values.put(Score.COLUMN_PLAYER_ID, score.getPlayerId());
                    break;
                case Score.COLUMN_ROUND_NUMBER:
                    values.put(Score.COLUMN_ROUND_NUMBER, score.getRoundNumber());
                    break;
                case Score.COLUMN_CURRENT_SCORE:
                    values.put(Score.COLUMN_CURRENT_SCORE, score.getCurrentScore());
                    break;
                case Score.COLUMN_SCORE:
                    values.put(Score.COLUMN_SCORE, score.getScore());
                    break;
            }
        }
        return values;
    }

    @Override
    protected Score cursorToBase(Cursor cursor) {
        return new Score(
                cursor.getLong(cursor.getColumnIndexOrThrow(Score.COLUMN_ID)),
                cursor.getLong(cursor.getColumnIndexOrThrow(Score.COLUMN_PLAYER_ID)),
                cursor.getLong(cursor.getColumnIndexOrThrow(Score.COLUMN_ROUND_NUMBER)),
                cursor.getLong(cursor.getColumnIndexOrThrow(Score.COLUMN_CURRENT_SCORE)),
                cursor.getLong(cursor.getColumnIndexOrThrow(Score.COLUMN_SCORE))
        );
    }
}
