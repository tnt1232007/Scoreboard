package com.tnt.scoreboard.adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.tnt.scoreboard.R;
import com.tnt.scoreboard.models.Score;

public class ScoreViewHolder extends RecyclerView.ViewHolder {

    private final TextView mScore;

    public ScoreViewHolder(View itemView) {
        super(itemView);
        mScore = (TextView) itemView.findViewById(R.id.score);
    }

    public void updateData(Score score, boolean isLatest) {
        Resources r = itemView.getResources();
        long s = score.getScore();
        int color = s >= 0 ? r.getColor(R.color.green) : r.getColor(R.color.red);
        mScore.setText((s > 0 ? "+" : "") + s);
        mScore.setTextColor(color);
        mScore.setTextSize(TypedValue.COMPLEX_UNIT_SP, isLatest ? 16 : 8);
    }
}
