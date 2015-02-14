package com.tnt.scoreboard;

import android.app.AlertDialog;
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
import com.tnt.scoreboard.utils.StringUtils;

import java.util.List;

public class GameScoreActivity extends BaseActivity {

    public static final String ROUND = "Round ";
    private RecyclerView mRecyclerView;
    private PlayerAdapter mPlayerAdapter;
    private Game mGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_game_score);
        mGame = getGame(getIntent().getLongExtra(Game.COLUMN_ID, -1));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mPlayerAdapter = new PlayerAdapter(mGame);
        mPlayerAdapter.setListener(new PlayerAdapter.IOnScoreUpdateListener() {
            @Override
            public void onAdded(Player player, Score score) {
                addScore(player, score);
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
            public void onEnded(List<Integer> championList) {
                //TODO: Winner dialog
                new AlertDialog.Builder(GameScoreActivity.this)
                        .setMessage("Winner is " + StringUtils.join(championList, ","));
            }
        });
        mRecyclerView.setAdapter(mPlayerAdapter);
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
}
