package com.tnt.scoreboard;

import android.content.Intent;
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

import java.util.List;

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

        mPlayerAdapter = new PlayerAdapter(this, mGame);
        mPlayerAdapter.setListener(new PlayerAdapter.IOnScoreUpdateListener() {
            @Override
            public void onAdded(Player player, Score score) {
                addScore(player, score);
            }

            @Override
            public void onDeleted(Player player) {
                deleteLatestScore(player);
            }

            @Override
            public void onDeleted(List<Player> playerList) {
                deleteLatestRoundScore(playerList);
            }

            @Override
            public void onUpdated(int round) {
                mGame.setNumberOfRounds(round);
                updateGame(mGame);
                setTitle(ROUND + (mGame.getNumberOfRounds() + 1));
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
        Intent intent = new Intent(this, GameHistoryActivity.class);
        intent.putExtra(Game.COLUMN_ID, mGame.getId());
        intent.putExtra(Game.COLUMN_STATE, item.getItemId());
        switch (item.getItemId()) {
            case R.id.action_stop_all:
                mPlayerAdapter.stopAll();
                mPlayerAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_undo_all:
                mPlayerAdapter.undoAll();
                return true;
            case R.id.action_history:
                startActivity(intent);
                return true;
            case R.id.action_archive:
                setResult(RESULT_OK, intent);
                finish();
                return true;
            case R.id.action_delete:
                setResult(RESULT_OK, intent);
                finish();
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
