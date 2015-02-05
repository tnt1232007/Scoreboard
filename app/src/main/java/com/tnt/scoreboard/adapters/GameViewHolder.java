package com.tnt.scoreboard.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.tnt.scoreboard.R;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.utils.ColorUtils;
import com.tnt.scoreboard.utils.PrefUtils;
import com.tnt.scoreboard.utils.StringUtils;

public class GameViewHolder extends RecyclerView.ViewHolder {

    private final int mHighlightColor;
    private final int mCheckColor;
    private final boolean mIsFirstNameLast;

    private ImageView mIcon;
    private ImageView mCheck;
    private ImageView mBackground;
    private TextView mPlayerName;
    private TextView mCurrentRound;
    private TextView mDateTime;

    private IOnGameClickListener mListener;
    private TextDrawable.IBuilder mDrawableBuilder = TextDrawable.builder().round();
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

    public GameViewHolder(View itemView) {
        super(itemView);
        mIcon = (ImageView) itemView.findViewById(R.id.icon);
        mCheck = (ImageView) itemView.findViewById(R.id.check);
        mBackground = (ImageView) itemView.findViewById(R.id.background);
        mPlayerName = (TextView) itemView.findViewById(R.id.playerName);
        mCurrentRound = (TextView) itemView.findViewById(R.id.currentRound);
        mDateTime = (TextView) itemView.findViewById(R.id.dateTime);
        mIsFirstNameLast = PrefUtils.isFirstNameLast(itemView.getContext());
        mHighlightColor = ColorUtils.GetAttrColor(itemView.getContext(),
                android.R.attr.colorControlHighlight);
        mCheckColor = itemView.getContext().getResources().getColor(R.color.grayDark);

        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGameClick(v, true);
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGameClick(v, false);
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.onGameClick(v, true);
                return true;
            }
        });
    }

    public void updateData(Game game) {
        if (game == null) return;
        mPlayerName.setText(StringUtils.join(game.getPlayers(), ", ", 40));
        mCurrentRound.setText("Round " + game.getCurrentRoundNumber());
        mDateTime.setText(DateUtils.formatDateTime(
                mDateTime.getContext(), game.getCreateDate().getTime(),
                DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME));
    }

    public void updateState(Game game, boolean selected) {
        if (selected) {
            mIcon.setImageDrawable(mDrawableBuilder.build(" ", mCheckColor));
            mBackground.setBackgroundColor(mHighlightColor);
            mCheck.setVisibility(View.VISIBLE);
        } else {
            String[] nameArray = game.getPlayers().get(0).getName().split(" ");
            String firstName = nameArray[mIsFirstNameLast ? nameArray.length - 1 : 0];
            mIcon.setImageDrawable(mDrawableBuilder.build(
                    String.valueOf(firstName.charAt(0)),
                    mColorGenerator.getColor(firstName.charAt(0))));
            mBackground.setBackgroundColor(Color.TRANSPARENT);
            mCheck.setVisibility(View.GONE);
        }
    }

    //<editor-fold desc="Getter Setter">
    public void setListener(IOnGameClickListener listener) {
        this.mListener = listener;
    }
    //</editor-fold>

    public interface IOnGameClickListener {

        public void onGameClick(View v, boolean isCheckClick);
    }
}
