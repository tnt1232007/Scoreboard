package com.tnt.scoreboard.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.CountDownTimer;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.amulyakhare.textdrawable.TextDrawable;
import com.tnt.scoreboard.R;
import com.tnt.scoreboard.models.Player;
import com.tnt.scoreboard.models.Score;
import com.tnt.scoreboard.utils.PrefUtils;

import java.util.List;

public class PlayerViewHolder extends RecyclerView.ViewHolder {

    private final ImageView mAvatar, mIncrementScore;
    private final Button mButton0, mButton1, mButton2, mButton3;
    private final Context mContext;
    private final CountDownTimer mCountdown;
    private final TextView mCurrentRank, mPlayerName, mTotalScore;
    private final ProgressBar mDelayProgress, mScoreProgress;
    private final TextDrawable.IBuilder mDrawableBuilder = TextDrawable.builder()
            .beginConfig().bold().endConfig().round();
    private final RecyclerView mRecyclerView;
    private final ToggleButton mToggle;
    private int m0, m1, m2, m3, mGreen, mRed, mScore;
    private IOnScoreUpdateListener mListener;
    private long mPlayerId;

    public PlayerViewHolder(final View itemView) {
        super(itemView);
        Resources r = itemView.getResources();
        mGreen = r.getColor(R.color.green);
        mRed = r.getColor(R.color.red);
        mContext = itemView.getContext();
        mPlayerName = (TextView) itemView.findViewById(R.id.playerName);
        mCurrentRank = (TextView) itemView.findViewById(R.id.currentRank);
        mTotalScore = (TextView) itemView.findViewById(R.id.totalScore);
        mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
        mScoreProgress = ((ProgressBar) itemView.findViewById(R.id.scoreProgress));
        mIncrementScore = ((ImageView) itemView.findViewById(R.id.incrementScore));
        mRecyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
        mDelayProgress = (ProgressBar) itemView.findViewById(R.id.delayProgress);
        mToggle = (ToggleButton) itemView.findViewById(R.id.toggle);
        mButton0 = (Button) itemView.findViewById(R.id.btn0);
        mButton1 = (Button) itemView.findViewById(R.id.btn1);
        mButton2 = (Button) itemView.findViewById(R.id.btn2);
        mButton3 = (Button) itemView.findViewById(R.id.btn3);
        m0 = PrefUtils.getScore0(mContext);
        m1 = PrefUtils.getScore1(mContext);
        m2 = PrefUtils.getScore2(mContext);
        m3 = PrefUtils.getScore3(mContext);

        final int updateDelay = PrefUtils.getUpdateDelay(mContext) * 1000;
        mDelayProgress.setMax(updateDelay);
        mCountdown = new CountDownTimer(updateDelay, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                mDelayProgress.setProgress((int) (updateDelay - millisUntilFinished));
            }

            @Override
            public void onFinish() {
                finishCountdown(updateDelay);
            }
        };

        CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        itemView.findViewById(R.id.footerLayout).setSelected(isChecked);
                        String sign = isChecked ? "-" : "+";
                        mButton0.setText((m0 == 0 ? "" : sign) + m0);
                        mButton1.setText((m1 == 0 ? "" : sign) + m1);
                        mButton2.setText((m2 == 0 ? "" : sign) + m2);
                        mButton3.setText((m3 == 0 ? "" : sign) + m3);
                    }
                };
        mToggle.setOnCheckedChangeListener(onCheckedChangeListener);
        onCheckedChangeListener.onCheckedChanged(null, false);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCountdown(v, false);
            }
        };
        View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startCountdown(v, true);
                return true;
            }
        };
        mButton0.setOnClickListener(onClickListener);
        mButton1.setOnClickListener(onClickListener);
        mButton2.setOnClickListener(onClickListener);
        mButton3.setOnClickListener(onClickListener);
        mButton0.setOnLongClickListener(onLongClickListener);
        mButton1.setOnLongClickListener(onLongClickListener);
        mButton2.setOnLongClickListener(onLongClickListener);
        mButton3.setOnLongClickListener(onLongClickListener);
        mIncrementScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountdown.cancel();
                finishCountdown(updateDelay);
            }
        });
        itemView.findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScore = 0;
                mToggle.setChecked(false);
                mCountdown.cancel();
                mDelayProgress.setProgress(0);
                show(false);
            }
        });
        itemView.findViewById(R.id.btnUndo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    new AlertDialog.Builder(mContext)
                            .setMessage("Undo the last score?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mListener.onDeleted();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }
        });
    }

    public void updateData(Player player, int rank, int percent) {
        if (player == null) return;
        mPlayerId = player.getId();

        String name = player.getName();
        String initial = String.valueOf(name.charAt(0));
        String rest = name.substring(1, name.length());
        int color = (int) player.getColor();
        long score = player.getScore();
        mAvatar.setBackground(mDrawableBuilder.build(initial, color));
        mPlayerName.setText(rest);
        mPlayerName.setTextColor(color);
        mCurrentRank.setText(getRankText(rank));
        mTotalScore.setText(String.valueOf(score));
        mTotalScore.setSelected(score < 0);
        mScoreProgress.setProgress(Math.abs(percent));
        if (percent >= 0) {
            mScoreProgress.setRotation(180);
            mScoreProgress.getProgressDrawable().setColorFilter(mGreen, PorterDuff.Mode.SRC_IN);
        } else {
            mScoreProgress.setRotation(0);
            mScoreProgress.getProgressDrawable().setColorFilter(mRed, PorterDuff.Mode.SRC_IN);
        }

        List<Score> scoreList = player.getScoreList();
        int end = scoreList.size();
        int start = end < 5 ? 0 : end - 5;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(new ScoreAdapter(scoreList.subList(start, end)));
    }

    private void startCountdown(View button, boolean isLongClick) {
        int negative = mToggle.isChecked() ? -1 : 1;
        negative *= isLongClick ? -1 : 1;
        if (button == mButton0)
            mScore += m0 * negative;
        else if (button == mButton1)
            mScore += m1 * negative;
        else if (button == mButton2)
            mScore += m2 * negative;
        else if (button == mButton3)
            mScore += m3 * negative;

        mIncrementScore.setBackground(mDrawableBuilder.build(
                (mScore > 0 ? "+" : "") + mScore, mScore >= 0 ? mGreen : mRed));
        mCountdown.cancel();
        mCountdown.start();
        show(true);
    }

    private void finishCountdown(int updateDelay) {
        mDelayProgress.setProgress(updateDelay);
        if (mListener != null) {
            mListener.onAdded(new Score(mPlayerId, mScore));
            mScore = 0;
        }
        mDelayProgress.setProgress(0);
        show(false);
    }

    private void show(boolean visible) {
        final float alpha = visible ? 1f : 0f;
        final int value = (visible ? -70 : 0);
        int firstDelay = visible ? 100 : 0;
        int secondDelay = visible ? 0 : 100;
        mIncrementScore.animate().setStartDelay(firstDelay).alpha(alpha).start();
        mTotalScore.animate().setStartDelay(secondDelay).x(value).start();
    }

    private String getRankText(int rank) {
        String msg;
        switch (rank) {
            case 1:
                msg = "st";
                break;
            case 2:
                msg = "nd";
                break;
            case 3:
                msg = "rd";
                break;
            default:
                msg = "th";
                break;
        }
        return rank + msg + " place";
    }

    public void setListener(IOnScoreUpdateListener listener) {
        this.mListener = listener;
    }

    public interface IOnScoreUpdateListener {
        public void onAdded(Score score);

        public void onDeleted();
    }
}
