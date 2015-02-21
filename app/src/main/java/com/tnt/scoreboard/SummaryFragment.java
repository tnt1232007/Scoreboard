package com.tnt.scoreboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tnt.scoreboard.adapters.SummaryAdapter;
import com.tnt.scoreboard.models.Game;

public class SummaryFragment extends Fragment {

    private static Game mGame;

    public static SummaryFragment getInstance(Game game) {
        mGame = game;
        return new SummaryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        int count = (int) mGame.getNumberOfPlayers() + 2;
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),
                count, GridLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new SummaryAdapter(mGame));
        if (count < 10) {
            ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
            params.height = count * 132;
            recyclerView.setLayoutParams(params);
        }
        return view;
    }

    @Override
    public String toString() {
        return "Summary";
    }
}
