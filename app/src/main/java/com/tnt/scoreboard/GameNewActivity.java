package com.tnt.scoreboard;

import android.animation.ValueAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.models.Player;
import com.tnt.scoreboard.utils.ColorUtils;
import com.tnt.scoreboard.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;


public class GameNewActivity extends BaseActivity {

    public static final String PLAYER = "Player ";
    public static final String MAXIMUM_ALLOW = "Maximum %s players allowed";
    public static final String MINIMUM_ALLOW = "Must have minimum %s players";
    private static final String COLOR_INDEX = "colorIndex";
    private static final String CHAR_INDEX = "harIndex";

    private static final int MAX_PLAYER_NUM = 8;
    private static final int MIN_PLAYER_NUM = 2;
    private static final int ASCII_A = 65;
    private static final int CHAR_SIZE = 26;

    private List<PlayerNewFragment> fragmentList = new ArrayList<>();
    private ScrollView mScrollView;
    private Button mNewPlayerButton;
    private int mColorIndex, mCharIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_game_new);

        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mNewPlayerButton = (Button) findViewById(R.id.newPlayerButton);
        mNewPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewPlayerFragment(null, -1, true, true);
            }
        });

        if (savedInstanceState != null) {
            mColorIndex = savedInstanceState.getInt(COLOR_INDEX);
            mCharIndex = savedInstanceState.getInt(CHAR_INDEX);
            return;
        }

        Game game = getGame(getIntent().getLongExtra(Game.COLUMN_ID, -1));
        if (game == null) {
            addNewPlayerFragment(PrefUtils.getDefaultName(this), -1, false, false);
            addNewPlayerFragment(null, -1, false, false);
        } else {
            for (Player p : game.getPlayers()) {
                addNewPlayerFragment(p.getName(), (int) p.getColor(), true, false);
            }
        }
    }

    private void addNewPlayerFragment(String playerName, int color, boolean removable, boolean animate) {
        if (animate)
            move(true);
        else
            moveWithoutAnimation(true);

        if (playerName == null)
            playerName = PLAYER + Character.toChars(ASCII_A + mCharIndex)[0];

        if (color == -1)
            color = ColorUtils.ColorMap(this).getValue(mColorIndex);

        final PlayerNewFragment frag = PlayerNewFragment.newInstance(playerName, color, removable);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.linearLayout, frag, String.valueOf(mColorIndex)).commit();
        manager.executePendingTransactions();

        if (animate) {
            mScrollView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScrollView.fullScroll(View.FOCUS_DOWN);
                    frag.requestFocus();
                }
            }, 400);
        }

        mColorIndex++;
        mCharIndex++;
        mColorIndex = mColorIndex >= ColorUtils.ColorMap(this).size() ? 0 : mColorIndex;
        mCharIndex = mCharIndex >= CHAR_SIZE ? 0 : mCharIndex;

        if (fragmentList.size() >= MAX_PLAYER_NUM) {
            mNewPlayerButton.setVisibility(View.GONE);
            Toast.makeText(this, String.format(MAXIMUM_ALLOW, MAX_PLAYER_NUM), Toast.LENGTH_SHORT).show();
        }
    }

    private void moveWithoutAnimation(boolean isDown) {
        int density = (int) getResources().getDisplayMetrics().density;
        int delta = isDown ? 60 * density : -60 * density;

        final RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) mNewPlayerButton.getLayoutParams();
        params.topMargin += delta;
        mNewPlayerButton.setLayoutParams(params);
    }

    private void move(boolean isDown) {
        int density = (int) getResources().getDisplayMetrics().density;
        int delta = isDown ? 60 * density : -60 * density;

        final RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) mNewPlayerButton.getLayoutParams();
        ValueAnimator anim = ValueAnimator.ofInt(params.topMargin, params.topMargin + delta);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                params.topMargin = (int) (valueAnimator.getAnimatedValue());
                mNewPlayerButton.setLayoutParams(params);
            }
        });
        anim.setDuration(280);
        if (!isDown) anim.setStartDelay(280);
        anim.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(COLOR_INDEX, mColorIndex);
        outState.putInt(CHAR_INDEX, mCharIndex);
    }

    @Override
    public void onAttachFragment(Fragment frag) {
        if (frag instanceof PlayerNewFragment) {
            final PlayerNewFragment f = (PlayerNewFragment) frag;
            fragmentList.add(f);
            f.setOnRemoveListener(new PlayerNewFragment.IOnRemoveListener() {
                @Override
                public void onRemove() {
                    getFragmentManager().beginTransaction().remove(f).commit();
                    move(false);
                    fragmentList.remove(f);
                    if (fragmentList.size() < MAX_PLAYER_NUM) {
                        mNewPlayerButton.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return onCreateOptionsMenu(menu, R.menu.menu_game_new);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (fragmentList.size() < MIN_PLAYER_NUM) {
                    Toast.makeText(this, String.format(MINIMUM_ALLOW, MIN_PLAYER_NUM), Toast.LENGTH_SHORT).show();
                    return true;
                }

                List<Player> playerList = new ArrayList<>();
                for (PlayerNewFragment frag : fragmentList) {
                    Player p = new Player(frag.getPlayerName(), frag.getColor());
                    playerList.add(p);
                }
                Game game = addGame(playerList);

                Intent intent = new Intent(this, GameScoreActivity.class);
                intent.putExtra(Game.COLUMN_ID, game.getId());
                startActivity(intent);

                setResult(RESULT_OK, intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
