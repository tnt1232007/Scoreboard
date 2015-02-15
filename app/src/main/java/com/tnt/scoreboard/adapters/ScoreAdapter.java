package com.tnt.scoreboard.adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tnt.scoreboard.R;
import com.tnt.scoreboard.models.Score;

import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    public static final int SIZE = 5;
    private List<Score> mScoreList;

    public ScoreAdapter(List<Score> scoreList) {
        mScoreList = scoreList;
    }

    @Override
    public ScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_score, parent, false);
        return new ScoreViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ScoreViewHolder holder, int position) {
        int index = mScoreList.size() - getItemCount() + position;
        Score score = mScoreList.get(index);
        holder.updateData(score, index);
    }

    @Override
    public int getItemCount() {
        return mScoreList.size() < SIZE ? mScoreList.size() : SIZE;
    }

    static class ScoreViewHolder extends RecyclerView.ViewHolder {

        private final TextView mOrder, mScore;

        public ScoreViewHolder(View itemView) {
            super(itemView);
            mOrder = (TextView) itemView.findViewById(R.id.order);
            mScore = (TextView) itemView.findViewById(R.id.score);
        }

        public void updateData(Score score, int index) {
            Resources r = itemView.getResources();
            long s = score.getScore();
            int color = s >= 0 ? r.getColor(R.color.green) : r.getColor(R.color.red);
            mOrder.setText((index + 1) + ".");
            mScore.setText((s > 0 ? "+" : "") + s);
            mScore.setTextColor(color);
        }
    }
}
