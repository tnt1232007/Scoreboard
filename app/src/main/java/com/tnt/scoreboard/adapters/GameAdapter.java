package com.tnt.scoreboard.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tnt.scoreboard.GameHistoryActivity;
import com.tnt.scoreboard.GameScoreActivity;
import com.tnt.scoreboard.R;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class GameAdapter extends RecyclerView.Adapter<GameViewHolder> {

    private final Activity mActivity;
    private List<Game> mGameList;
    private List<Game> mUndoGames;
    private TreeSet<Game> mSelectedGames;
    private IOnGameSelectListener mListener;

    public GameAdapter(Activity activity, List<Game> gameList) {
        //TODO: group games by date
        mActivity = activity;
        mGameList = gameList;
        mSelectedGames = new TreeSet<>();
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(final GameViewHolder holder, int position) {
        final Game game = mGameList.get(position);
        game.setIndex(position);
        holder.updateData(game);
        holder.updateState(game, mSelectedGames.contains(game));
        holder.setListener(new GameViewHolder.IOnGameClickListener() {
            @Override
            public void onGameClick(View v, boolean isCheckClick) {
                if (isCheckClick || getSelectedCount() > 0) {
                    holder.updateState(game, select(game));
                    mListener.onGameSelect();
                } else if (game.getState() == Game.State.NORMAL) {
                    Intent intent = new Intent(mActivity, GameScoreActivity.class);
                    intent.putExtra(Game.COLUMN_ID, game.getId());
                    mActivity.startActivityForResult(intent, Constants.GAME_SCORE_REQUEST);
                } else {
                    Intent intent = new Intent(mActivity, GameHistoryActivity.class);
                    intent.putExtra(Game.COLUMN_ID, game.getId());
                    mActivity.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGameList.size();
    }

    //<editor-fold desc="Adapter items handler">
    public void add(Game game) {
        mGameList.add(game);
        game.setIndex(mGameList.size() - 1);
        notifyItemInserted(game.getIndex());
    }

    public void add(List<Game> gameList) {
        Collections.sort(gameList);
        for (Game g : gameList) {
            mGameList.add(g.getIndex(), g);
            notifyItemInserted(g.getIndex());
        }
        refreshIndex();
    }

    public Game remove(long gameId) {
        Game game = null;
        for (Game g : mGameList) {
            if (g.getId() == gameId) {
                game = g;
                break;
            }
        }
        if (game == null) return null;
        mSelectedGames.clear();
        mUndoGames = new ArrayList<>();
        mUndoGames.add(game);
        mGameList.remove(game);
        notifyItemRemoved(game.getIndex());
        refreshIndex();
        return game;
    }

    public void remove() {
        Iterator iterator = mSelectedGames.descendingIterator();
        while (iterator.hasNext()) {
            Game g = (Game) iterator.next();
            mGameList.remove(g);
            notifyItemRemoved(g.getIndex());
        }
        mUndoGames = new ArrayList<>(mSelectedGames);
        mSelectedGames.clear();
        refreshIndex();
    }

    public boolean select(Game game) {
        if (mSelectedGames.contains(game)) {
            mSelectedGames.remove(game);
            return false;
        } else {
            mSelectedGames.add(game);
            return true;
        }
    }

    public void clear() {
        if (mSelectedGames.isEmpty()) return;
        mSelectedGames.clear();
        notifyDataSetChanged();
    }

    private void refreshIndex() {
        int i = 0;
        for (Game g : mGameList) {
            g.setIndex(i++);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Getter Setter">
    public int getSelectedCount() {
        return mSelectedGames.size();
    }

    public List<Game> getUndoItems() {
        return mUndoGames;
    }

    public List<Game> getSelectedItems() {
        return new ArrayList<>(mSelectedGames);
    }

    public void setListener(IOnGameSelectListener listener) {
        mListener = listener;
    }
    //</editor-fold>

    public interface IOnGameSelectListener {

        public void onGameSelect();
    }
}

