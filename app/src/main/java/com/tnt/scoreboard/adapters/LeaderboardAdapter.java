package com.tnt.scoreboard.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.tnt.scoreboard.R;
import com.tnt.scoreboard.models.Player;
import com.tnt.scoreboard.utils.Constants;

import java.util.List;

public class LeaderboardAdapter
        extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    private final List<Player> mPlayerList;

    public LeaderboardAdapter(List<Player> playerList) {
        this.mPlayerList = playerList;
    }

    @Override
    public LeaderboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaderboard, parent, false);
        return new LeaderboardViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(LeaderboardViewHolder holder, int position) {
        position = position + Constants.TOP_PLAYERS;
        Player player = mPlayerList.get(position);
        holder.updateData(player, position + 1);
    }

    @Override
    public int getItemCount() {
        return mPlayerList.size() - Constants.TOP_PLAYERS;
    }

    static class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        private final TextDrawable.IBuilder mAvatarBuilder = TextDrawable.builder()
                .beginConfig().bold().endConfig().round();
        private final ImageView mRank;
        private final TextView mPlayer, mScore;

        public LeaderboardViewHolder(final View itemView) {
            super(itemView);
            mRank = (ImageView) itemView.findViewById(R.id.rank);
            mPlayer = (TextView) itemView.findViewById(R.id.player);
            mScore = (TextView) itemView.findViewById(R.id.score);
        }

        public void updateData(Player player, int rank) {
            mRank.setImageDrawable(mAvatarBuilder.build(
                    String.valueOf(rank), (int) player.getColor()));
            mPlayer.setText(player.getName());
            mScore.setText(String.valueOf(player.getScore()));
        }
    }
}
