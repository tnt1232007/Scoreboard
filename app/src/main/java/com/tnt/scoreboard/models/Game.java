package com.tnt.scoreboard.models;

import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

public class Game extends Base implements Comparable<Game> {

    public static final String TABLE_NAME = "game";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CREATED_DATE = "createdDate";
    public static final String COLUMN_NUMBER_OF_PLAYERS = "numberOfPlayers";
    public static final String COLUMN_NUMBER_OF_ROUNDS = "numberOfRounds";
    public static final String COLUMN_FIRST_TO_WIN = "firstToWin";
    public static final String COLUMN_INFINITE = "infinite";
    public static final String COLUMN_ENDING_SCORE = "endingScore";
    public static final String COLUMN_STATE = "state";
    private int index;
    private String title;
    private Date createDate;
    private long numberOfPlayers;
    private long numberOfRounds;
    private long endingScore;
    private boolean firstToWin;
    private boolean infinite;
    private State state;
    private List<Player> players;

    public Game(String title, long numberOfPlayers, long endingScore,
                boolean firstToWin, boolean infinite) {
        this.title = title;
        this.numberOfPlayers = numberOfPlayers;
        this.endingScore = endingScore;
        this.firstToWin = firstToWin;
        this.infinite = infinite;
        numberOfRounds = 0;
        state = State.NORMAL;
    }

    public Game(long id, String title, long numberOfPlayers, long numberOfRounds,
                long endingScore, boolean firstToWin, boolean infinite,
                Date createDate, State state) {
        this.id = id;
        this.title = title;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfRounds = numberOfRounds;
        this.endingScore = endingScore;
        this.firstToWin = firstToWin;
        this.infinite = infinite;
        this.createDate = createDate;
        this.state = state;
    }

    //<editor-fold desc="Getter Setter">
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public long getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(long numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public long getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(long numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public long getEndingScore() {
        return endingScore;
    }

    public void setEndingScore(long endingScore) {
        this.endingScore = endingScore;
    }

    public boolean isFirstToWin() {
        return firstToWin;
    }

    public void setFirstToWin(boolean firstToWin) {
        this.firstToWin = firstToWin;
    }

    public boolean isInfinite() {
        return infinite;
    }

    public void setInfinite(boolean infinite) {
        this.infinite = infinite;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
    //</editor-fold>

    @Override
    public String toString() {
        return id + "." + createDate;
    }

    @Override
    public int compareTo(@NonNull Game another) {
        return getIndex() - another.getIndex();
    }

    public enum State {
        NORMAL, ARCHIVE, DELETE
    }
}
