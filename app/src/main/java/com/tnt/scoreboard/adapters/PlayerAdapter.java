package com.tnt.scoreboard.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tnt.scoreboard.R;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.models.Player;
import com.tnt.scoreboard.models.Score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerViewHolder> {

    private final long mEndingScore;
    private final boolean mIsFirstToWin, mIsInfinite;
    private List<Player> mPlayerList;
    private IOnScoreUpdateListener mListener;

    public PlayerAdapter(Game game) {
        this.mPlayerList = game.getPlayers();
        mEndingScore = game.getEndingScore();
        mIsFirstToWin = game.isFirstToWin();
        mIsInfinite = game.isInfinite();
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_player, parent, false);
        return new PlayerViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(final PlayerViewHolder holder, final int position) {
        final Player player = mPlayerList.get(position);
        holder.updateData(player, getRank(player),
                (int) (player.getScore() * 100 / getMaxScoreAndCheck()));
        holder.setListener(new PlayerViewHolder.IOnScoreUpdateListener() {
            @Override
            public void onAdded(Score score) {
                score = mListener.onAdded(player, score);
                player.getScoreList().add(score);
                player.setScore(player.getScore() + score.getScore());
                mListener.onUpdated(notifyAndGetMaxRound());
            }

            @Override
            public void onDeleted() {
                if (player.getScoreList().size() == 0)
                    return;

                Score score = mListener.onDeleted(player);
                player.getScoreList().remove(score);
                player.setScore(player.getScore() - score.getScore());
                mListener.onUpdated(notifyAndGetMaxRound());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlayerList.size();
    }

    private int getRank(Player player) {
        List<Player> sortedList = new ArrayList<>(mPlayerList);
        Collections.sort(sortedList);
        if (!mIsFirstToWin)
            Collections.reverse(sortedList);
        return sortedList.indexOf(player) + 1;
    }

    private long getMaxScoreAndCheck() {
        List<Integer> championList = new ArrayList<>();
        long max = mPlayerList.get(0).getScore();
        championList.add(0);
        for (int i = 1; i < mPlayerList.size(); i++) {
            long score = mPlayerList.get(i).getScore();
            if (max < score) {
                max = score;
                championList.clear();
                championList.add(i);
            } else if (max == score) {
                championList.add(i);
            }
        }
        long result = (long) Math.pow(10, Math.ceil(Math.log10(max)));
        if (max >= mEndingScore) {
            if (!mIsInfinite)
                mListener.onEnded(championList);
            return result;
        } else return mEndingScore;
    }

    private int notifyAndGetMaxRound() {
        int max = mPlayerList.get(0).getScoreList().size();
        for (int i = 0; i < mPlayerList.size(); i++) {
            notifyItemChanged(i);
            int round = mPlayerList.get(i).getScoreList().size();
            if (max < round)
                max = round;
        }
        return max;
    }

    public void setListener(IOnScoreUpdateListener listener) {
        mListener = listener;
    }

    public interface IOnScoreUpdateListener {
        public Score onAdded(Player player, Score score);

        public Score onDeleted(Player player);

        public void onUpdated(int round);

        public void onEnded(List<Integer> championList);
    }
}
