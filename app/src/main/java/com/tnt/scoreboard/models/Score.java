package com.tnt.scoreboard.models;

public class Score extends Base {

    public static final String TABLE_NAME = "score";
    public static final String COLUMN_PLAYER_ID = "playerId";
    public static final String COLUMN_ROUND_NUMBER = "roundNumber";
    public static final String COLUMN_CURRENT_SCORE = "currentScore";
    public static final String COLUMN_SCORE = "score";

    private long playerId;
    private long roundNumber;
    private long currentScore;
    private long score;

    public Score(long id, long playerId, long roundNumber, long currentScore, long score) {
        this.roundNumber = roundNumber;
        this.currentScore = currentScore;
        this.id = id;
        this.playerId = playerId;
        this.score = score;
    }

    public Score(long playerId, long roundNumber, long currentScore, long score) {
        this.playerId = playerId;
        this.roundNumber = roundNumber;
        this.currentScore = currentScore;
        this.score = score;
    }

    //<editor-fold desc="Getter Setter">
    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(long roundNumber) {
        this.roundNumber = roundNumber;
    }

    public long getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(long currentScore) {
        this.currentScore = currentScore;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
    //</editor-fold>
}
