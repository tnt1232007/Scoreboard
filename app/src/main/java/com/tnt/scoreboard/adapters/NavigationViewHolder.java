package com.tnt.scoreboard.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tnt.scoreboard.R;
import com.tnt.scoreboard.utils.ColorUtils;

public class NavigationViewHolder extends RecyclerView.ViewHolder {

    private ImageView mIcon;
    private TextView mText;
    private int mColor;
    private int mDefaultColor;
    private IOnNavigationClickListener mListener;
    private Context mContext;

    public NavigationViewHolder(View itemView, int viewType) {
        super(itemView);
        mContext = itemView.getContext();
        mDefaultColor = ColorUtils.getAttrColor(mContext, android.R.attr.textColorPrimary);

        switch (viewType) {
            case NavigationAdapter.TYPE_HEADER:
                mText = (TextView) itemView.findViewById(R.id.text);
                break;
            case NavigationAdapter.TYPE_ITEM:
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
            case NavigationAdapter.TYPE_HEADER:
                this.mText.setText(text.replace(NavigationAdapter.PREFIX, ""));
                break;
            case NavigationAdapter.TYPE_ITEM:
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

    //<editor-fold desc="Getter Setter">
    public void setListener(IOnNavigationClickListener listener) {
        mListener = listener;
    }
    //</editor-fold>

    public interface IOnNavigationClickListener {

        public void onNavigationClick(View v, int navigationOption);
    }
}
