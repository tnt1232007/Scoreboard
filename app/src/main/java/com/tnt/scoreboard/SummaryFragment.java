package com.tnt.scoreboard;

import android.graphics.Bitmap;
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
    private Bitmap mScreenShot;
    private ViewGroup.LayoutParams mParams;

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
        return view;
    }

    public void update() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mScreenShot == null) {
                    mParams = mFreezeHeader.getLayoutParams();
                    mParams.width = mRecyclerView.getLayoutManager().getChildAt(0).getWidth();
                    mScreenShot = DrawableUtils.takeScreenShot(mRecyclerView);
                }
                mFreezeHeader.setLayoutParams(mParams);
                mFreezeHeader.setImageBitmap(mScreenShot);
            }
        }, 100);
    }

    @Override
    public String toString() {
        return "Summary";
    }
}
