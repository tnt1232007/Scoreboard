package com.tnt.scoreboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LeaderboardFragment extends Fragment {

    public static LeaderboardFragment getInstance() {
        return new LeaderboardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        return view;
    }

    @Override
    public String toString() {
        return "Leaderboard";
    }
}
