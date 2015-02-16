package com.tnt.scoreboard.models;

import android.support.annotation.NonNull;

import java.util.List;

public class Player extends Base implements Comparable<Player> {

    public static final String TABLE_NAME = "player";
    public static final String COLUMN_GAME_ID = "gameId";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_COLOR = "color";

    private long gameId;
    private String name;
    private long score;
    private long color;
    private List<Score> scoreList;
    private boolean onHold;

    public Player(String name, long color, long score) {
        this.name = name;
        this.color = color;
        this.score = score;
    }

    public Player(long id, long gameId, String name, long score, long color) {
        this.id = id;
        this.gameId = gameId;
        this.name = name;
        this.score = score;
        this.color = color;
    }

    //<editor-fold desc="Getter Setter">
    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public long getColor() {
        return color;
    }

    public void setColor(long color) {
        this.color = color;
    }

    public List<Score> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<Score> scoreList) {
        this.scoreList = scoreList;
    }

    public boolean isOnHold() {
        return onHold;
    }

    public void setOnHold(boolean onHold) {
        this.onHold = onHold;
    }
    //</editor-fold>

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(@NonNull Player another) {
        return (int) (another.getScore() - getScore());
    }
}
