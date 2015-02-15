package com.tnt.scoreboard.models;

public class Score extends Base {

    public static final String TABLE_NAME = "score";
    public static final String COLUMN_PLAYER_ID = "playerId";
    public static final String COLUMN_SCORE = "score";

    private long playerId;
    private long score;

    public Score(long id, long playerId, long score) {
        this.id = id;
        this.playerId = playerId;
        this.score = score;
    }

    public Score(long playerId, long score) {
        this.playerId = playerId;
        this.score = score;
    }

    //<editor-fold desc="Getter Setter">
    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
    //</editor-fold>
}
