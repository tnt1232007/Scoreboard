package com.tnt.scoreboard.dataAccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.models.Player;
import com.tnt.scoreboard.utils.FileUtils;
import com.tnt.scoreboard.utils.RandUtils;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String SQLITE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final int DATABASE_VERSION = 23;
    private static final String DATABASE_NAME = "scoreboard.sqlite";

    private static final String CREATE_TABLE_GAME = "CREATE TABLE " + Game.TABLE_NAME + "("
            + Game.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Game.COLUMN_TITLE + " VARCHAR NOT NULL, "
            + Game.COLUMN_NUMBER_OF_PLAYERS + " INTEGER NOT NULL, "
            + Game.COLUMN_CURRENT_ROUND_NUMBER + " INTEGER NOT NULL, "
            + Game.COLUMN_ENDING_SCORE + " INTEGER NOT NULL, "
            + Game.COLUMN_FIRST_TO_WIN + " INTEGER NOT NULL, "
            + Game.COLUMN_INFINITE + " INTEGER NOT NULL, "
            + Game.COLUMN_STATE + " INTEGER NOT NULL, "
            + Game.COLUMN_CREATED_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ");";
    private static final String CREATE_TABLE_PLAYER = "CREATE TABLE " + Player.TABLE_NAME + "("
            + Player.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Player.COLUMN_GAME_ID + " INTEGER NOT NULL, "
            + Player.COLUMN_NAME + " VARCHAR NOT NULL, "
            + Player.COLUMN_SCORE + " INTEGER NOT NULL, "
            + Player.COLUMN_COLOR + " INTEGER NOT NULL"
            + ");";

    private static final String POPULATE_TABLE_GAME = "INSERT INTO " + Game.TABLE_NAME + " ("
            + Game.COLUMN_ID + ","
            + Game.COLUMN_TITLE + ","
            + Game.COLUMN_NUMBER_OF_PLAYERS + ","
            + Game.COLUMN_CURRENT_ROUND_NUMBER + ","
            + Game.COLUMN_ENDING_SCORE + ","
            + Game.COLUMN_FIRST_TO_WIN + ","
            + Game.COLUMN_INFINITE + ","
            + Game.COLUMN_STATE
            + ") VALUES(?,?,?,?,?,?,?,?)";

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

        generateRandomData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(FileUtils.APP_NAME, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + Game.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Player.TABLE_NAME);
        onCreate(db);
    }

    private void generateRandomData(SQLiteDatabase db) {
        String[] names = new String[]{
                "Jackson", "Aiden", "Liam", "Lucas", "Noah", "Mason", "Ethan", "Caden", "Jacob",
                "Logan", "Jayden", "Elijah", "Jack", "Luke", "Michael", "Benjamin", "Alexander",
                "James", "Jayce", "Caleb", "Connor", "William", "Carter", "Ryan", "Oliver",
                "Matthew", "Daniel", "Gabriel", "Henry", "Owen", "Grayson", "Dylan", "Landon",
                "Isaac", "Nicholas", "Wyatt", "Nathan", "Andrew", "Cameron", "Dominic", "Joshua",
                "Eli", "Sebastian", "Hunter", "Brayden", "David", "Samuel", "Evan", "Gavin",
                "Christian", "Max", "Anthony", "Joseph", "Julian", "John", "Colton", "Levi",
                "Muhammad", "Isaiah", "Aaron", "Tyler", "Charlie", "Adam", "Parker", "Austin",
                "Thomas", "Zachary", "Nolan", "Alex", "Ian", "Jonathan", "Christopher", "Cooper",
                "Hudson", "Miles", "Adrian", "Leo", "Blake", "Lincoln", "Jordan", "Tristan",
                "Jason", "Josiah", "Xavier", "Camden", "Chase", "Declan", "Carson", "Colin",
                "Brody", "Asher", "Jeremiah", "Micah", "Easton", "Xander", "Ryder", "Nathaniel",
                "Elliot", "Sean", "Cole", "Sophia", "Emma", "Olivia", "Ava", "Isabella", "Mia",
                "Zoe", "Lily", "Emily", "Madelyn", "Madison", "Chloe", "Charlotte", "Aubrey",
                "Avery", "Abigail", "Kaylee", "Layla", "Harper", "Ella", "Amelia", "Arianna",
                "Riley", "Aria", "Hailey", "Hannah", "Aaliyah", "Evelyn", "Addison", "Mackenzie",
                "Adalyn", "Ellie", "Brooklyn", "Nora", "Scarlett", "Grace", "Anna", "Isabelle",
                "Natalie", "Kaitlyn", "Lillian", "Sarah", "Audrey", "Elizabeth", "Leah",
                "Annabelle", "Kylie", "Mila", "Claire", "Victoria", "Maya", "Lila", "Elena",
                "Lucy", "Savannah", "Gabriella", "Callie", "Alaina", "Sophie", "Makayla", "Kennedy",
                "Sadie", "Skyler", "Allison", "Caroline", "Charlie", "Penelope", "Alyssa", "Peyton",
                "Samantha", "Liliana", "Bailey", "Maria", "Reagan", "Violet", "Eliana", "Adeline",
                "Eva", "Stella", "Keira", "Katherine", "Vivian", "Alice", "Alexandra", "Camilla",
                "Kayla", "Alexis", "Sydney", "Kaelyn", "Jasmine", "Julia", "Cora", "Lauren",
                "Piper", "Gianna", "Paisley", "Bella", "London", "Clara", "Cadence"
        };
        Integer[] colors = new Integer[]{
                -769226, -14575885, -11751600, -6543440, -5317, -16738680, -26624, -8825528,
                -1499549, -12627531, -16728876, -7617718, -16121, -6381922, -10011977,
                -16537100, -3285959, -43230, -10453621
        };
        int numOfGames = RandUtils.nextInt(20, 40);
        for (int i = 0, k = 0; i < numOfGames; i++) {
            int numOfPlayers = RandUtils.nextInt(2, 8);
            db.execSQL(POPULATE_TABLE_GAME, new Object[]{i + 1, "Poker", numOfPlayers,
                    RandUtils.nextInt(1, 100), 1000, RandUtils.nextInt(0, 1),
                    RandUtils.nextInt(0, 1), RandUtils.nextInt(0, 2)});

            for (int j = 0; j < numOfPlayers; j++, k++) {
                db.execSQL(POPULATE_TABLE_PLAYER, new Object[]{k + 1, i + 1,
                        RandUtils.nextItem(names),
                        RandUtils.nextInt(-1000, 1000),
                        RandUtils.nextItem(colors)});
            }
        }
    }
}
