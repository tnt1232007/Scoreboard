package com.tnt.scoreboard;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.utils.StringUtils;

public class GameScoreActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_game_score);

        Game game = getGame(getIntent().getLongExtra(Game.COLUMN_ID, -1));
        TextView textView = (TextView) findViewById(R.id.hello_world);
        textView.setText(StringUtils.join(game.getPlayers(), " vs. "));
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
