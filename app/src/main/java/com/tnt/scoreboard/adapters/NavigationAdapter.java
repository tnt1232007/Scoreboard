package com.tnt.scoreboard.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tnt.scoreboard.R;
import com.tnt.scoreboard.utils.ColorUtils;
import com.tnt.scoreboard.utils.Constants;

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.NavigationViewHolder> {

    private static final String PREFIX = "#";
    private static final String DIVIDER = "DIVIDER";
    private final TypedArray mIcons, mColors;
    private final String[] mText;
    private int mCurrentPosition;
    private IOnNavigationClickListener mListener;

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
            case Constants.TYPE_HEADER:
                layout = inflater.inflate(R.layout.item_header, parent, false);
                break;
            case Constants.TYPE_ITEM:
                layout = inflater.inflate(R.layout.item_navigation, parent, false);
                break;
            case Constants.TYPE_DIVIDER:
                layout = inflater.inflate(R.layout.divider_horizontal, parent, false);
                break;
        }
        return new NavigationViewHolder(layout, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        String text = mText[position];
        return text.equals(DIVIDER) ? Constants.TYPE_DIVIDER :
                text.startsWith(PREFIX) ? Constants.TYPE_HEADER : Constants.TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(NavigationViewHolder holder, final int position) {
        holder.updateData(getItemViewType(position), mIcons.getResourceId(position, 0),
                mText[position], mColors.getResourceId(position, 0), position == mCurrentPosition);
        holder.setListener(new IOnNavigationClickListener() {
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

    public void setCurrentPosition(int currentPosition) {
        this.mCurrentPosition = currentPosition;
    }

    public void setListener(IOnNavigationClickListener listener) {
        this.mListener = listener;
    }

    public interface IOnNavigationClickListener {

        public void onNavigationClick(View v, int navigationOption);
    }

    static class NavigationViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIcon;
        private TextView mText;
        private int mColor, mDefaultColor;
        private IOnNavigationClickListener mListener;
        private Context mContext;

        public NavigationViewHolder(View itemView, int viewType) {
            super(itemView);
            mContext = itemView.getContext();
            mDefaultColor = ColorUtils.getAttrColor(mContext, android.R.attr.textColorPrimary);

            switch (viewType) {
                case Constants.TYPE_HEADER:
                    mText = (TextView) itemView.findViewById(R.id.text);
                    break;
                case Constants.TYPE_ITEM:
                    mIcon = (ImageView) itemView.findViewById(R.id.icon);
                    mText = (TextView) itemView.findViewById(R.id.text);
                    break;
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onNavigationClick(v, -1);
                    updateState(true);
                }
            });
        }

        public void updateData(int viewType, int icon, String text, int color, boolean isSelected) {
            switch (viewType) {
                case Constants.TYPE_HEADER:
                    this.mText.setText(text.replace(PREFIX, ""));
                    break;
                case Constants.TYPE_ITEM:
                    this.mIcon.setImageResource(icon);
                    this.mText.setText(text);
                    this.mColor = color;
                    updateState(isSelected);
                    break;
            }
        }

        private void updateState(boolean isSelected) {
            if (mColor == 0) return;
            itemView.setSelected(isSelected);
            mText.setTextColor(isSelected ? mContext.getResources().getColor(mColor) : mDefaultColor);
        }

        public void setListener(IOnNavigationClickListener listener) {
            mListener = listener;
        }
    }
}
