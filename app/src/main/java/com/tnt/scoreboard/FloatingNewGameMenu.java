package com.tnt.scoreboard;

import android.content.Context;
import android.content.Intent;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.models.Player;
import com.tnt.scoreboard.utils.Constants;
import com.tnt.scoreboard.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FloatingNewGameMenu extends FloatingActionsMenu {

    public static final String RECENT_GAMES = "Recent games will show up here";
    private final TextDrawable.IBuilder mDrawableBuilder =
            TextDrawable.builder().beginConfig().fontSize(40).bold().endConfig().round();
    private final ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private final List<FloatingActionButton> buttonList = new ArrayList<>();

    private BaseActivity mActivity;
    private ImageView mDimBackground;
    private View mFabBlankLayout;

    public FloatingNewGameMenu(Context context) {
        super(context);
    }

    public FloatingNewGameMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatingNewGameMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void expand() {
        super.expand();
        if (mDimBackground == null) return;

        mDimBackground.animate().alpha(0.9f).withStartAction(new Runnable() {
            @Override
            public void run() {
                mDimBackground.setVisibility(VISIBLE);
                mFabBlankLayout.setVisibility(VISIBLE);
            }
        });
    }

    @Override
    public void collapse() {
        super.collapse();
        if (mDimBackground == null) return;

        mDimBackground.animate().alpha(0f).withEndAction(new Runnable() {
            @Override
            public void run() {
                mDimBackground.setVisibility(INVISIBLE);
                mFabBlankLayout.setVisibility(INVISIBLE);
            }
        });
    }

    public void setup(final BaseActivity activity, List<Game> gameList) {
        mActivity = activity;
        mDimBackground = ((ImageView) mActivity.findViewById(R.id.dimBackground));
        mFabBlankLayout = mActivity.findViewById(R.id.fabBlankLayout);

        mDimBackground.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                collapse();
            }
        });
        mActivity.findViewById(R.id.fabBlank).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, GameNewActivity.class);
                mActivity.startActivityForResult(intent, Constants.GAME_NEW_REQUEST);
                collapse();
            }
        });

        for (FloatingActionButton button : buttonList) {
            removeButton(button);
        }
        buttonList.clear();
        for (Game g : gameList) {
            FloatingActionButton button = createRecentGameButton(g);
            addButton(button);
            buttonList.add(button);
        }
        if (gameList.isEmpty()) {
            FloatingActionButton button = createInfoButton();
            addButton(button);
            buttonList.add(button);
        }
    }

    private FloatingActionButton createInfoButton() {
        FloatingActionButton button = new FloatingActionButton(mActivity);
        button.setTitle(RECENT_GAMES);
        button.setSize(FloatingActionButton.SIZE_MINI);
        button.setIcon(R.drawable.ic_etc);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                collapse();
            }
        });
        return button;
    }

    private FloatingActionButton createRecentGameButton(final Game game) {
        List<Player> players = game.getPlayers();
        Context context = this.getContext();
        String s1 = String.format("%s%s", players.size(),
                StringUtils.getInitial(context, players.get(0).getName()));
        String s2 = String.format("%s%s", players.size(),
                StringUtils.getInitial(context, players.get(1).getName()));
        int color = mColorGenerator.getColor(s2);

        FloatingActionButton button = new FloatingActionButton(mActivity);
        button.setTitle(StringUtils.join(players, ", ", 28));
        button.setSize(FloatingActionButton.SIZE_MINI);
        button.setColorNormal(color);
        button.setColorPressed(color);
        button.setIconDrawable(mDrawableBuilder.build(s1, color));
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, GameNewActivity.class);
                intent.putExtra(Game.COLUMN_ID, game.getId());
                mActivity.startActivityForResult(intent, Constants.GAME_NEW_REQUEST);
                collapse();
            }
        });
        return button;
    }

    public void show(boolean visible, boolean animated) {
        if (getVisibility() == GONE) return;
        if (animated) TransitionManager.beginDelayedTransition(
                (ViewGroup) mActivity.findViewById(R.id.layout), new Slide());
        setVisibility(visible ? VISIBLE : INVISIBLE);
    }

    public void move(int value) {
        if (getVisibility() != VISIBLE) return;
        Interpolator interpolator = AnimationUtils.loadInterpolator(getContext(), value > 0
                ? R.interpolator.sb__accelerate_cubic : R.interpolator.sb__decelerate_cubic);
        animate().yBy(value).setInterpolator(interpolator).start();
        mFabBlankLayout.animate().setDuration(300).setInterpolator(interpolator).yBy(value).start();
    }
}
