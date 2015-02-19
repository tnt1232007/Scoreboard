package com.tnt.scoreboard.dataAccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.models.Player;
import com.tnt.scoreboard.models.Score;
import com.tnt.scoreboard.utils.Constants;
import com.tnt.scoreboard.utils.FileUtils;
import com.tnt.scoreboard.utils.RandUtils;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 26;
    private static final String DATABASE_NAME = "scoreboard.sqlite";

    private static final String CREATE_TABLE_GAME = "CREATE TABLE " + Game.TABLE_NAME + "("
            + Game.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Game.COLUMN_TITLE + " VARCHAR NOT NULL, "
            + Game.COLUMN_NUMBER_OF_PLAYERS + " INTEGER NOT NULL, "
            + Game.COLUMN_NUMBER_OF_ROUNDS + " INTEGER NOT NULL, "
            + Game.COLUMN_STARTING_SCORE + " INTEGER NOT NULL, "
            + Game.COLUMN_ENDING_SCORE + " INTEGER NOT NULL, "
            + Game.COLUMN_STATE + " INTEGER NOT NULL, "
            + Game.COLUMN_CREATED_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + Game.COLUMN_UPDATED_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ");";

    private static final String CREATE_TABLE_PLAYER = "CREATE TABLE " + Player.TABLE_NAME + "("
            + Player.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Player.COLUMN_GAME_ID + " INTEGER NOT NULL, "
            + Player.COLUMN_NAME + " VARCHAR NOT NULL, "
            + Player.COLUMN_SCORE + " INTEGER NOT NULL, "
            + Player.COLUMN_COLOR + " INTEGER NOT NULL"
            + ");";

    private static final String CREATE_TABLE_SCORE = "CREATE TABLE " + Score.TABLE_NAME + "("
            + Score.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Score.COLUMN_PLAYER_ID + " INTEGER NOT NULL, "
            + Score.COLUMN_ROUND_NUMBER + " INTEGER NOT NULL, "
            + Score.COLUMN_CURRENT_SCORE + " INTEGER NOT NULL, "
            + Score.COLUMN_SCORE + " INTEGER NOT NULL"
            + ");";

    private static final String POPULATE_TABLE_GAME = "INSERT INTO " + Game.TABLE_NAME + " ("
            + Game.COLUMN_ID + ","
            + Game.COLUMN_TITLE + ","
            + Game.COLUMN_NUMBER_OF_PLAYERS + ","
            + Game.COLUMN_NUMBER_OF_ROUNDS + ","
            + Game.COLUMN_STARTING_SCORE + ","
            + Game.COLUMN_ENDING_SCORE + ","
            + Game.COLUMN_STATE + ","
            + Game.COLUMN_CREATED_DATE + ","
            + Game.COLUMN_UPDATED_DATE
            + ") VALUES(?,?,?,?,?,?,?,?,?)";

    private static final String POPULATE_TABLE_PLAYER = "INSERT INTO " + Player.TABLE_NAME + " ("
            + Player.COLUMN_ID + ","
            + Player.COLUMN_GAME_ID + ","
            + Player.COLUMN_NAME + ","
            + Player.COLUMN_SCORE + ","
            + Player.COLUMN_COLOR
            + ") VALUES(?,?,?,?,?)";

    private static final String POPULATE_TABLE_SCORE = "INSERT INTO " + Score.TABLE_NAME + " ("
            + Score.COLUMN_ID + ","
            + Score.COLUMN_PLAYER_ID + ","
            + Score.COLUMN_ROUND_NUMBER + ","
            + Score.COLUMN_CURRENT_SCORE + ","
            + Score.COLUMN_SCORE
            + ") VALUES(?,?,?,?,?)";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_GAME);
        db.execSQL(CREATE_TABLE_PLAYER);
        db.execSQL(CREATE_TABLE_SCORE);

        generateRandomData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(FileUtils.APP_NAME, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + Game.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Player.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Score.TABLE_NAME);
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
        int gameId = 1, playerId = 1, scoreId = 1;
        int numOfGames = RandUtils.nextInt(20, 40);
        for (int i = 0; i < numOfGames; i++, gameId++) {
            int numOfPlayers = RandUtils.nextInt(Constants.MIN_PLAYERS, Constants.MAX_PLAYERS);
            int numOfRounds = RandUtils.nextInt(1, 100);
            int startingScore = RandUtils.nextInt(0, 40);
            int endingScore = RandUtils.nextInt(60, 100);
            String date = RandUtils.nextDateTime(5).toString(Constants.SQLITE_DATE_FORMAT);
            db.execSQL(POPULATE_TABLE_GAME, new Object[]{
                    gameId, "Poker" + gameId, numOfPlayers,
                    numOfRounds, startingScore, endingScore,
                    RandUtils.nextInt(0, 2), date, date});

            for (int j = 0; j < numOfPlayers; j++, playerId++) {
                int currentScore = startingScore;
                for (int l = 0; l < numOfRounds; l++, scoreId++) {
                    int score = RandUtils.nextInt(-3, 3);
                    db.execSQL(POPULATE_TABLE_SCORE, new Object[]{
                            scoreId, playerId, l + 1, currentScore, score
                    });
                    currentScore += score;
                }

                db.execSQL(POPULATE_TABLE_PLAYER, new Object[]{
                        playerId, gameId,
                        RandUtils.nextItem(names),
                        currentScore,
                        RandUtils.nextItem(colors)});
            }
        }
    }
}
