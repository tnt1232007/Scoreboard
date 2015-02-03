package com.tnt.scoreboard.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tnt.scoreboard.GameScoreActivity;
import com.tnt.scoreboard.R;
import com.tnt.scoreboard.models.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;


public class GameAdapter extends RecyclerView.Adapter<GameViewHolder> {

    private List<Game> mGameList;
    private TreeSet<Game> mSelectedGames;
    private IOnSelectListener mListener;

    public GameAdapter(List<Game> gameList) {
        this.mGameList = gameList;
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
        holder.updateState(game, false);
        holder.setListener(new GameViewHolder.IOnGameClickListener() {
            @Override
            public void onGameClick(View v, boolean isCheckClick) {
                if (isCheckClick || getSelectedCount() > 0) {
                    holder.updateState(game, select(game));
                    mListener.onSelect();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, GameScoreActivity.class);
                    intent.putExtra(Game.COLUMN_ID, game.getId());
                    context.startActivity(intent);
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
        Log.d("Add game", game.getIndex() + "");
    }

    public void add(List<Game> gameList) {
        Collections.sort(gameList);
        for (Game g : gameList) {
            mGameList.add(g.getIndex(), g);
            notifyItemInserted(g.getIndex());
            Log.d("Add game", g.getIndex() + "");
        }
        refreshIndex();
    }

    public void remove() {
        Iterator iterator = mSelectedGames.descendingIterator();
        while (iterator.hasNext()) {
            Game g = (Game) iterator.next();
            mGameList.remove(g);
            notifyItemRemoved(g.getIndex());
            Log.d("Rem game", g.getIndex() + "");
        }
        mSelectedGames.clear();
        refreshIndex();
    }

    public boolean select(Game game) {
        if (mSelectedGames.contains(game)) {
            mSelectedGames.remove(game);
            Log.d("Des", game.getIndex() + "");
            return false;
        } else {
            mSelectedGames.add(game);
            Log.d("Sel", game.getIndex() + "");
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

    public List<Game> getSelectedItems() {
        return new ArrayList<>(mSelectedGames);
    }
    //</editor-fold>

    //<editor-fold desc="Getter Setter">
    public void setListener(IOnSelectListener listener) {
        mListener = listener;
    }
    //</editor-fold>

    public interface IOnSelectListener {

        public void onSelect();
    }
}

