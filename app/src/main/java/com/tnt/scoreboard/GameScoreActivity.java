package com.tnt.scoreboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.tnt.scoreboard.adapters.PlayerAdapter;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.models.Player;
import com.tnt.scoreboard.models.Score;

public class GameScoreActivity extends BaseActivity {

    public static final String ROUND = "Round ";
    private PlayerAdapter mPlayerAdapter;
    private Game mGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_game_score);
        mGame = getGame(getIntent().getLongExtra(Game.COLUMN_ID, -1));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mPlayerAdapter = new PlayerAdapter(mGame);
        mPlayerAdapter.setListener(new PlayerAdapter.IOnScoreUpdateListener() {
            @Override
            public Score onAdded(Player player, Score score) {
                return addScore(player, score);
            }

            @Override
            public Score onDeleted(Player player) {
                return deleteScore(player);
            }

            @Override
            public void onUpdated(int round) {
                mGame.setNumberOfRounds(round);
                updateGame(mGame);
                setTitle(ROUND + (mGame.getNumberOfRounds() + 1));
            }

            @Override
            public void onEnded() {
                //TODO: Show history activity
            }
        });
        recyclerView.setAdapter(mPlayerAdapter);
        setTitle(ROUND + (mGame.getNumberOfRounds() + 1));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return onCreateOptionsMenu(menu, R.menu.menu_game_score);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);
        if (key.equals(getString(R.string.pref_key_update_delay))
                || key.equals(getString(R.string.pref_key_score_0))
                || key.equals(getString(R.string.pref_key_score_1))
                || key.equals(getString(R.string.pref_key_score_2))
                || key.equals(getString(R.string.pref_key_score_3))) {
            mPlayerAdapter.notifyDataSetChanged();
        }
    }
}
