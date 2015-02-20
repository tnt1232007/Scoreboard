package com.tnt.scoreboard;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.plus.model.people.Person;
import com.tnt.scoreboard.adapters.NavigationAdapter;
import com.tnt.scoreboard.utils.DrawableUtils;
import com.tnt.scoreboard.utils.InternetUtils;

public class NavigationDrawerFragment extends Fragment {

    private OnDrawerToggle mCallback;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationAdapter mNavigationAdapter;
    private ImageView mAvatar, mCover;
    private TextView mName, mEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mAvatar = (ImageView) view.findViewById(R.id.avatar);
        mCover = (ImageView) view.findViewById(R.id.cover);
        mName = (TextView) view.findViewById(R.id.name);
        mEmail = (TextView) view.findViewById(R.id.email);

        mNavigationAdapter = new NavigationAdapter(view.getContext());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(mNavigationAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnDrawerToggle) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDrawerToggleListener");
        }
    }

    public void setup(final DrawerLayout drawerLayout,
                      NavigationAdapter.IOnNavigationClickListener listener) {
        mNavigationAdapter.setListener(listener);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
                R.string.draw_open, R.string.draw_close) {
            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                boolean selected = super.onOptionsItemSelected(item);
                if (selected) {
                    Bitmap bitmap = DrawableUtils.takeScreenShot(
                            getActivity().getWindow().getDecorView().getRootView());
                    mCallback.onDrawerOpened(bitmap);
                }
                return selected;
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                if (newState == DrawerLayout.STATE_DRAGGING
                        && !drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    Bitmap bitmap = DrawableUtils.takeScreenShot(
                            getActivity().getWindow().getDecorView().getRootView());
                    mCallback.onDrawerOpened(bitmap);
                }
            }

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

    public void setupAdditionalInfo(Person person, String email) {
        if (person != null && email != null) {
            mName.setText(person.getDisplayName());
            mEmail.setText(email);
            new InternetUtils.DownloadImage(mAvatar, true).execute(
                    person.getImage().getUrl().replace("sz=50", "sz=250"));
            new InternetUtils.DownloadImage(mCover, false).execute(
                    person.getCover().getCoverPhoto().getUrl());
        }
    }

    public void setCurrentPosition(int currentPosition) {
        mNavigationAdapter.setCurrentPosition(currentPosition);
    }

    public boolean isDrawerToggleSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item);
    }

    public interface OnDrawerToggle {
        public void onDrawerOpened(Parcelable parcelable);
    }
}
