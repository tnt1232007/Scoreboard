package com.tnt.scoreboard;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tnt.scoreboard.adapters.LeaderboardAdapter;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.models.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardFragment extends Fragment {

    private static List<Player> mPlayerList;

    public static LeaderboardFragment getInstance(Game game) {
        mPlayerList = game.getPlayerList();
        Collections.sort(mPlayerList);
        return new LeaderboardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new LeaderboardAdapter(new ArrayList<Player>()));

        final SwipeRefreshLayout swipeLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        final View swipeRefresh = view.findViewById(R.id.swipeRefresh);
        View divider = view.findViewById(R.id.divider);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                        swipeLayout.setEnabled(false);
                        TransitionManager.beginDelayedTransition((ViewGroup) view, new Slide());
                        swipeRefresh.setVisibility(View.GONE);
                        recyclerView.setAdapter(new LeaderboardAdapter(mPlayerList));
                    }
                }, 1000);
            }
        });

        Player player;
        int size = mPlayerList.size();
        if (size == 0) return view;
        if (size >= 1) {
            player = mPlayerList.get(0);
            view.findViewById(R.id.layout1).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.player1)).setText(player.getName());
            ((TextView) view.findViewById(R.id.score1)).setText(String.valueOf(player.getScore()));
        }
        if (size >= 2) {
            player = mPlayerList.get(1);
            view.findViewById(R.id.layout2).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.player2)).setText(player.getName());
            ((TextView) view.findViewById(R.id.score2)).setText(String.valueOf(player.getScore()));
        }
        if (size >= 3) {
            player = mPlayerList.get(2);
            view.findViewById(R.id.layout3).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.player3)).setText(player.getName());
            ((TextView) view.findViewById(R.id.score3)).setText(String.valueOf(player.getScore()));
        }
        if (size >= 4) {
            swipeLayout.setVisibility(View.VISIBLE);
            swipeRefresh.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public String toString() {
        return "Leaderboard";
    }
}
