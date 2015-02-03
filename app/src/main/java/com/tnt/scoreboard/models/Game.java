package com.tnt.scoreboard.models;

import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;


public class Game extends Base implements Comparable<Game> {

    public static final String TABLE_NAME = "game";
    public static final String COLUMN_CREATED_DATE = "createdDate";
    public static final String COLUMN_NUMBER_OF_PLAYERS = "numberOfPlayers";
    public static final String COLUMN_CURRENT_ROUND_NUMBER = "currentRoundNumber";
    public static final String COLUMN_STATE = "state";
    private int index;
    private Date createDate;
    private long numberOfPlayers;
    private long currentRoundNumber;
    private State state;
    private List<Player> players;

    public Game(long numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        currentRoundNumber = 0;
        state = State.NORMAL;
    }

    public Game(long id, long numberOfPlayers, long currentRoundNumber, Date createDate, State state) {
        this.id = id;
        this.numberOfPlayers = numberOfPlayers;
        this.currentRoundNumber = currentRoundNumber;
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

    public long getCurrentRoundNumber() {
        return currentRoundNumber;
    }

    public void setCurrentRoundNumber(long currentRoundNumber) {
        this.currentRoundNumber = currentRoundNumber;
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
