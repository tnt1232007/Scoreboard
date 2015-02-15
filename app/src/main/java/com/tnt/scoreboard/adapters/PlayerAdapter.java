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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.amulyakhare.textdrawable.TextDrawable;
import com.tnt.scoreboard.R;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.models.Player;
import com.tnt.scoreboard.models.Score;
import com.tnt.scoreboard.utils.PrefUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

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
        long max = mPlayerList.get(0).getScore();
        for (int i = 1; i < mPlayerList.size(); i++) {
            long score = mPlayerList.get(i).getScore();
            if (max < score)
                max = score;
        }
        long result = (long) Math.pow(10, Math.ceil(Math.log10(max)));
        if (max >= mEndingScore) {
            if (!mIsInfinite)
                mListener.onEnded();
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

        public void onEnded();
    }

    static class PlayerViewHolder extends RecyclerView.ViewHolder {

        private final TextDrawable.IBuilder mAvatarBuilder = TextDrawable.builder()
                .beginConfig().bold().endConfig().round();
        private final TextDrawable.IBuilder mScoreBuilder = TextDrawable.builder().round();
        private final ImageView mAvatar, mRankImage, mIncrementScore;
        private final Button mButton0, mButton1, mButton2, mButton3;
        private final Context mContext;
        private final TextView mRankText, mPlayerName, mTotalScore;
        private final ProgressBar mDelayProgress, mScoreProgress;
        private final RecyclerView mRecyclerView;
        private final ToggleButton mToggle;
        private final CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;
        private final int mGreen, mRed;
        private CountDownTimer mCountdown;
        private int m0, m1, m2, m3, mScore, mUpdateDelay = -1;
        private IOnScoreUpdateListener mListener;
        private long mPlayerId;

        public PlayerViewHolder(final View itemView) {
            super(itemView);
            Resources r = itemView.getResources();
            mGreen = r.getColor(R.color.green);
            mRed = r.getColor(R.color.red);
            mContext = itemView.getContext();
            mPlayerName = (TextView) itemView.findViewById(R.id.playerName);
            mRankText = (TextView) itemView.findViewById(R.id.currentRank);
            mTotalScore = (TextView) itemView.findViewById(R.id.totalScore);
            mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            mRankImage = (ImageView) itemView.findViewById(R.id.rank);
            mScoreProgress = ((ProgressBar) itemView.findViewById(R.id.scoreProgress));
            mIncrementScore = ((ImageView) itemView.findViewById(R.id.incrementScore));
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
            mDelayProgress = (ProgressBar) itemView.findViewById(R.id.delayProgress);
            mToggle = (ToggleButton) itemView.findViewById(R.id.toggle);
            mButton0 = (Button) itemView.findViewById(R.id.btn0);
            mButton1 = (Button) itemView.findViewById(R.id.btn1);
            mButton2 = (Button) itemView.findViewById(R.id.btn2);
            mButton3 = (Button) itemView.findViewById(R.id.btn3);

            mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
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
            mToggle.setOnCheckedChangeListener(mOnCheckedChangeListener);

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
                    finishCountdown();
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
            updateRank(rank);

            m0 = PrefUtils.getScore0(mContext);
            m1 = PrefUtils.getScore1(mContext);
            m2 = PrefUtils.getScore2(mContext);
            m3 = PrefUtils.getScore3(mContext);
            mOnCheckedChangeListener.onCheckedChanged(null, mToggle.isChecked());

            int newUpdateDelay = PrefUtils.getUpdateDelay(mContext) * 1000;
            if (mUpdateDelay != newUpdateDelay) {
                mUpdateDelay = newUpdateDelay;
                if (mCountdown != null)
                    mCountdown.cancel();
                mDelayProgress.setProgress(0);
                mDelayProgress.setMax(mUpdateDelay);
                mScore = 0;
                show(false);
                mCountdown = new CountDownTimer(mUpdateDelay, 10) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mDelayProgress.setProgress((int) (mUpdateDelay - millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        finishCountdown();
                    }
                };
            }

            String name = player.getName();
            String initial = String.valueOf(name.charAt(0));
            String rest = name.substring(1, name.length());
            int color = (int) player.getColor();
            long score = player.getScore();
            mAvatar.setBackground(mAvatarBuilder.build(initial, color));
            mPlayerName.setText(rest);
            mPlayerName.setTextColor(color);
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

            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(new ScoreAdapter(player.getScoreList()));
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

            mIncrementScore.setBackground(mScoreBuilder.build(
                    (mScore > 0 ? "+" : "") + mScore, mScore >= 0 ? mGreen : mRed));
            show(true);

            if (mUpdateDelay != 0) {
                mCountdown.cancel();
                mCountdown.start();
            } else {
                finishCountdown();
            }
        }

        private void finishCountdown() {
            mDelayProgress.setProgress(mUpdateDelay);
            if (mListener != null) {
                mListener.onAdded(new Score(mPlayerId, mScore));
                mScore = 0;
            }
            mDelayProgress.setProgress(0);
            show(false);
        }

        private void show(final boolean visible) {
            final float alpha = visible ? 1f : 0f;
            final int value = (visible ? -70 : 0);
            int firstDelay = visible ? 100 : 0;
            int secondDelay = visible ? 0 : 100;
            mIncrementScore.animate().setStartDelay(firstDelay).alpha(alpha)
                    .withStartAction(new Runnable() {
                        @Override
                        public void run() {
                            if (visible)
                                mIncrementScore.setVisibility(View.VISIBLE);
                        }
                    })
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            if (!visible)
                                mIncrementScore.setVisibility(View.INVISIBLE);
                        }
                    }).start();
            mTotalScore.animate().setStartDelay(secondDelay).x(value).start();
        }

        private void updateRank(int rank) {
            String s;
            mRankImage.setVisibility(View.INVISIBLE);
            switch (rank) {
                case 1:
                    s = "st";
                    mRankImage.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    s = "nd";
                    break;
                case 3:
                    s = "rd";
                    break;
                default:
                    s = "th";
                    break;
            }
            mRankText.setText(rank + s + " place");
        }

        public void setListener(IOnScoreUpdateListener listener) {
            this.mListener = listener;
        }

        public interface IOnScoreUpdateListener {
            public void onAdded(Score score);

            public void onDeleted();
        }
    }
}
