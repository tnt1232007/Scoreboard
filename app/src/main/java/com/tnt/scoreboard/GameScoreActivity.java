package com.tnt.scoreboard;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.tnt.scoreboard.adapters.PlayerAdapter;
import com.tnt.scoreboard.models.Game;

public class GameScoreActivity extends BaseActivity {

    public static final String ROUND = "Round ";
    private RecyclerView mRecyclerView;
    private PlayerAdapter mPlayerAdapterAdapter;
    private Game mGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_game_score);
        mGame = getGame(getIntent().getLongExtra(Game.COLUMN_ID, -1));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mPlayerAdapterAdapter = new PlayerAdapter(mGame.getPlayers());
        mRecyclerView.setAdapter(mPlayerAdapterAdapter);
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
            case R.id.action_previous:
                mGame.decrementRounds();
                updateGame(mGame);
                setTitle(ROUND + (mGame.getNumberOfRounds() + 1));
                return true;
            case R.id.action_next:
                mGame.incrementCurrentRound();
                updateGame(mGame);
                setTitle(ROUND + (mGame.getNumberOfRounds() + 1));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
