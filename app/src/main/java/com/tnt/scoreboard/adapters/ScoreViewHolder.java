package com.tnt.scoreboard.adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tnt.scoreboard.R;
import com.tnt.scoreboard.models.Score;

public class ScoreViewHolder extends RecyclerView.ViewHolder {

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
