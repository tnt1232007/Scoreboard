package com.tnt.scoreboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.tnt.scoreboard.adapters.GameAdapter;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.utils.ActivityUtils;

import java.util.List;


public class MyActionMode implements ActionMode.Callback {

    private final GameListActivity mActivity;
    private final MyUndoBar mUndoBar;
    private final MyNewGameButton mFab;
    private final GameAdapter mGameAdapter;
    private final ActivityUtils.Screen mScreen;

    public MyActionMode(GameListActivity activity, ActivityUtils.Screen screen) {
        mActivity = activity;
        mScreen = screen;
        mGameAdapter = activity.getGameAdapter();
        mUndoBar = activity.getUndoBar();
        mFab = activity.getFab();
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(mScreen.MENU_LAYOUT, menu);
        mUndoBar.clear();
        mFab.show(false);
        mActivity.getWindow().setStatusBarColor(
                mActivity.getResources().getColor(R.color.lightBlack));
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
        final List<Game> selectedGames = mGameAdapter.getSelectedItems();
        String msg = selectedGames.size() + (selectedGames.size() == 1 ? " game" : " games");
        mUndoBar.setup(item.getItemId(), mGameAdapter, mFab, mScreen);

        switch (item.getItemId()) {
            case R.id.action_delete_forever:
                DialogInterface.OnClickListener dialogListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        mActivity.deleteGames(selectedGames);
                                        mGameAdapter.remove();
                                        mFab.move(true);
                                        mode.finish();
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        };
                new AlertDialog.Builder(mActivity)
                        .setMessage("Delete " + msg + " forever?")
                        .setPositiveButton("Yes", dialogListener)
                        .setNegativeButton("No", dialogListener).show();
                return true;
            case R.id.action_archive:
                mActivity.changeState(selectedGames, Game.State.ARCHIVE);
                mUndoBar.message(msg + " archived");
                break;
            case R.id.action_unarchive:
                mActivity.changeState(selectedGames, Game.State.NORMAL);
                mUndoBar.message(msg + " unarchived");
                break;
            case R.id.action_delete:
                mActivity.changeState(selectedGames, Game.State.DELETE);
                mUndoBar.message(msg + " moved to Trash");
                break;
            case R.id.action_restore:
                mActivity.changeState(selectedGames, Game.State.NORMAL);
                mUndoBar.message(msg + " restored");
                break;
        }

        mGameAdapter.remove();
        mUndoBar.show();
        mFab.move(true);
        mode.finish();
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mFab.show(true);
        mGameAdapter.clear();
        mActivity.mActionMode = null;
        mActivity.getWindow().setStatusBarColor(
                mActivity.getResources().getColor(mScreen.COLOR_PRIMARY_DARK));
        mActivity.showEmptyView(mGameAdapter.getItemCount() == 0, mScreen);
    }
}
