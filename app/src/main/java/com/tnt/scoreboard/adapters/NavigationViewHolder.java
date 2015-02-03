package com.tnt.scoreboard.adapters;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tnt.scoreboard.R;

public class NavigationViewHolder extends RecyclerView.ViewHolder {

    private ImageView mBackground = null;
    private ImageView mIcon = null;
    private TextView mText = null;
    private int mColor;
    private IOnNavigationClickListener mListener;

    public NavigationViewHolder(View itemView, int viewType) {
        super(itemView);
        switch (viewType) {
            case NavigationAdapter.TYPE_HEADER:
                mText = (TextView) itemView.findViewById(R.id.text);
                break;
            case NavigationAdapter.TYPE_ITEM:
                mIcon = (ImageView) itemView.findViewById(R.id.icon);
                mText = (TextView) itemView.findViewById(R.id.text);
                mBackground = (ImageView) itemView.findViewById(R.id.background);
                break;
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onNavigationClick(v, -1);
                updateState();
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
                if (isSelected) {
                    updateState();
                } else {
                    mBackground.setBackgroundColor(Color.TRANSPARENT);
                    mText.setTextColor(Color.BLACK);
                }
                break;
        }
    }

    private void updateState() {
        if (mColor == 0) return;
        Resources resources = itemView.getContext().getResources();
        mBackground.setBackgroundColor(resources.getColor(R.color.highlighted));
        mText.setTextColor(resources.getColor(mColor));
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
