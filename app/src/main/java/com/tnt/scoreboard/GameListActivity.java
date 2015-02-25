package com.tnt.scoreboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.tnt.scoreboard.adapters.GameAdapter;
import com.tnt.scoreboard.adapters.NavigationAdapter;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.utils.ActivityUtils;
import com.tnt.scoreboard.utils.Constants;
import com.tnt.scoreboard.utils.FileUtils;

import java.util.List;

public class GameListActivity extends BaseActivity implements
        com.nispok.snackbar.listeners.ActionClickListener,
        NavigationAdapter.IOnNavigationClickListener,
        NavigationDrawerFragment.IOnDrawerToggle,
        GameAdapter.IOnGameSelectListener,
        ActionMode.Callback {

    private ActionMode mActionMode;
    private GameAdapter mGameAdapter;
    private FloatingNewGameMenu mFab;
    private RecyclerView mRecyclerView;
    private NavigationDrawerFragment mNavigationDrawer;
    private ActivityUtils.Screen mScreen;
    private DrawerLayout mDrawerLayout;
    private Bitmap mBitmap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_game_list);
        initGoogleApi();

        mFab = (FloatingNewGameMenu) findViewById(R.id.fab);
        mFab.setup(this, getRecentGameList(Constants.RECENT_GAMES_NUM));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationDrawer = (NavigationDrawerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navigationDrawer);
        mNavigationDrawer.setup(mDrawerLayout, this, this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        onNavigationClick(null, ActivityUtils.GAMES);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return onCreateOptionsMenu(menu, R.menu.menu_game_list);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mNavigationDrawer.isDrawerToggleSelected(item))
            return true;
        switch (item.getItemId()) {
            case R.id.action_select_all:
                mFab.show(false, true);
                mGameAdapter.selectAll();
                onGameSelect();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Game game;
        boolean updated;
        switch (requestCode) {
            case Constants.GAME_NEW_REQUEST:
                if (resultCode != RESULT_OK) return;
                game = getGame(data.getLongExtra(Game.COLUMN_ID, -1));
                mGameAdapter.add(game);
                mRecyclerView.getLayoutManager().scrollToPosition(0);
                mFab.setup(this, getRecentGameList(Constants.RECENT_GAMES_NUM));
                break;
            case Constants.GAME_SCORE_REQUEST:
                switch (resultCode) {
                    case RESULT_OK: //Archive or delete
                        game = mGameAdapter.remove(data.getLongExtra(Game.COLUMN_ID, -1));
                        int itemId = data.getIntExtra(Game.COLUMN_STATE, -1);
                        Snackbar snackbar = newUndoBar();
                        switch (itemId) {
                            case R.id.action_archive:
                                snackbar.setTag(R.id.action_unarchive);
                                snackbar.text("1 archived");
                                game.setState(Game.State.ARCHIVE);
                                break;
                            case R.id.action_delete:
                                snackbar.setTag(R.id.action_restore);
                                snackbar.text("1 deleted");
                                game.setState(Game.State.DELETE);
                                break;
                        }
                        updateGame(game, Game.COLUMN_STATE);
                        SnackbarManager.show(snackbar);
                        break;
                    case RESULT_FIRST_USER: //Rematch
                        updated = data.getBooleanExtra(Game.COLUMN_UPDATED_DATE, false);
                        if (updated) {
                            mGameAdapter.setGameList(getGameList(mScreen.STATE));
                            mGameAdapter.notifyDataSetChanged();
                        }

                        Intent intent = new Intent(this, GameNewActivity.class);
                        intent.putExtra(Game.COLUMN_ID, data.getLongExtra(Game.COLUMN_ID, -1));
                        startActivityForResult(intent, Constants.GAME_NEW_REQUEST);
                        break;
                    case RESULT_CANCELED: //Updated date
                        updated = data.getBooleanExtra(Game.COLUMN_UPDATED_DATE, false);
                        if (updated) {
                            mGameAdapter.setGameList(getGameList(mScreen.STATE));
                            mGameAdapter.notifyDataSetChanged();
                        }
                        break;
                }
                break;
            case Constants.GAME_HISTORY_REQUEST:
                if (resultCode != RESULT_OK) return;
                game = mGameAdapter.remove(data.getLongExtra(Game.COLUMN_ID, -1));
                int itemId = data.getIntExtra(Game.COLUMN_STATE, -1);
                if (itemId == R.id.action_delete_forever) {
                    deleteGame(game);
                    mFab.setup(GameListActivity.this,
                            getRecentGameList(Constants.RECENT_GAMES_NUM));
                    break;
                }

                Snackbar snackbar = newUndoBar();
                switch (itemId) {
                    case R.id.action_archive:
                        snackbar.setTag(R.id.action_delete);
                        snackbar.text("1 archived");
                        game.setState(Game.State.ARCHIVE);
                        break;
                    case R.id.action_unarchive:
                        snackbar.setTag(R.id.action_archive);
                        snackbar.text("1 unarchived");
                        game.setState(Game.State.NORMAL);
                        break;
                    case R.id.action_delete:
                        snackbar.setTag(R.id.action_archive);
                        snackbar.text("1 deleted");
                        game.setState(Game.State.DELETE);
                        break;
                    case R.id.action_restore:
                        snackbar.setTag(R.id.action_delete);
                        snackbar.text("1 restored");
                        game.setState(Game.State.NORMAL);
                        break;
                }
                updateGame(game, Game.COLUMN_STATE);
                SnackbarManager.show(snackbar);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onNavigationClick(View v, int navigationOption) {
        Intent intent;
        SnackbarManager.dismiss();
        mFab.collapse();
        if (mActionMode != null) mActionMode.finish();

        switch (navigationOption) {
            case ActivityUtils.GAMES:
            case ActivityUtils.ARCHIVE:
            case ActivityUtils.TRASH:
                mDrawerLayout.closeDrawers();
                if (mScreen == ActivityUtils.SCREENS[navigationOption])
                    return;

                mScreen = ActivityUtils.SCREENS[navigationOption];
                mFab.setVisibility(mScreen.FAB_VISIBLE);
                Resources r = getResources();
                mToolbar.setTitle(r.getString(mScreen.TITLE));
                mToolbar.setBackground(new ColorDrawable(r.getColor(mScreen.COLOR_PRIMARY)));
                getWindow().setStatusBarColor(r.getColor(mScreen.COLOR_PRIMARY_DARK));
                mGameAdapter = new GameAdapter(this, getGameList(mScreen.STATE));
                mGameAdapter.setListener(this);
                mRecyclerView.setAdapter(mGameAdapter);
                mNavigationDrawer.setCurrentPosition(navigationOption);

                boolean visible = mGameAdapter.getItemCount() == 0;
                findViewById(R.id.layout).setBackgroundResource(
                        visible ? mScreen.COLOR_ACCENT : android.R.color.transparent);
                findViewById(R.id.emptyLayout).setVisibility(visible ? View.VISIBLE : View.GONE);
                ((ImageView) findViewById(R.id.emptyImage)).setImageResource(mScreen.EMPTY_BACKGROUND);
                ((TextView) findViewById(R.id.emptyHeader)).setText(mScreen.EMPTY_HEADER);
                ((TextView) findViewById(R.id.emptyText)).setText(mScreen.EMPTY_TEXT);
                break;
            case ActivityUtils.SETTINGS:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            case ActivityUtils.HELP:
                intent = new Intent(this, HelpFeedbackActivity.class);
                startActivity(intent);
                FileUtils.saveBitmap(mBitmap, Constants.SCREENSHOT);
                break;
        }
    }

    @Override
    public void onGameSelect() {
        int count = mGameAdapter.getSelectedCount();
        if (count > 0 && mActionMode == null) {
            mActionMode = startSupportActionMode(this);
        } else if (count == 0 && mActionMode != null) {
            mActionMode.finish();
        }
        if (mActionMode != null) {
            mActionMode.setTitle(String.format("%s/%s", count, mGameAdapter.getItemCount()));
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        actionMode.getMenuInflater().inflate(mScreen.ACTION_LAYOUT, menu);
        SnackbarManager.dismiss();
        mFab.show(false, true);
        getWindow().setStatusBarColor(getResources().getColor(R.color.lightBlack));
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(final ActionMode actionMode, MenuItem menuItem) {
        final List<Game> selectedGames = mGameAdapter.getSelectedItems();
        String count = String.valueOf(selectedGames.size());
        String deleteMsg = selectedGames.size() == 1 ? "game" : count + " games";

        Snackbar snackbar = newUndoBar();
        switch (menuItem.getItemId()) {
            case R.id.action_delete_forever:
                DialogInterface.OnClickListener dialogListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        deleteGames(selectedGames);
                                        mGameAdapter.remove();
                                        actionMode.finish();
                                        mFab.setup(GameListActivity.this,
                                                getRecentGameList(Constants.RECENT_GAMES_NUM));
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        };
                new AlertDialog.Builder(this)
                        .setMessage("Delete " + deleteMsg + " forever?")
                        .setPositiveButton("Yes", dialogListener)
                        .setNegativeButton("No", dialogListener).show();
                return true;
            case R.id.action_archive:
                snackbar.setTag(selectedGames.get(0).getState() == Game.State.NORMAL
                        ? R.id.action_unarchive : R.id.action_delete);
                updateGameState(selectedGames, Game.State.ARCHIVE);
                snackbar.text(count + " archived");
                break;
            case R.id.action_unarchive:
                snackbar.setTag(R.id.action_archive);
                updateGameState(selectedGames, Game.State.NORMAL);
                snackbar.text(count + " unarchived");
                break;
            case R.id.action_delete:
                snackbar.setTag(selectedGames.get(0).getState() == Game.State.NORMAL
                        ? R.id.action_restore : R.id.action_archive);
                updateGameState(selectedGames, Game.State.DELETE);
                snackbar.text(count + " moved to Trash");
                break;
            case R.id.action_restore:
                snackbar.setTag(R.id.action_delete);
                updateGameState(selectedGames, Game.State.NORMAL);
                snackbar.text(count + " restored");
                break;
        }

        mGameAdapter.remove();
        mFab.show(true, false);
        actionMode.finish();
        SnackbarManager.show(snackbar);
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        mFab.show(true, true);
        mGameAdapter.clear();
        mActionMode = null;
        getWindow().setStatusBarColor(getResources().getColor(mScreen.COLOR_PRIMARY_DARK));
        updateEmptyView(mScreen);
    }

    @Override
    public void onActionClicked(Snackbar snackbar) {
        List<Game> mUndoGames = mGameAdapter.getUndoItems();
        switch ((int) snackbar.getTag()) {
            case R.id.action_archive:
                updateGameState(mUndoGames, Game.State.ARCHIVE);
                break;
            case R.id.action_delete:
                updateGameState(mUndoGames, Game.State.DELETE);
                break;
            case R.id.action_unarchive:
            case R.id.action_restore:
                updateGameState(mUndoGames, Game.State.NORMAL);
                break;
        }
        mGameAdapter.add(mUndoGames);
        updateEmptyView(mScreen);
    }

    @Override
    public void onDrawerOpened(Parcelable parcelable) {
        if (parcelable instanceof Bitmap) {
            mBitmap = (Bitmap) parcelable;
        }
    }

    @Override
    public void onBackPressed() {
        if (mFab.isExpanded()) {
            mFab.collapse();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);
        mNavigationDrawer.setupAdditionalInfo(getPerson(), getEmail());
    }

    private Snackbar newUndoBar() {
        return Snackbar.with(this).actionLabel("Undo").actionListener(this)
                .eventListener(new com.nispok.snackbar.listeners.EventListener() {

                    @Override
                    public void onShow(Snackbar snackbar) {
                        mFab.move(-snackbar.getHeight());
                    }

                    @Override
                    public void onShowByReplace(Snackbar snackbar) {
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {
                    }

                    @Override
                    public void onDismiss(Snackbar snackbar) {
                        mFab.move(snackbar.getHeight());
                    }

                    @Override
                    public void onDismissByReplace(Snackbar snackbar) {
                    }

                    @Override
                    public void onDismissed(Snackbar snackbar) {
                    }
                })
                .attachToRecyclerView(mRecyclerView).swipeToDismiss(false);
    }

    private void updateEmptyView(ActivityUtils.Screen screen) {
        boolean visible = mGameAdapter.getItemCount() == 0;
        findViewById(R.id.layout).setBackgroundResource(
                visible ? screen.COLOR_ACCENT : android.R.color.transparent);
        findViewById(R.id.emptyLayout).setVisibility(visible ? View.VISIBLE : View.GONE);
        ((ImageView) findViewById(R.id.emptyImage)).setImageResource(screen.EMPTY_BACKGROUND);
        ((TextView) findViewById(R.id.emptyHeader)).setText(screen.EMPTY_HEADER);
        ((TextView) findViewById(R.id.emptyText)).setText(screen.EMPTY_TEXT);
    }

    private void updateGameState(List<Game> gameList, Game.State state) {
        for (Game g : gameList) {
            g.setState(state);
            updateGame(g, Game.COLUMN_STATE);
        }
    }

    //TODO: Animate toolbar color change
//    private void revealImageCircular(final int primary) {
//        final View view = mToolbar;
//        view.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                int x = view.getWidth() / 2;
//                int y = view.getHeight() / 2;
//                Animator anim = ViewAnimationUtils.createCircularReveal(view, x, y, 0, x);
//                anim.setDuration(200);
//                anim.addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//                        super.onAnimationStart(animation);
//                        mToolbar.setBackground(new ColorDrawable(primary));
//                    }
//                });
//                anim.start();
//            }
//        }, 100);
//    }
}
