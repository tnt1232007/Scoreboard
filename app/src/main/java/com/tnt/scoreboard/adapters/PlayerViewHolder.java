package com.tnt.scoreboard.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
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

public class PlayerViewHolder extends RecyclerView.ViewHolder {

    private final TextDrawable.IBuilder mDrawableBuilder = TextDrawable.builder()
            .beginConfig().bold().endConfig().round();
    private TextView mCurrentRank;
    private ProgressBar mProgressBar;
    private ImageView mAvatar;
    private TextView mPlayerName;
    private TextView mTotalScore;
    private ImageView mIncrementScore;
    private Button mButton0;
    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private int mScore;
    private int m0;
    private int m1;
    private int m2;
    private int m3;

    public PlayerViewHolder(final View itemView) {
        super(itemView);
        mPlayerName = (TextView) itemView.findViewById(R.id.playerName);
        mCurrentRank = (TextView) itemView.findViewById(R.id.currentRank);
        mTotalScore = (TextView) itemView.findViewById(R.id.totalScore);
        mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
        mProgressBar = ((ProgressBar) itemView.findViewById(R.id.progressScore));
        mIncrementScore = ((ImageView) itemView.findViewById(R.id.incrementScore));
        mButton0 = (Button) itemView.findViewById(R.id.btn0);
        mButton1 = (Button) itemView.findViewById(R.id.btn1);
        mButton2 = (Button) itemView.findViewById(R.id.btn2);
        mButton3 = (Button) itemView.findViewById(R.id.btn3);

        //TODO: get from preference
        m0 = 0;
        m1 = 1;
        m2 = 2;
        m3 = 3;

        final ToggleButton toggle = (ToggleButton) itemView.findViewById(R.id.toggle);
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
        toggle.setOnCheckedChangeListener(onCheckedChangeListener);
        onCheckedChangeListener.onCheckedChanged(null, false);

        final Resources r = itemView.getContext().getResources();
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int negative = toggle.isChecked() ? -1 : 1;
                if (v == mButton0)
                    mScore += m0 * negative;
                else if (v == mButton1)
                    mScore += m1 * negative;
                else if (v == mButton2)
                    mScore += m2 * negative;
                else if (v == mButton3)
                    mScore += m3 * negative;

                String sign = mScore > 0 ? "+" : "";
                int color = mScore >= 0 ? r.getColor(R.color.green) : r.getColor(R.color.red);
                mIncrementScore.setBackground(mDrawableBuilder.build(sign + mScore, color));
                show(true);
            }
        };
        mButton0.setOnClickListener(onClickListener);
        mButton1.setOnClickListener(onClickListener);
        mButton2.setOnClickListener(onClickListener);
        mButton3.setOnClickListener(onClickListener);
        itemView.findViewById(R.id.btnC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScore = 0;
                show(false);
                toggle.setChecked(false);
            }
        });
    }

    public void updateData(Player player, int rank, int percent) {
        if (player == null) return;
        Context context = itemView.getContext();
        String name = player.getName();
        String initial = String.valueOf(name.charAt(0));
        String rest = name.substring(1, name.length());
        int color = (int) player.getColor();
        long score = player.getScore();

        mAvatar.setBackground(mDrawableBuilder.build(initial, color));
        mPlayerName.setText(rest);
        mPlayerName.setTextColor(color);
        mCurrentRank.setText(rank + getPosText(rank) + " place");
        mTotalScore.setText(String.valueOf(score));
        mTotalScore.setSelected(score < 0);
        mProgressBar.setProgress(Math.abs(percent));
        if (percent >= 0) {
            mProgressBar.setRotation(180);
            mProgressBar.getProgressDrawable().setColorFilter(
                    context.getResources().getColor(R.color.green), PorterDuff.Mode.SRC_IN);
        } else {
            mProgressBar.setRotation(0);
            mProgressBar.getProgressDrawable().setColorFilter(
                    context.getResources().getColor(R.color.red), PorterDuff.Mode.SRC_IN);
        }
    }

    public void show(boolean visible) {
        final float alpha = visible ? 1f : 0f;
        final int value = (visible ? 795 : 860) - mTotalScore.getWidth();
        int firstDelay = visible ? 100 : 0;
        int secondDelay = visible ? 0 : 100;
        mIncrementScore.animate().setStartDelay(firstDelay).alpha(alpha).start();
        mTotalScore.animate().setStartDelay(secondDelay).x(value).start();
    }

    private String getPosText(int rank) {
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
        return msg;
    }
}
