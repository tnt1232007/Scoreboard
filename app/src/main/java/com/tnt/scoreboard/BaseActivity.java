package com.tnt.scoreboard;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.tnt.scoreboard.dataAccess.GameDAO;
import com.tnt.scoreboard.dataAccess.PlayerDAO;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.models.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public abstract class BaseActivity extends ActionBarActivity {

    private static final String DESC = " DESC";
    private static final String EQUALS = " = ";
    protected Toolbar mToolbar;
    private GameDAO gameDAO;
    private PlayerDAO playerDAO;

    protected void onCreate(Bundle savedInstanceState, int layoutId) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);
        PreferenceManager.setDefaultValues(this, R.xml.setting, false);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gameDAO = new GameDAO(this);
        playerDAO = new PlayerDAO(this);
    }

    public boolean onCreateOptionsMenu(Menu menu, int layoutId) {
        getMenuInflater().inflate(layoutId, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //<editor-fold desc="Data access wrapper">
    public List<Game> getRecentGameList(int limit) {
        gameDAO.open();
        List<Game> games = gameDAO.get(null, null, null, null,
                Game.COLUMN_CREATED_DATE + DESC, null);
        gameDAO.close();

        playerDAO.open();
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            game.setPlayers(playerDAO.get(Player.COLUMN_GAME_ID + EQUALS + game.getId()));
        }
        playerDAO.close();

        TreeSet<Game> treeSet = new TreeSet<>(new Comparator<Game>() {
            @Override
            public int compare(Game lhs, Game rhs) {
                if (lhs.getPlayers().containsAll(rhs.getPlayers()))
                    return 0;
                return 1;
            }
        });
        treeSet.addAll(games);

        return treeSet.size() <= limit
                ? new ArrayList<>(treeSet)
                : new ArrayList<>(treeSet).subList(0, limit);
    }

    public List<Game> getGameList(Game.State state) {
        gameDAO.open();
        List<Game> games = gameDAO.get(Game.COLUMN_STATE + EQUALS + state.ordinal());
        gameDAO.close();

        playerDAO.open();
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            game.setPlayers(playerDAO.get(Player.COLUMN_GAME_ID + EQUALS + game.getId()));
        }
        playerDAO.close();
        return games;
    }

    public Game getGame(long gameId) {
        gameDAO.open();
        Game game = gameDAO.get(gameId);
        gameDAO.close();

        if (game == null) return null;

        playerDAO.open();
        game.setPlayers(playerDAO.get(Player.COLUMN_GAME_ID + EQUALS + game.getId()));
        playerDAO.close();
        return game;
    }

    public Game addGame(List<Player> playerList) {
        gameDAO.open();
        Game game = gameDAO.create(new Game(playerList.size()));
        gameDAO.close();
        game.setPlayers(playerList);

        playerDAO.open();
        for (Player p : playerList) {
            p.setGameId(game.getId());
            p.setId(playerDAO.create(p).getId());
        }
        playerDAO.close();
        return game;
    }

    public void deleteGames(List<Game> gameList) {
        playerDAO.open();
        for (Game g : gameList) {
            for (Player p : g.getPlayers()) {
                playerDAO.delete(p.getId());
            }
        }
        playerDAO.close();

        gameDAO.open();
        for (Game g : gameList) {
            gameDAO.delete(g.getId());
        }
        gameDAO.close();
    }

    public void changeState(List<Game> gameList, Game.State state) {
        gameDAO.open();
        for (Game g : gameList) {
            g.setState(state);
            gameDAO.update(g);
        }
        gameDAO.close();
    }
    //</editor-fold>
}
