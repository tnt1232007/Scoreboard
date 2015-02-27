package com.tnt.scoreboard;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tnt.scoreboard.adapters.SummaryAdapter;
import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.utils.DrawableUtils;

public class SummaryFragment extends Fragment {

    private static Game mGame;
    private ImageView mFreezeHeader;
    private RecyclerView mRecyclerView;

    public static SummaryFragment getInstance(Game game) {
        mGame = game;
        return new SummaryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        mFreezeHeader = (ImageView) view.findViewById(R.id.freezeHeader);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        int count = (int) mGame.getNumberOfPlayers() + 2;
        mRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), count, GridLayoutManager.HORIZONTAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(new SummaryAdapter(mGame));
        if (count < 10) {
            ViewGroup.LayoutParams params = mRecyclerView.getLayoutParams();
            params.height = count * 70;
            mRecyclerView.setLayoutParams(params);
        }
        return view;
    }

    public void update() {
        if (mFreezeHeader.getDrawable() != null) return;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFreezeHeader.setImageBitmap(DrawableUtils.takeScreenShot(mRecyclerView));
                ViewGroup.LayoutParams params = mFreezeHeader.getLayoutParams();
                params.width = mRecyclerView.getLayoutManager().getChildAt(0).getWidth();
                mFreezeHeader.setLayoutParams(params);
            }
        }, 400);
    }

    @Override
    public String toString() {
        return "Summary";
    }
}
