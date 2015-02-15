package com.tnt.scoreboard;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.tnt.scoreboard.adapters.HistoryPagerAdapter;
import com.tnt.scoreboard.libs.SlidingTabLayout;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.utils.ColorUtils;

public class GameHistoryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_game_history);
        Game game = getGame(getIntent().getLongExtra(Game.COLUMN_ID, -1));

        ViewPager pager = ((ViewPager) findViewById(R.id.pager));
        pager.setAdapter(new HistoryPagerAdapter(getSupportFragmentManager(), game));

        SlidingTabLayout tabs = ((SlidingTabLayout) findViewById(R.id.tabs));
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return ColorUtils.getAttrColor(
                        GameHistoryActivity.this, android.R.attr.colorPrimary);
            }
        });
        tabs.setViewPager(pager);
    }
}
