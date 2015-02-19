package com.tnt.scoreboard.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tnt.scoreboard.R;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.models.Player;
import com.tnt.scoreboard.models.Score;
import com.tnt.scoreboard.utils.Constants;
import com.tnt.scoreboard.utils.StringUtils;

import java.util.List;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder> {

    private static final int ROW_DELTA = 2;
    private final Game mGame;
    private final int mCols;
    private final int mRows;

    public SummaryAdapter(Game game) {
        mGame = game;
        mCols = (int) (mGame.getNumberOfRounds() + 2);
        mRows = (int) (mGame.getNumberOfPlayers() + ROW_DELTA);
    }

    @Override
    public SummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case Constants.TYPE_HEADER:
                layout = inflater.inflate(R.layout.item_summary_header, parent, false);
                break;
            case Constants.TYPE_FOOTER:
                layout = inflater.inflate(R.layout.item_summary_footer, parent, false);
                break;
            case Constants.TYPE_ITEM:
                layout = inflater.inflate(R.layout.item_summary, parent, false);
                break;
        }
        return new SummaryViewHolder(layout, viewType);
    }

    @Override
    public void onBindViewHolder(SummaryViewHolder holder, int position) {
        int row = position % mRows;
        int col = position / mRows;
        List<Player> playerList = mGame.getPlayerList();

        //Empty first cell
        if (row == 0 && col == 0) {
            holder.updateData(null, null);
            return;
        }

        //Round number Header
        if (row == 0) {
            holder.updateData(col == mCols - 1 ? "Final"
                    : "R" + StringUtils.padLeft(col, '0', (int) Math.log10(mCols) + 1), null);
            return;
        }

        //Player name Header
        if (col == 0) {
            holder.updateData(row == mRows - 1 ? "\u03A3"
                    : playerList.get(row - 1).getName(), null);
            return;
        }

        //Empty last cell
        if (row == mRows - 1 && col == mCols - 1) {
            holder.updateData(null, null);
            return;
        }

        //Score sum Footer
        if (row == mRows - 1) {
            long incrementScore = 0;
            for (int i = 0; i < playerList.size(); i++) {
                List<Score> scoreList = playerList.get(i).getScoreList();
                incrementScore += scoreList.get(Math.min(col - 1, scoreList.size() - 1)).getScore();
            }
            holder.updateData(addPlus(incrementScore), null);
            return;
        }

        //Final score Footer OR the last few columns with no change in score
        List<Score> scoreList = playerList.get(row - 1).getScoreList();
        if (col == mCols - 1 || col > scoreList.size()) {
            holder.updateData(playerList.get(row - 1).getScore(), null);
            return;
        }

        //Normal summary cell
        Score score = scoreList.get(Math.min(col - 1, scoreList.size() - 1));
        holder.updateData(score.getCurrentScore(), addPlus(score.getScore()));
    }

    @Override
    public int getItemCount() {
        return mCols * mRows;
    }

    @Override
    public int getItemViewType(int position) {
        int row = position % mRows;
        int col = position / mRows;
        return (col == 0 || row == 0) && col + row != 0
                ? Constants.TYPE_HEADER
                : row == mRows - 1 || col == mCols - 1
                ? Constants.TYPE_FOOTER
                : Constants.TYPE_ITEM;
    }

    private String addPlus(long score) {
        return score >= 0 ? "+" + score : String.valueOf(score);
    }

    static class SummaryViewHolder extends RecyclerView.ViewHolder {

        private TextView mTotalScore, mIncrementScore;

        public SummaryViewHolder(final View itemView, int viewType) {
            super(itemView);
            mTotalScore = (TextView) itemView.findViewById(R.id.totalScore);
            switch (viewType) {
                case Constants.TYPE_HEADER:
                    break;
                case Constants.TYPE_ITEM:
                    mIncrementScore = (TextView) itemView.findViewById(R.id.incrementScore);
                    break;
            }
        }

        public void updateData(Object totalScore, Object incrementScore) {
            if (mTotalScore != null)
                mTotalScore.setText(totalScore == null
                        ? "" : String.valueOf(totalScore));
            if (mIncrementScore != null)
                mIncrementScore.setText(incrementScore == null
                        ? "" : String.valueOf(incrementScore));
        }
    }
}
