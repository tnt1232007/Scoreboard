package com.tnt.scoreboard.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.tnt.scoreboard.GameHistoryActivity;
import com.tnt.scoreboard.GameScoreActivity;
import com.tnt.scoreboard.R;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.models.Player;
import com.tnt.scoreboard.utils.Constants;
import com.tnt.scoreboard.utils.DateTimeUtils;
import com.tnt.scoreboard.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private final Activity mActivity;
    private List<Game> mGameList;
    private List<Game> mUndoGames;
    private TreeSet<Game> mSelectedGames;
    private IOnGameSelectListener mListener;

    public GameAdapter(Activity activity, List<Game> gameList) {
        mActivity = activity;
        mGameList = gameList;
        mSelectedGames = new TreeSet<>();
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
                    mActivity.startActivityForResult(intent, Constants.GAME_HISTORY_REQUEST);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGameList.size();
    }

    public int getSelectedCount() {
        return mSelectedGames.size();
    }

    public List<Game> getUndoItems() {
        return mUndoGames;
    }

    public List<Game> getSelectedItems() {
        return new ArrayList<>(mSelectedGames);
    }

    //<editor-fold desc="Adapter items handler">
    public void add(Game game) {
        game.setIndex(0);
        mGameList.add(game.getIndex(), game);
        notifyItemInserted(game.getIndex());
        refreshIndex();
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

    public void setGameList(List<Game> gameList) {
        mGameList = gameList;
    }

    public void setListener(IOnGameSelectListener listener) {
        mListener = listener;
    }

    public interface IOnGameSelectListener {

        public void onGameSelect();
    }

    static class GameViewHolder extends RecyclerView.ViewHolder {

        private final int mCheckColor;
        private final TextDrawable.IBuilder mDrawableBuilder = TextDrawable.builder().round();
        private final ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
        private final ImageView mIcon, mCheck;
        private final TextView mPlayerName, mNumberOfRounds, mDateTime;

        private IOnGameClickListener mListener;
        private boolean isCheckClick;

        public GameViewHolder(View itemView) {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(R.id.icon);
            mCheck = (ImageView) itemView.findViewById(R.id.check);
            mPlayerName = (TextView) itemView.findViewById(R.id.playerName);
            mNumberOfRounds = (TextView) itemView.findViewById(R.id.numberOfRounds);
            mDateTime = (TextView) itemView.findViewById(R.id.dateTime);
            mCheckColor = itemView.getResources().getColor(R.color.grayDark);

            mIcon.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    isCheckClick = true;
                    return false;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onGameClick(v, isCheckClick);
                    isCheckClick = false;
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.onGameClick(v, true);
                    return true;
                }
            });
        }

        public void updateData(Game game) {
            if (game == null) return;
            mPlayerName.setText(StringUtils.join(game.getPlayerList(), ", "));
            mNumberOfRounds.setText(game.getNumberOfRounds() + " Rounds");
            mDateTime.setText(DateTimeUtils.formatPretty(game.getUpdatedDate()));
        }

        public void updateState(Game game, boolean selected) {
            if (selected) {
                mIcon.setImageDrawable(mDrawableBuilder.build(" ", mCheckColor));
                itemView.setSelected(true);
                mCheck.setVisibility(View.VISIBLE);
            } else {
                List<Player> players = game.getPlayerList();
                Context context = itemView.getContext();
                String s1 = String.format("%s%s", players.size(),
                        StringUtils.getInitial(context, players.get(0).getName()));
                String s2 = String.format("%s%s", players.size(),
                        StringUtils.getInitial(context, players.get(1).getName()));
                mIcon.setImageDrawable(mDrawableBuilder.build(s1, mColorGenerator.getColor(s2)));
                itemView.setSelected(false);
                mCheck.setVisibility(View.GONE);
            }
        }

        public void setListener(IOnGameClickListener listener) {
            this.mListener = listener;
        }

        public interface IOnGameClickListener {

            public void onGameClick(View v, boolean isCheckClick);
        }
    }
}

