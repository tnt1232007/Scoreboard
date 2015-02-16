package com.tnt.scoreboard;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.models.Player;
import com.tnt.scoreboard.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardFragment extends Fragment {

    private static final String LAYOUT = "layout";
    private static final String PLAYER = "player";
    private static final String SCORE = "score";
    private static final String ID = "id";

    public static LeaderboardFragment getInstance(Game game) {
        List<Player> playerList = game.getPlayers();
        Collections.sort(playerList);

        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> scores = new ArrayList<>();
        for (Player p : playerList) {
            names.add(p.getName());
            scores.add(String.valueOf(p.getScore()));
        }

        LeaderboardFragment fragment = new LeaderboardFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(PLAYER, names);
        bundle.putStringArrayList(SCORE, scores);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        ArrayList<String> names = bundle.getStringArrayList(PLAYER);
        ArrayList<String> scores = bundle.getStringArrayList(SCORE);

        final View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        final ArrayList<View> layouts = new ArrayList<>();
        ArrayList<TextView> playerViews = new ArrayList<>();
        ArrayList<TextView> scoreViews = new ArrayList<>();

        String packageName = container.getContext().getPackageName();
        Resources r = getResources();
        for (int i = 1; i < Constants.MAX_PLAYERS + 1; i++) {
            layouts.add(view.findViewById(r.getIdentifier(LAYOUT + i, ID, packageName)));
            playerViews.add((TextView) view.findViewById(r.getIdentifier(PLAYER + i, ID, packageName)));
            scoreViews.add((TextView) view.findViewById(r.getIdentifier(SCORE + i, ID, packageName)));
        }

        final int size = names.size();
        for (int i = 0; i < size; i++) {
            playerViews.get(i).setText(names.get(i));
            scoreViews.get(i).setText(scores.get(i));
        }

        if (size == Constants.TOP_PLAYERS - 1) {
            view.findViewById(R.id.layout3).setVisibility(View.GONE);
        }

        View viewAll = view.findViewById(R.id.viewAll);
        if (size <= Constants.TOP_PLAYERS) {
            viewAll.setVisibility(View.GONE);
        }

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition((ViewGroup) view, new Slide());
                v.setVisibility(View.GONE);
                for (int i = 0; i < layouts.size(); i++) {
                    if (i >= size)
                        layouts.get(i).setVisibility(View.GONE);
                    else
                        layouts.get(i).setVisibility(View.VISIBLE);
                }
                view.findViewById(R.id.divider).setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    @Override
    public String toString() {
        return "Leaderboard";
    }
}
