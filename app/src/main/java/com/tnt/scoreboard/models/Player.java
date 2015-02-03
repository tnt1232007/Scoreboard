package com.tnt.scoreboard.models;

public class Player extends Base {

    public static final String TABLE_NAME = "player";
    public static final String COLUMN_GAME_ID = "gameId";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_COLOR = "color";

    private long gameId;
    private String name;
    private double score;
    private long color;

    public Player(String name, long color) {
        this.name = name;
        this.color = color;
        score = 0;
    }

    public Player(long id, long gameId, String name, double score, long color) {
        this.id = id;
        this.gameId = gameId;
        this.name = name;
        this.score = score;
        this.color = color;
    }

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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public long getColor() {
        return color;
    }

    public void setColor(long color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && name.equals(((Player) o).name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
