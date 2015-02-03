package com.tnt.scoreboard;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tnt.scoreboard.adapters.GameAdapter;
import com.tnt.scoreboard.adapters.NavigationAdapter;
import com.tnt.scoreboard.adapters.NavigationViewHolder;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.utils.ActivityUtils;

import java.util.List;


public class NavigationDrawerFragment extends Fragment {

    private int mCurrentPosition;
    private ActionBarDrawerToggle mDrawerToggle;
    private GameListActivity mActivity;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private MyUndoBar mUndoBar;
    private MyNewGameButton mFab;
    private NavigationAdapter mNavigationAdapter;

    public NavigationDrawerFragment() {
    }

    public void setup(GameListActivity activity, DrawerLayout drawerLayout) {
        mActivity = activity;
        mDrawerLayout = drawerLayout;
        mToolbar = activity.mToolbar;
        mUndoBar = activity.getUndoBar();
        mFab = activity.getFab();
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
                R.string.draw_open, R.string.draw_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(mDrawerToggle);
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        ActionBar actionBar = mActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        final Context context = view.getContext();

        mNavigationAdapter = new NavigationAdapter(context);
        mNavigationAdapter.setListener(new NavigationViewHolder.IOnNavigationClickListener() {
            @Override
            public void onNavigationClick(View v, int navigationOption) {
                mDrawerLayout.closeDrawers();
                if (mCurrentPosition == navigationOption) return;
                mUndoBar.clear();
                mFab.collapse();
                if (mActivity.mActionMode != null) mActivity.mActionMode.finish();
                navigate(navigationOption);
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(mNavigationAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //<editor-fold desc="TODO: Uses Google API to get personal info">
        ImageView avatar = (ImageView) view.findViewById(R.id.avatar);
        avatar.setImageResource(R.drawable.avatar);

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText("TNT");

        TextView email = (TextView) view.findViewById(R.id.email);
        email.setText("tnt1232007@gmail.com");

        RelativeLayout header = (RelativeLayout) view.findViewById(R.id.header);
        header.setBackgroundResource(R.drawable.cover);
        //</editor-fold>
        return view;
    }

    public void navigate(int naviOption) {
        switch (naviOption) {
            case ActivityUtils.GAMES:
            case ActivityUtils.ARCHIVE:
            case ActivityUtils.TRASH:
                final ActivityUtils.Screen screen = ActivityUtils.SCREENS[naviOption];

                Resources r = getResources();
                mFab.setVisibility(screen.FAB_VISIBLE);
                mToolbar.setTitle(r.getString(screen.TITLE));
                mToolbar.setBackground(new ColorDrawable(r.getColor(screen.COLOR_PRIMARY)));
                mActivity.getWindow().setStatusBarColor(r.getColor(screen.COLOR_PRIMARY_DARK));
                setCurrentPosition(naviOption);

                List<Game> gameList = mActivity.getGameList(screen.STATE);
                final GameAdapter gameAdapter = new GameAdapter(gameList);
                gameAdapter.setListener(new GameAdapter.IOnSelectListener() {
                    @Override
                    public void onSelect() {
                        int count = gameAdapter.getSelectedCount();
                        if (count > 0 && mActivity.mActionMode == null) {
                            mActivity.mActionMode = mActivity.startSupportActionMode(
                                    new MyActionMode(mActivity, screen));
                        } else if (count == 0 && mActivity.mActionMode != null) {
                            mActivity.mActionMode.finish();
                        }
                        if (mActivity.mActionMode != null) {
                            mActivity.mActionMode.setTitle(String.valueOf(count));
                        }
                    }
                });
                mActivity.setGameAdapter(gameAdapter);
                mActivity.showEmptyView(gameList.isEmpty(), ActivityUtils.SCREENS[naviOption]);
                break;
            case ActivityUtils.SETTINGS:
                Intent intent = new Intent(mActivity, SettingActivity.class);
                startActivity(intent);
                break;
            case ActivityUtils.HELP:
                break;
        }
    }

    //<editor-fold desc="Getter Setter">
    public void setCurrentPosition(int currentPosition) {
        mCurrentPosition = currentPosition;
        mNavigationAdapter.setCurrentPosition(currentPosition);
    }

    public boolean isDrawerToggleSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item);
    }
    //</editor-fold>
}
