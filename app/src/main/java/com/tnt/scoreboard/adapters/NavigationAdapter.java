package com.tnt.scoreboard.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tnt.scoreboard.R;

public class NavigationAdapter extends RecyclerView.Adapter<NavigationViewHolder> {

    public static final String PREFIX = "#";
    public static final String DIVIDER = "DIVIDER";
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_DIVIDER = 2;
    private final TypedArray mIcons;
    private final String[] mText;
    private final TypedArray mColors;
    private int mCurrentPosition;
    private NavigationViewHolder.IOnNavigationClickListener mListener;

    public NavigationAdapter(Context context) {
        mIcons = context.getResources().obtainTypedArray(R.array.navigation_icons);
        mText = context.getResources().getStringArray(R.array.navigation_items);
        mColors = context.getResources().obtainTypedArray(R.array.navigation_colors);
    }

    @Override
    public NavigationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case NavigationAdapter.TYPE_HEADER:
                layout = inflater.inflate(R.layout.header_navigation, parent, false);
                break;
            case NavigationAdapter.TYPE_ITEM:
                layout = inflater.inflate(R.layout.item_navigation, parent, false);
                break;
            case NavigationAdapter.TYPE_DIVIDER:
                layout = inflater.inflate(R.layout.divider_navigation, parent, false);
                break;
        }
        return new NavigationViewHolder(layout, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        String text = this.mText[position];
        return text.equals(DIVIDER) ? TYPE_DIVIDER :
                text.startsWith(PREFIX) ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(NavigationViewHolder holder, final int position) {
        holder.updateData(getItemViewType(position), mIcons.getResourceId(position, 0),
                mText[position], mColors.getResourceId(position, 0), position == mCurrentPosition);
        holder.setListener(new NavigationViewHolder.IOnNavigationClickListener() {
            @Override
            public void onNavigationClick(View v, int p) {
                notifyDataSetChanged();
                mListener.onNavigationClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mText.length;
    }

    //<editor-fold desc="Getter Setter">
    public void setCurrentPosition(int currentPosition) {
        this.mCurrentPosition = currentPosition;
    }

    public void setListener(NavigationViewHolder.IOnNavigationClickListener listener) {
        this.mListener = listener;
    }
    //</editor-fold>
}
