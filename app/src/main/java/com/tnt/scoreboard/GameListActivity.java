package com.tnt.scoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tnt.scoreboard.adapters.GameAdapter;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.utils.ActivityUtils;


//TODO: Swipe to archive/delete
public class GameListActivity extends BaseActivity {

    protected ActionMode mActionMode;
    private NavigationDrawerFragment mNavigationDrawer;
    private GameAdapter mGameAdapter;
    private MyNewGameButton mFab;
    private RecyclerView mRecyclerView;
    private MyUndoBar mUndoBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_game_list);
        mUndoBar = new MyUndoBar(this);

        mFab = (MyNewGameButton) findViewById(R.id.fab);
        mFab.setup(this, getRecentGameList(3));

        mNavigationDrawer = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigationDrawer);
        mNavigationDrawer.setup(this, (DrawerLayout) findViewById(R.id.drawerLayout));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mNavigationDrawer.navigate(ActivityUtils.GAMES);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu, R.menu.menu_game_list);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mNavigationDrawer.isDrawerToggleSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case MyNewGameButton.NEW_GAME_REQUEST:
                long gameId = data.getLongExtra(Game.COLUMN_ID, -1);
                mGameAdapter.add(getGame(gameId));
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void showEmptyView(boolean visible, ActivityUtils.Screen screen) {
        findViewById(R.id.layout).setBackgroundResource(
                visible ? screen.COLOR_ACCENT : android.R.color.background_light);
        findViewById(R.id.emptyLayout).setVisibility(visible ? View.VISIBLE : View.GONE);
        ((ImageView) findViewById(R.id.emptyImage)).setImageResource(screen.EMPTY_BACKGROUND);
        ((TextView) findViewById(R.id.emptyHeader)).setText(screen.EMPTY_HEADER);
        ((TextView) findViewById(R.id.emptyText)).setText(screen.EMPTY_TEXT);
    }

    public GameAdapter getGameAdapter() {
        return mGameAdapter;
    }

    public void setGameAdapter(GameAdapter gameAdapter) {
        mGameAdapter = gameAdapter;
        mRecyclerView.setAdapter(gameAdapter);
    }

    public MyNewGameButton getFab() {
        return mFab;
    }

    public MyUndoBar getUndoBar() {
        return mUndoBar;
    }

//TODO: Animate toolbar color change
//    private void revealImageCircular(final int primary, final int primaryDark) {
//        final View view = mToolbar;
//        view.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                int x = view.getWidth() / 2;
//                int y = view.getHeight();
//                Animator anim = ViewAnimationUtils.createCircularReveal(view, x, y, 0, x);
//                anim.setDuration(400);
//                anim.addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//                        super.onAnimationStart(animation);
//                        mToolbar.setBackground(new ColorDrawable(primary));
//                        getWindow().setStatusBarColor(primaryDark);
//                    }
//                });
//                anim.start();
//            }
//        }, 100);
//    }
}
