package com.tnt.scoreboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SummaryFragment extends Fragment {

    public static SummaryFragment getInstance() {
        return new SummaryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        return view;
    }

    @Override
    public String toString() {
        return "Summary";
    }
}
