package com.tnt.scoreboard.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tnt.scoreboard.R;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.models.Player;
import com.tnt.scoreboard.utils.Constants;
import com.tnt.scoreboard.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.InfoViewHolder> {

    private final Game mGame;
    private final List<Player> mPlayerList;

    public InfoAdapter(Game game) {
        mGame = game;
        mPlayerList = new ArrayList<>(game.getPlayerList());
        Collections.sort(mPlayerList);
    }

    @Override
    public InfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case Constants.TYPE_HEADER:
                layout = inflater.inflate(R.layout.item_info_header, parent, false);
                break;
            case Constants.TYPE_ITEM:
                layout = inflater.inflate(R.layout.item_info, parent, false);
                break;
        }
        return new InfoViewHolder(layout, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? Constants.TYPE_HEADER : Constants.TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(InfoViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case Constants.TYPE_HEADER:
                holder.updateHeader(mGame);
                break;
            case Constants.TYPE_ITEM:
                int index = position - 1;
                Player player = mPlayerList.get(index);
                holder.updateData(player, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mPlayerList.size() + 1;
    }

    static class InfoViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle, mSubTitle, mStart, mEnd,
                mCreated, mState, mRank, mPlayer, mScore;

        public InfoViewHolder(final View itemView, int viewType) {
            super(itemView);
            switch (viewType) {
                case Constants.TYPE_HEADER:
                    mTitle = (TextView) itemView.findViewById(R.id.title);
                    mSubTitle = (TextView) itemView.findViewById(R.id.subTitle);
                    mStart = (TextView) itemView.findViewById(R.id.start);
                    mEnd = (TextView) itemView.findViewById(R.id.end);
                    mCreated = (TextView) itemView.findViewById(R.id.created);
                    mState = (TextView) itemView.findViewById(R.id.state);
                    break;
                case Constants.TYPE_ITEM:
                    mRank = (TextView) itemView.findViewById(R.id.rank);
                    mPlayer = (TextView) itemView.findViewById(R.id.player);
                    mScore = (TextView) itemView.findViewById(R.id.score);
                    break;
            }
        }

        public void updateHeader(Game game) {
            mTitle.setText(game.getTitle());
            mSubTitle.setText(" Updated " + DateTimeUtils.formatPretty(game.getUpdatedDate()));
            mState.setText(game.getState() == Game.State.NORMAL ? "Ongoing"
                    : game.getState() == Game.State.ARCHIVE ? "Archived" : "Trash");
            mStart.setText(String.valueOf(game.getStartingScore()));
            mEnd.setText(String.valueOf(game.getEndingScore()));
            mCreated.setText(game.getCreatedDate().toString("MMM dd, yyyy"));
        }

        public void updateData(Player player, int rank) {
            mRank.setText(String.valueOf(rank));
            mPlayer.setText(player.getName());
            mScore.setText(String.valueOf(player.getScore()));
        }
    }
}
