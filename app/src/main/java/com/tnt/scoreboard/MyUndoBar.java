package com.tnt.scoreboard;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cocosw.undobar.UndoBarController;
import com.tnt.scoreboard.adapters.GameAdapter;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.utils.ActivityUtils;

import java.util.List;


public class MyUndoBar extends UndoBarController.UndoBar {

    private GameListActivity mActivity;

    public MyUndoBar(GameListActivity activity) {
        super(activity);
        mActivity = activity;
    }

    public void setup(final int actionId, final GameAdapter gameAdapter,
                      final MyNewGameButton fab, final ActivityUtils.Screen screen) {
        UndoBarController.UndoListener listener = new UndoBarController.AdvancedUndoListener() {
            private List<Game> mStoredGames = gameAdapter.getSelectedItems();

            @Override
            public void onUndo(@Nullable Parcelable token) {
                switch (actionId) {
                    case R.id.action_archive:
                        mActivity.changeState(mStoredGames, Game.State.NORMAL);
                        break;
                    case R.id.action_unarchive:
                        mActivity.changeState(mStoredGames, Game.State.ARCHIVE);
                        break;
                    case R.id.action_delete:
                        mActivity.changeState(mStoredGames, Game.State.NORMAL);
                        break;
                    case R.id.action_restore:
                        mActivity.changeState(mStoredGames, Game.State.DELETE);
                        break;
                }
                gameAdapter.add(mStoredGames);
                mActivity.showEmptyView(gameAdapter.getItemCount() == 0, screen);
                fab.move(false);
            }

            @Override
            public void onHide(@Nullable Parcelable parcelable) {
                fab.move(false);
            }

            @Override
            public void onClear(@NonNull Parcelable[] parcelables) {
                fab.move(false);
            }
        };
        noicon(true).listener(listener);
    }
}
