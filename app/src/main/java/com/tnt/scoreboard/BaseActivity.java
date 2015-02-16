package com.tnt.scoreboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.tnt.scoreboard.dataAccess.GameDAO;
import com.tnt.scoreboard.dataAccess.PlayerDAO;
import com.tnt.scoreboard.dataAccess.ScoreDAO;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.models.Player;
import com.tnt.scoreboard.models.Score;
import com.tnt.scoreboard.utils.Constants;
import com.tnt.scoreboard.utils.DrawableUtils;
import com.tnt.scoreboard.utils.FileUtils;
import com.tnt.scoreboard.utils.PrefUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public abstract class BaseActivity extends ActionBarActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        NavigationDrawerFragment.OnDrawerToggle {

    private static final String DESC = " DESC";
    private static final String EQUALS = " = ";
    protected Toolbar mToolbar;
    private GameDAO mGameDAO;
    private PlayerDAO mPlayerDAO;
    private ScoreDAO mScoreDAO;

    protected void onCreate(Bundle savedInstanceState, int layoutId) {
        PreferenceManager.setDefaultValues(this, R.xml.setting, false);
        switchTheme(PrefUtils.getTheme(this));
        switchOrientation(PrefUtils.getOrientation(this));

        super.onCreate(savedInstanceState);
        setContentView(layoutId);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGameDAO = new GameDAO(this);
        mPlayerDAO = new PlayerDAO(this);
        mScoreDAO = new ScoreDAO(this);
    }

    public boolean onCreateOptionsMenu(Menu menu, int layoutId) {
        getMenuInflater().inflate(layoutId, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_help:
                Bitmap bitmap = DrawableUtils.takeScreenShot(
                        getWindow().getDecorView().getRootView());
                FileUtils.saveBitmap(bitmap, Constants.SCREENSHOT);
                intent = new Intent(this, HelpFeedbackActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_key_orientation))) {
            String orientation = sharedPreferences.getString(key, "");
            switchOrientation(orientation);
        } else if (key.equals(getString(R.string.pref_key_theme))) {
            recreate();
        }
    }

    @Override
    public void onDrawerOpened(Parcelable parcelable) {
    }

    private void switchOrientation(String orientation) {
        if (orientation.equals(getString(R.string.pref_orientation_portrait))) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (orientation.equals(getString(R.string.pref_orientation_landscape))) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    private void switchTheme(String theme) {
        boolean isLight = theme.equals(getString(R.string.pref_theme_light));
        if (this instanceof GameNewActivity) {
            setTheme(isLight ? R.style.GameNewLightTheme : R.style.GameNewTheme);
        } else if (this instanceof GameScoreActivity) {
            setTheme(isLight ? R.style.GameScoreLightTheme : R.style.GameScoreTheme);
        } else if (this instanceof GameHistoryActivity) {
            setTheme(isLight ? R.style.GameHistoryLightTheme : R.style.GameHistoryTheme);
        } else {
            setTheme(isLight ? R.style.BaseLightTheme : R.style.BaseTheme);
        }
    }

    //<editor-fold desc="Data access wrapper">
    public List<Game> getRecentGameList(int limit) {
        mGameDAO.open();
        List<Game> games = mGameDAO.get(null, null, null, null,
                Game.COLUMN_CREATED_DATE + DESC, null);
        mGameDAO.close();

        mPlayerDAO.open();
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            game.setPlayers(mPlayerDAO.get(Player.COLUMN_GAME_ID + EQUALS + game.getId()));
        }
        mPlayerDAO.close();

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
        mGameDAO.open();
        List<Game> games = mGameDAO.get(Game.COLUMN_STATE + EQUALS + state.ordinal());
        mGameDAO.close();

        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            mPlayerDAO.open();
            List<Player> players = mPlayerDAO.get(Player.COLUMN_GAME_ID + EQUALS + game.getId());
            mPlayerDAO.close();

            mScoreDAO.open();
            for (int j = 0; j < players.size(); j++) {
                Player player = players.get(j);
                player.setScoreList(mScoreDAO.get(
                        Score.COLUMN_PLAYER_ID + EQUALS + player.getId()));
            }
            mScoreDAO.close();

            game.setPlayers(players);
        }
        return games;
    }

    public Game getGame(long gameId) {
        mGameDAO.open();
        Game game = mGameDAO.get(gameId);
        mGameDAO.close();

        if (game == null) return null;

        mPlayerDAO.open();
        List<Player> players = mPlayerDAO.get(Player.COLUMN_GAME_ID + EQUALS + game.getId());
        mPlayerDAO.close();

        mScoreDAO.open();
        for (int j = 0; j < players.size(); j++) {
            Player player = players.get(j);
            player.setScoreList(mScoreDAO.get(
                    Score.COLUMN_PLAYER_ID + EQUALS + player.getId()));
        }
        mScoreDAO.close();

        game.setPlayers(players);
        return game;
    }

    public Game addGame(String title, List<Player> playerList,
                        long startingScore, long endingScore) {
        mGameDAO.open();
        Game game = mGameDAO.create(new Game(title, playerList.size(), startingScore, endingScore));
        mGameDAO.close();
        game.setPlayers(playerList);

        mPlayerDAO.open();
        for (Player p : playerList) {
            p.setGameId(game.getId());
            p.setId(mPlayerDAO.create(p).getId());
        }
        mPlayerDAO.close();
        return game;
    }

    public void deleteGames(List<Game> gameList) {
        mPlayerDAO.open();
        for (Game g : gameList) {
            for (Player p : g.getPlayers()) {
                mPlayerDAO.delete(p.getId());
            }
        }
        mPlayerDAO.close();

        mGameDAO.open();
        for (Game g : gameList) {
            mGameDAO.delete(g.getId());
        }
        mGameDAO.close();
    }

    public void updateGame(Game game) {
        mGameDAO.open();
        mGameDAO.update(game);
        mGameDAO.close();
    }

    public void addScore(Player player, Score score) {
        mScoreDAO.open();
        score = mScoreDAO.create(score);
        mScoreDAO.close();

        mPlayerDAO.open();
        player.setScore(player.getScore() + score.getScore());
        player.getScoreList().add(score);
        mPlayerDAO.update(player);
        mPlayerDAO.close();
    }

    public void deleteLatestScore(Player player) {
        mScoreDAO.open();
        List<Score> scoreList = player.getScoreList();
        if (scoreList.size() == 0) {
            mScoreDAO.close();
            return;
        }

        Score score = scoreList.get(scoreList.size() - 1);
        mScoreDAO.delete(score.getId());
        mScoreDAO.close();

        mPlayerDAO.open();
        player.setScore(player.getScore() - score.getScore());
        player.getScoreList().remove(score);
        mPlayerDAO.update(player);
        mPlayerDAO.close();
    }

    public void deleteLatestRoundScore(List<Player> playerList) {
        int maxRounds = 0;
        for (Player p : playerList) {
            int size = p.getScoreList().size();
            if (maxRounds < size)
                maxRounds = size;
        }

        if (maxRounds == 0)
            return;

        for (Player p : playerList) {
            int size = p.getScoreList().size();
            if (size != maxRounds)
                continue;

            mScoreDAO.open();
            Score score = p.getScoreList().get(maxRounds - 1);
            mScoreDAO.delete(score.getId());
            mScoreDAO.close();

            mPlayerDAO.open();
            p.setScore(p.getScore() - score.getScore());
            p.getScoreList().remove(score);
            mPlayerDAO.update(p);
            mPlayerDAO.close();
        }
    }
    //</editor-fold>
}
