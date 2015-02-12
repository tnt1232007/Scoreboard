package com.tnt.scoreboard.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.tnt.scoreboard.R;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.models.Player;
import com.tnt.scoreboard.utils.StringUtils;

import java.util.List;

public class GameViewHolder extends RecyclerView.ViewHolder {

    private final int mCheckColor;
    private final TextDrawable.IBuilder mDrawableBuilder = TextDrawable.builder().round();
    private final ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

    private ImageView mIcon;
    private ImageView mCheck;
    private TextView mPlayerName;
    private TextView mCurrentRound;
    private TextView mDateTime;
    private IOnGameClickListener mListener;
    private boolean isCheckClick;

    public GameViewHolder(View itemView) {
        super(itemView);
        mIcon = (ImageView) itemView.findViewById(R.id.icon);
        mCheck = (ImageView) itemView.findViewById(R.id.check);
        mPlayerName = (TextView) itemView.findViewById(R.id.playerName);
        mCurrentRound = (TextView) itemView.findViewById(R.id.currentRound);
        mDateTime = (TextView) itemView.findViewById(R.id.dateTime);
        mCheckColor = itemView.getContext().getResources().getColor(R.color.grayDark);

        mIcon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isCheckClick = true;
                return false;
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGameClick(v, isCheckClick);
                isCheckClick = false;
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
        mPlayerName.setText(StringUtils.join(game.getPlayers(), ", "));
        mCurrentRound.setText("Round " + game.getCurrentRoundNumber());
        mDateTime.setText(DateUtils.formatDateTime(
                mDateTime.getContext(), game.getCreateDate().getTime(),
                DateUtils.FORMAT_ABBREV_ALL
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_SHOW_TIME));
    }

    public void updateState(Game game, boolean selected) {
        if (selected) {
            mIcon.setImageDrawable(mDrawableBuilder.build(" ", mCheckColor));
            itemView.setSelected(true);
            mCheck.setVisibility(View.VISIBLE);
        } else {
            List<Player> players = game.getPlayers();
            Context context = itemView.getContext();
            String s1 = String.format("%s%s", players.size(),
                    StringUtils.getInitial(context, players.get(0).getName()));
            String s2 = String.format("%s%s", players.size(),
                    StringUtils.getInitial(context, players.get(1).getName()));
            mIcon.setImageDrawable(mDrawableBuilder.build(s1, mColorGenerator.getColor(s2)));
            itemView.setSelected(false);
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
