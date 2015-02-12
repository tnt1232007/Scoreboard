package com.tnt.scoreboard.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tnt.scoreboard.R;
import com.tnt.scoreboard.models.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerViewHolder> {

    private List<Player> mPlayerList;
    private List<Player> mSortedList;
    private int mMaxScore;

    public PlayerAdapter(List<Player> playerList) {
        this.mPlayerList = playerList;
        mSortedList = new ArrayList<>(mPlayerList);
        Collections.sort(mSortedList);
        mMaxScore = getMaxScore();
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_player, parent, false);
        return new PlayerViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        Player player = mPlayerList.get(position);
        holder.updateData(player, getRank(player), (int) (player.getScore() * 100 / mMaxScore));
    }

    @Override
    public int getItemCount() {
        return mPlayerList.size();
    }

    private int getRank(Player player) {
        return mSortedList.indexOf(player) + 1;
    }

    private int getMaxScore() {
        long max = mPlayerList.get(0).getScore();
        for (int i = 1; i < mPlayerList.size(); i++) {
            long score = mPlayerList.get(i).getScore();
            if (max < score)
                max = score;
        }
        int result = (int) Math.pow(10, Math.ceil(Math.log10(max)));
        return result == 0 ? 100 : result;
    }
}
