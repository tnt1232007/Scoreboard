/*
 * Copyright 2015 TNT
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tnt.scoreboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.tnt.scoreboard.adapters.PlayerAdapter;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.models.Player;
import com.tnt.scoreboard.models.Score;
import com.tnt.scoreboard.utils.DateTimeUtils;

import org.joda.time.DateTime;

import java.util.List;

public class GameScoreActivity extends BaseActivity
        implements InfoDrawerFragment.IOnDrawerToggle {

    public static final String ROUND = "Round ";
    private PlayerAdapter mPlayerAdapter;
    private Game mGame;
    private boolean updated;
    private InfoDrawerFragment mInfoDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_game_score);
        mGame = getGame(getIntent().getLongExtra(Game.COLUMN_ID, -1));

        mInfoDrawer = (InfoDrawerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.infoDrawer);
        mInfoDrawer.setup((DrawerLayout) findViewById(R.id.drawerLayout));
        mInfoDrawer.setListener(this);
        mInfoDrawer.update(new Game(0, "", 0, 0, 0, 0, Game.State.NORMAL, DateTime.now(), DateTime.now()));

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
                mGame.setUpdatedDate(DateTimeUtils.now());
                updateGame(mGame, Game.COLUMN_NUMBER_OF_ROUNDS, Game.COLUMN_UPDATED_DATE);
                updated = true;
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
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_stop_all:
                mPlayerAdapter.stopAll();
                mPlayerAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_undo_all:
                mPlayerAdapter.undoAll();
                return true;
            case R.id.action_history:
                intent = new Intent(this, GameHistoryActivity.class);
                intent.putExtra(Game.COLUMN_ID, mGame.getId());
                startActivity(intent);
                return true;
            case R.id.action_archive:
            case R.id.action_delete:
                intent = new Intent();
                intent.putExtra(Game.COLUMN_ID, mGame.getId());
                intent.putExtra(Game.COLUMN_STATE, item.getItemId());
                setResult(RESULT_OK, intent);
                finish();
                return true;
            case R.id.action_rematch:
                intent = new Intent();
                intent.putExtra(Game.COLUMN_ID, mGame.getId());
                intent.putExtra(Game.COLUMN_UPDATED_DATE, updated);
                setResult(RESULT_FIRST_USER, intent);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Game.COLUMN_UPDATED_DATE, updated);
        setResult(RESULT_CANCELED, intent);
        super.onBackPressed();
    }

    @Override
    public void onDrawerOpened() {
        mInfoDrawer.update(mGame);
    }
}
