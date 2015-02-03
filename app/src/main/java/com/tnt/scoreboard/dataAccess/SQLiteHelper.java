package com.tnt.scoreboard.dataAccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.models.Player;


public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String SQLITE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATABASE_NAME = "scoreboard.sqlite";
    private static final int DATABASE_VERSION = 12;

    private static final String CREATE_TABLE_GAME = "CREATE TABLE " + Game.TABLE_NAME + "("
            + Game.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Game.COLUMN_CREATED_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + Game.COLUMN_NUMBER_OF_PLAYERS + " INTEGER NOT NULL, "
            + Game.COLUMN_CURRENT_ROUND_NUMBER + " INTEGER NOT NULL, "
            + Game.COLUMN_STATE + " INTEGER NOT NULL"
            + ");";
    private static final String CREATE_TABLE_PLAYER = "CREATE TABLE " + Player.TABLE_NAME + "("
            + Player.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Player.COLUMN_GAME_ID + " INTEGER NOT NULL, "
            + Player.COLUMN_NAME + " VARCHAR NOT NULL, "
            + Player.COLUMN_SCORE + " DOUBLE NOT NULL, "
            + Player.COLUMN_COLOR + " INTEGER NOT NULL"
            + ");";

    private static final String POPULATE_TABLE_GAME = "INSERT INTO " + Game.TABLE_NAME + " ("
            + Game.COLUMN_ID + ","
            + Game.COLUMN_NUMBER_OF_PLAYERS + ","
            + Game.COLUMN_CURRENT_ROUND_NUMBER + ","
            + Game.COLUMN_STATE
            + ") VALUES(?,?,?,?)";

    private static final String POPULATE_TABLE_PLAYER = "INSERT INTO " + Player.TABLE_NAME + " ("
            + Player.COLUMN_ID + ","
            + Player.COLUMN_GAME_ID + ","
            + Player.COLUMN_NAME + ","
            + Player.COLUMN_SCORE + ","
            + Player.COLUMN_COLOR
            + ") VALUES(?,?,?,?,?)";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_GAME);
        db.execSQL(CREATE_TABLE_PLAYER);

        db.execSQL(POPULATE_TABLE_GAME, new Object[]{1, 4, 112, 0});
        db.execSQL(POPULATE_TABLE_GAME, new Object[]{2, 8, 7, 1});
        db.execSQL(POPULATE_TABLE_GAME, new Object[]{3, 6, 36, 2});

        db.execSQL(POPULATE_TABLE_PLAYER, new Object[]{1, 1, "TNT", 246, -1499549});
        db.execSQL(POPULATE_TABLE_PLAYER, new Object[]{2, 1, "KL12", 312, -26624});
        db.execSQL(POPULATE_TABLE_PLAYER, new Object[]{3, 1, "LG", 216, -12627531});
        db.execSQL(POPULATE_TABLE_PLAYER, new Object[]{4, 1, "Sony", -124, -11751600});

        db.execSQL(POPULATE_TABLE_PLAYER, new Object[]{5, 2, "Player S", 62, -16121});
        db.execSQL(POPULATE_TABLE_PLAYER, new Object[]{6, 2, "Player T", 47, -16738680});
        db.execSQL(POPULATE_TABLE_PLAYER, new Object[]{7, 2, "Player U", 27, -11751600});
        db.execSQL(POPULATE_TABLE_PLAYER, new Object[]{8, 2, "Player V", 79, -16537100});
        db.execSQL(POPULATE_TABLE_PLAYER, new Object[]{9, 2, "Player W", 12, -43230});
        db.execSQL(POPULATE_TABLE_PLAYER, new Object[]{10, 2, "Player X", 40, -3285959});
        db.execSQL(POPULATE_TABLE_PLAYER, new Object[]{11, 2, "Player Y", 11, -7617718});
        db.execSQL(POPULATE_TABLE_PLAYER, new Object[]{12, 2, "Player Z", 73, -16728876});

        db.execSQL(POPULATE_TABLE_PLAYER, new Object[]{13, 3, "Adam", 84, -16537100});
        db.execSQL(POPULATE_TABLE_PLAYER, new Object[]{14, 3, "Lily", 72, -12627531});
        db.execSQL(POPULATE_TABLE_PLAYER, new Object[]{15, 3, "Trevor", 92, -10453621});
        db.execSQL(POPULATE_TABLE_PLAYER, new Object[]{16, 3, "Tim", 124, -43230});
        db.execSQL(POPULATE_TABLE_PLAYER, new Object[]{17, 3, "Jessi", 102, -11751600});
        db.execSQL(POPULATE_TABLE_PLAYER, new Object[]{18, 3, "Doctor", 99, -8825528});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + Game.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Player.TABLE_NAME);
        onCreate(db);
    }
}
