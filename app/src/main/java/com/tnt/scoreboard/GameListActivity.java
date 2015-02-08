package com.tnt.scoreboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.cocosw.undobar.UndoBarController;
import com.tnt.scoreboard.adapters.GameAdapter;
import com.tnt.scoreboard.adapters.NavigationViewHolder;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.utils.ActivityUtils;
import com.tnt.scoreboard.utils.FileUtils;

import java.util.List;

//TODO: Swipe to archive/delete
public class GameListActivity extends BaseActivity implements
        NavigationViewHolder.IOnNavigationClickListener,
        GameAdapter.IOnGameSelectListener,
        ActionMode.Callback,
        UndoBarController.AdvancedUndoListener {

    public static final String ACTION = "action";
    public static final String SCREEN = "screen";
    public static final int RECENT_GAMES_NUM = 3;

    private ActionMode mActionMode;
    private GameAdapter mGameAdapter;
    private FloatingNewGameMenu mFab;
    private RecyclerView mRecyclerView;
    private UndoBarController.UndoBar mUndoBar;
    private NavigationDrawerFragment mNavigationDrawer;
    private ActivityUtils.Screen mScreen;
    private DrawerLayout mDrawerLayout;
    private Bitmap mBitmap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_game_list);
        mUndoBar = new UndoBarController.UndoBar(this);

        mFab = (FloatingNewGameMenu) findViewById(R.id.fab);
        mFab.setup(this, getRecentGameList(RECENT_GAMES_NUM));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationDrawer = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigationDrawer);
        mNavigationDrawer.setup(mDrawerLayout, this);

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
            case FloatingNewGameMenu.NEW_GAME_REQUEST:
                long gameId = data.getLongExtra(Game.COLUMN_ID, -1);
                mGameAdapter.add(getGame(gameId));
                mFab.setup(this, getRecentGameList(RECENT_GAMES_NUM));
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onNavigationClick(View v, int navigationOption) {
        Intent intent;
        mUndoBar.clear();
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
                mGameAdapter = new GameAdapter(getGameList(mScreen.STATE));
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
                FileUtils.saveBitmap(mBitmap, HelpFeedbackActivity.SCREENSHOT);
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
            mActionMode.setTitle(String.valueOf(count));
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        actionMode.getMenuInflater().inflate(mScreen.MENU_LAYOUT, menu);
        mUndoBar.clear();
        mFab.show(false);
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
        Bundle bundle = new Bundle();
        bundle.putInt(ACTION, menuItem.getItemId());
        bundle.putParcelable(SCREEN, mScreen);
        mUndoBar.token(bundle).noicon(true).listener(this);

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
                                        mFab.move(true);
                                        actionMode.finish();
                                        mFab.setup(GameListActivity.this, getRecentGameList(RECENT_GAMES_NUM));
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
                changeState(selectedGames, Game.State.ARCHIVE);
                mUndoBar.message(count + " archived");
                break;
            case R.id.action_unarchive:
                changeState(selectedGames, Game.State.NORMAL);
                mUndoBar.message(count + " unarchived");
                break;
            case R.id.action_delete:
                changeState(selectedGames, Game.State.DELETE);
                mUndoBar.message(count + " moved to Trash");
                break;
            case R.id.action_restore:
                changeState(selectedGames, Game.State.NORMAL);
                mUndoBar.message(count + " restored");
                break;
        }

        mGameAdapter.remove();
        mUndoBar.show();
        mFab.move(true);
        actionMode.finish();
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        mFab.show(true);
        mGameAdapter.clear();
        mActionMode = null;
        getWindow().setStatusBarColor(getResources().getColor(mScreen.COLOR_PRIMARY_DARK));
        updateEmptyView(mScreen);
    }

    @Override
    public void onHide(@Nullable Parcelable parcelable) {
        mFab.move(false);
    }

    @Override
    public void onClear(@NonNull Parcelable[] parcelables) {
        mFab.move(false);
    }

    @Override
    public void onUndo(@Nullable Parcelable parcelable) {
        if (parcelable == null) return;
        int actionId = ((Bundle) parcelable).getInt(ACTION);
        ActivityUtils.Screen screen = ((Bundle) parcelable).getParcelable(SCREEN);
        List<Game> mUndoGames = mGameAdapter.getUndoItems();
        switch (actionId) {
            case R.id.action_archive:
                changeState(mUndoGames, Game.State.NORMAL);
                break;
            case R.id.action_unarchive:
                changeState(mUndoGames, Game.State.ARCHIVE);
                break;
            case R.id.action_delete:
                changeState(mUndoGames, Game.State.NORMAL);
                break;
            case R.id.action_restore:
                changeState(mUndoGames, Game.State.DELETE);
                break;
        }
        mGameAdapter.add(mUndoGames);
        updateEmptyView(screen);
        mFab.move(false);
    }

    @Override
    public void onDrawerOpened(Parcelable parcelable) {
        super.onDrawerOpened(parcelable);
        if (parcelable instanceof Bitmap) {
            mBitmap = (Bitmap) parcelable;
        }
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

    //<editor-fold desc="TODO: Animate toolbar color change">
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
    //</editor-fold>
}
