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

    private final int mCols;
    private final int mRows;
    private Player[] mPlayerList;
    private int[] mSumList;

    public SummaryAdapter(Game game) {
        int numberOfRounds = (int) game.getNumberOfRounds();
        int numberOfPlayers = (int) game.getNumberOfPlayers();
        mCols = numberOfRounds + 2;
        mRows = numberOfPlayers + 2;

        mPlayerList = game.getPlayerList().toArray(new Player[numberOfPlayers]);
        mSumList = new int[numberOfRounds];
        for (int i = 0; i < numberOfRounds; i++) {
            int sum = 0;
            for (Player p : mPlayerList) {
                List<Score> scoreList = p.getScoreList();
                sum += scoreList.get(Math.min(i, scoreList.size() - 1)).getScore();
            }
            mSumList[i] = sum;
        }
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

        //Empty first cell
        if (row == 0 && col == 0) {
            holder.updateData(null, null);
            return;
        }

        //Round number Header
        if (row == 0) {
            holder.updateData(col == mCols - 1
                    ? "Final" : "R" + StringUtils.padLeft(col, '0', 2), null);
            return;
        }

        //Player name Header
        if (col == 0) {
            holder.updateData(row == mRows - 1 ? "\u03A3"
                    : mPlayerList[row - 1].getName(), null);
            return;
        }

        //Empty last cell
        if (row == mRows - 1 && col == mCols - 1) {
            holder.updateData(null, null);
            return;
        }

        //Score sum Footer
        if (row == mRows - 1) {
            holder.updateData(addPlus(mSumList[col - 1]), null);
            return;
        }

        //Final score Footer OR the last few columns with no change in score
        if (col == mCols - 1) {
            holder.updateData(mPlayerList[row - 1].getScore(), null);
            return;
        }

        //Normal summary cell
        List<Score> scoreList = mPlayerList[row - 1].getScoreList();
        if (col > scoreList.size()) {
            holder.updateData("-", null);
            return;
        }
        Score score = scoreList.get(col - 1);
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
        return col == 0 || row == 0
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
