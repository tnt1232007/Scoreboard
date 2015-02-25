package com.tnt.scoreboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tnt.scoreboard.adapters.InfoAdapter;
import com.tnt.scoreboard.models.Game;

public class InfoDrawerFragment extends Fragment {

    private IOnDrawerToggle mListener;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_drawer, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    public void setup(final DrawerLayout drawerLayout) {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
                R.string.draw_open, R.string.draw_close) {
            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                return super.onOptionsItemSelected(item);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (mListener != null) {
                    mListener.onDrawerOpened();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mRecyclerView.scrollToPosition(0);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    public void update(Game game) {
        if (game == null)
            mRecyclerView.setAdapter(null);
        else
            mRecyclerView.setAdapter(new InfoAdapter(game));
    }

    public void setListener(IOnDrawerToggle listener) {
        mListener = listener;
    }

    public interface IOnDrawerToggle {
        public void onDrawerOpened();
    }
}
