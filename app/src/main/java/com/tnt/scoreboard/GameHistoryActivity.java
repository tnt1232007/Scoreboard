package com.tnt.scoreboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.tnt.scoreboard.adapters.HistoryPagerAdapter;
import com.tnt.scoreboard.libs.SlidingTabLayout;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.utils.ColorUtils;

public class GameHistoryActivity extends BaseActivity {

    private Game mGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_game_history);
        mGame = getGame(getIntent().getLongExtra(Game.COLUMN_ID, -1));

        final ViewPager pager = ((ViewPager) findViewById(R.id.pager));
        final HistoryPagerAdapter adapter = new HistoryPagerAdapter(getSupportFragmentManager(), mGame);
        pager.setAdapter(adapter);

        SlidingTabLayout tabs = ((SlidingTabLayout) findViewById(R.id.tabs));
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return ColorUtils.getAttrColor(GameHistoryActivity.this, android.R.attr.colorPrimary);
            }
        });
        tabs.setViewPager(pager);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPx) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position != 1) return;
                SummaryFragment fragment = (SummaryFragment) adapter.instantiateItem(pager, position);
                fragment.update();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (mGame.getState()) {
            case ARCHIVE:
                return onCreateOptionsMenu(menu, R.menu.menu_archive_history);
            case DELETE:
                return onCreateOptionsMenu(menu, R.menu.menu_trash_history);
            default:
                return onCreateOptionsMenu(menu, R.menu.menu_game_history);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Intent intent = new Intent();
        intent.putExtra(Game.COLUMN_ID, mGame.getId());
        intent.putExtra(Game.COLUMN_STATE, item.getItemId());

        switch (item.getItemId()) {
            case R.id.action_archive:
            case R.id.action_unarchive:
            case R.id.action_delete:
            case R.id.action_restore:
                setResult(RESULT_OK, intent);
                finish();
                return true;
            case R.id.action_delete_forever:
                DialogInterface.OnClickListener dialogListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        setResult(RESULT_OK, intent);
                                        finish();
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        };
                new AlertDialog.Builder(this)
                        .setMessage("Delete current game forever?")
                        .setPositiveButton("Yes", dialogListener)
                        .setNegativeButton("No", dialogListener).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
