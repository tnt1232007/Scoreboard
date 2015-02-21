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

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.model.people.Person;
import com.tnt.scoreboard.adapters.NavigationAdapter;
import com.tnt.scoreboard.utils.DrawableUtils;
import com.tnt.scoreboard.utils.InternetUtils;

public class NavigationDrawerFragment extends Fragment {

    private IOnDrawerToggle mCallback;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationAdapter mNavigationAdapter;
    private ImageView mAvatar, mCover;
    private TextView mName, mEmail;
    private SignInButton mSignIn;
    private IOnGoogleApiListener mGoogleApiListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mAvatar = (ImageView) view.findViewById(R.id.avatar);
        mCover = (ImageView) view.findViewById(R.id.cover);
        mName = (TextView) view.findViewById(R.id.name);
        mEmail = (TextView) view.findViewById(R.id.email);
        mSignIn = (SignInButton) view.findViewById(R.id.signIn);

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
            mCallback = (IOnDrawerToggle) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDrawerToggleListener");
        }
    }

    public void setup(final DrawerLayout drawerLayout,
                      NavigationAdapter.IOnNavigationClickListener navigationClickListener,
                      final IOnGoogleApiListener googleApiListener) {
        mGoogleApiListener = googleApiListener;
        mNavigationAdapter.setListener(navigationClickListener);
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
        switchSignInButton(false);
    }

    public void setupAdditionalInfo(Person person, String email) {
        if (person != null) {
            mName.setText(person.getDisplayName());
            new InternetUtils.DownloadImage(mAvatar, true).execute(
                    person.getImage().getUrl().replace("sz=50", "sz=250"));
            new InternetUtils.DownloadImage(mCover, false).execute(
                    person.getCover().getCoverPhoto().getUrl());
        }
        if (email != null) {
            mEmail.setText(email);
        }
        switchSignInButton(true);
    }

    private void switchSignInButton(final boolean isSignIn) {
        String text = isSignIn ? "Sign out of Google" : "Sign in with Google";
        for (int i = 0; i < mSignIn.getChildCount(); i++) {
            View v = mSignIn.getChildAt(i);
            if (v instanceof TextView) {
                TextView textView = (TextView) v;
                textView.setText(text);
                textView.setAllCaps(false);
                break;
            }
        }
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGoogleApiListener == null) return;
                if (isSignIn) {
                    mGoogleApiListener.onSignOutClicked();
                    mName.setText("");
                    mEmail.setText("");
                    mAvatar.setImageResource(R.drawable.ic_avatar);
                    mCover.setImageResource(R.drawable.ic_cover);
                    switchSignInButton(false);
                } else {
                    mGoogleApiListener.onSignInClicked();
                }
            }
        });
    }

    public void setCurrentPosition(int currentPosition) {
        mNavigationAdapter.setCurrentPosition(currentPosition);
    }

    public boolean isDrawerToggleSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item);
    }

    public interface IOnDrawerToggle {
        public void onDrawerOpened(Parcelable parcelable);
    }

    public interface IOnGoogleApiListener {
        public void onSignInClicked();

        public void onSignOutClicked();
    }
}
