package com.tnt.scoreboard.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tnt.scoreboard.LeaderboardFragment;
import com.tnt.scoreboard.SummaryFragment;
import com.tnt.scoreboard.models.Game;

import java.util.ArrayList;
import java.util.List;

public class HistoryPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;

    public HistoryPagerAdapter(FragmentManager fm, Game game) {
        super(fm);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(LeaderboardFragment.getInstance(game));
        mFragmentList.add(SummaryFragment.getInstance(game));
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentList.get(position).toString();
    }
}
