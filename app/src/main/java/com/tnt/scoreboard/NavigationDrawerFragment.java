package com.tnt.scoreboard;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tnt.scoreboard.adapters.NavigationAdapter;
import com.tnt.scoreboard.adapters.NavigationViewHolder;

public class NavigationDrawerFragment extends Fragment {

    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationAdapter mNavigationAdapter;

    public NavigationDrawerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mNavigationAdapter = new NavigationAdapter(view.getContext());

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(mNavigationAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
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

    public void setup(DrawerLayout drawerLayout, NavigationViewHolder.IOnNavigationClickListener listener) {
        mNavigationAdapter.setListener(listener);
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
    }

    //<editor-fold desc="Getter Setter">
    public void setCurrentPosition(int currentPosition) {
        mNavigationAdapter.setCurrentPosition(currentPosition);
    }

    public boolean isDrawerToggleSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item);
    }
    //</editor-fold>
}
