package com.tnt.scoreboard.models;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class Game extends Base implements Comparable<Game> {

    public static final String TABLE_NAME = "game";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_NUMBER_OF_PLAYERS = "numberOfPlayers";
    public static final String COLUMN_NUMBER_OF_ROUNDS = "numberOfRounds";
    public static final String COLUMN_STARTING_SCORE = "startingScore";
    public static final String COLUMN_ENDING_SCORE = "endingScore";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_CREATED_DATE = "createdDate";
    public static final String COLUMN_UPDATED_DATE = "updatedDate";

    private int index;
    private String title;
    private long numberOfPlayers;
    private long numberOfRounds;
    private long startingScore;
    private long endingScore;
    private State state;
    private DateTime createdDate;
    private DateTime updatedDate;
    private List<Player> mPlayerList;

    public Game(String title, long numberOfPlayers, long startingScore, long endingScore) {
        this.title = title;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfRounds = 0;
        this.startingScore = startingScore;
        this.endingScore = endingScore;
        this.state = State.NORMAL;
        this.mPlayerList = new ArrayList<>();
    }

    public Game(long id, String title, long numberOfPlayers, long numberOfRounds,
                long startingScore, long endingScore, State state,
                DateTime createdDate, DateTime updatedDate) {
        this.id = id;
        this.title = title;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfRounds = numberOfRounds;
        this.startingScore = startingScore;
        this.endingScore = endingScore;
        this.state = state;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.mPlayerList = new ArrayList<>();
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

    public long getStartingScore() {
        return startingScore;
    }

    public void setStartingScore(long startingScore) {
        this.startingScore = startingScore;
    }

    public long getEndingScore() {
        return endingScore;
    }

    public void setEndingScore(long endingScore) {
        this.endingScore = endingScore;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public DateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(DateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<Player> getPlayerList() {
        return mPlayerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.mPlayerList = playerList;
    }
    //</editor-fold>

    @Override
    public String toString() {
        return id + "." + createdDate;
    }

    @Override
    public int compareTo(@NonNull Game another) {
        return index - another.index;
    }

    public enum State {
        NORMAL, ARCHIVE, DELETE
    }
}
