package com.tnt.scoreboard;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.models.Player;
import com.tnt.scoreboard.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FloatingNewGameMenu extends FloatingActionsMenu {

    public static final int NEW_GAME_REQUEST = 1;
    public static final String RECENT_GAMES = "Recent games will show up here";
    private final TextDrawable.IBuilder mDrawableBuilder =
            TextDrawable.builder().beginConfig().fontSize(40).bold().endConfig().round();
    private final ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private final List<FloatingActionButton> buttonList = new ArrayList<>();

    private BaseActivity mActivity;
    private ImageView mDimBackground;
    private View mFabBlankLayout;
    private int mDensity;

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
        mDensity = (int) getResources().getDisplayMetrics().density;
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
                mActivity.startActivityForResult(intent, NEW_GAME_REQUEST);
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
                mActivity.startActivityForResult(intent, NEW_GAME_REQUEST);
            }
        });
        return button;
    }

    public void show(boolean visible) {
        if (getVisibility() == GONE) return;
        TransitionManager.beginDelayedTransition(
                (ViewGroup) mActivity.findViewById(R.id.layout), new Slide());
        setVisibility(visible ? VISIBLE : INVISIBLE);
    }

    public void move(boolean up) {
        if (getVisibility() == GONE || isUp() == up) return;
        int delta = up ? 40 * mDensity : -40 * mDensity;

        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        final RelativeLayout.LayoutParams params2 =
                (RelativeLayout.LayoutParams) mFabBlankLayout.getLayoutParams();
        ValueAnimator anim = ValueAnimator.ofInt(params.bottomMargin, params.bottomMargin + delta);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                params.bottomMargin = params2.bottomMargin
                        = (int) (valueAnimator.getAnimatedValue());
                setLayoutParams(params);
                mFabBlankLayout.setLayoutParams(params2);
            }
        });
        anim.setDuration(150);
        anim.start();
        setUp(up);
    }

    private boolean isUp() {
        return getTag() == true;
    }

    private void setUp(boolean up) {
        setTag(up);
    }
}
