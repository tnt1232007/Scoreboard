package com.tnt.scoreboard.models;

import android.support.annotation.NonNull;

import java.util.Date;
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
    private Date createdDate;
    private Date updatedDate;
    private List<Player> players;

    public Game(String title, long numberOfPlayers, long startingScore, long endingScore) {
        this.title = title;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfRounds = 0;
        this.startingScore = startingScore;
        this.endingScore = endingScore;
        this.state = State.NORMAL;
    }

    public Game(long id, String title, long numberOfPlayers, long numberOfRounds,
                long startingScore, long endingScore, State state,
                Date createdDate, Date updatedDate) {
        this.id = id;
        this.title = title;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfRounds = numberOfRounds;
        this.startingScore = startingScore;
        this.endingScore = endingScore;
        this.state = state;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
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
