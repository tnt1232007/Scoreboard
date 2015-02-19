package com.tnt.scoreboard;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.models.Player;
import com.tnt.scoreboard.utils.ColorUtils;
import com.tnt.scoreboard.utils.Constants;
import com.tnt.scoreboard.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

public class GameNewActivity extends BaseActivity {

    private static final String PLAYER = "Player ";
    private static final String MAXIMUM_ALLOW = "Maximum %s participants allowed";
    private static final String MINIMUM_ALLOW = "Must have at least %s participants";
    private static final String START_END_CONFLICT = "Ending score must bigger than Starting score";
    private static final String COLOR_INDEX = "colorIndex";
    private static final String CHAR_INDEX = "harIndex";
    private static final String NEW_PLAYER_TOP_MARGIN = "newPlayerTopMargin";

    private static final int ASCII_A = 65;
    private static final int CHAR_SIZE = 26;

    private List<PlayerNewFragment> fragmentList = new ArrayList<>();
    private ScrollView mScrollView;
    private Button mNewPlayerButton;
    private TextView mTitle, mStartScore, mEndScore;
    private int mColorIndex, mCharIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_game_new);
        mTitle = ((TextView) findViewById(R.id.title));
        mStartScore = ((TextView) findViewById(R.id.start));
        mEndScore = ((TextView) findViewById(R.id.end));
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
            RelativeLayout.LayoutParams params =
                    (RelativeLayout.LayoutParams) mNewPlayerButton.getLayoutParams();
            params.topMargin = savedInstanceState.getInt(NEW_PLAYER_TOP_MARGIN);
            mNewPlayerButton.setLayoutParams(params);
            return;
        }

        Game game = getGame(getIntent().getLongExtra(Game.COLUMN_ID, -1));
        if (game == null) {
            addNewPlayerFragment(PrefUtils.getName(this), -1, false, false);
            addNewPlayerFragment(null, -1, false, false);
        } else {
            for (Player p : game.getPlayerList()) {
                addNewPlayerFragment(p.getName(), (int) p.getColor(), true, false);
            }
            mTitle.setText(game.getTitle());
            mStartScore.setText(String.valueOf(game.getStartingScore()));
            mEndScore.setText(String.valueOf(game.getEndingScore()));
        }
        mToolbar.setNavigationIcon(R.drawable.ic_close_dark);
    }

    private void addNewPlayerFragment(String playerName, int color, boolean removable, boolean animate) {
        if (animate) move(true);
        else moveWithoutAnimation(true);

        if (playerName == null) playerName = PLAYER + Character.toChars(ASCII_A + mCharIndex)[0];
        if (color == -1) color = ColorUtils.ColorMap(this).getValue(mColorIndex);

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

        if (fragmentList.size() >= Constants.MAX_PLAYERS) {
            mNewPlayerButton.setVisibility(View.GONE);
            Toast.makeText(this, String.format(MAXIMUM_ALLOW, Constants.MAX_PLAYERS),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void moveWithoutAnimation(boolean isDown) {
        int density = (int) getResources().getDisplayMetrics().density;
        int delta = isDown ? 61 * density : -61 * density;

        final RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) mNewPlayerButton.getLayoutParams();
        params.topMargin += delta;
        mNewPlayerButton.setLayoutParams(params);
    }

    private void move(boolean isDown) {
        int density = (int) getResources().getDisplayMetrics().density;
        int delta = isDown ? 61 * density : -61 * density;

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
        outState.putInt(NEW_PLAYER_TOP_MARGIN, ((RelativeLayout.LayoutParams)
                mNewPlayerButton.getLayoutParams()).topMargin);
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
                    if (fragmentList.size() < Constants.MAX_PLAYERS) {
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
            case android.R.id.home:
                new AlertDialog.Builder(this)
                        .setMessage("Dismiss and go home?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
                return true;
            case R.id.action_start:
                String title = getValue(mTitle, String.class);
                Long start = getValue(mStartScore, Long.class);
                Long end = getValue(mEndScore, Long.class);

                if (fragmentList.size() < Constants.MIN_PLAYERS) {
                    Toast.makeText(this, String.format(MINIMUM_ALLOW, Constants.MIN_PLAYERS),
                            Toast.LENGTH_SHORT).show();
                    return true;
                } else if (start > end) {
                    Toast.makeText(this, START_END_CONFLICT, Toast.LENGTH_SHORT).show();
                    return true;
                }

                List<Player> playerList = new ArrayList<>();
                for (PlayerNewFragment frag : fragmentList) {
                    Player p = new Player(frag.getPlayerName(), frag.getColor(), start);
                    playerList.add(p);
                }

                Game game = addGame(title, playerList, start, end);
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

    private <T> T getValue(TextView textView, Class<T> aClass) {
        CharSequence text = textView.getText();
        text = text.length() == 0 ? textView.getHint() : text;
        if (aClass == String.class)
            return aClass.cast(String.valueOf(text));
        if (aClass == Integer.class)
            return aClass.cast(Integer.parseInt(String.valueOf(text)));
        if (aClass == Long.class)
            return aClass.cast(Long.parseLong(String.valueOf(text)));
        return null;
    }
}
