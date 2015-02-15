package com.tnt.scoreboard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.tnt.scoreboard.R;
import com.tnt.scoreboard.utils.ColorUtils;
import com.tnt.scoreboard.utils.LinkMap;

public class ColorAdapter extends BaseAdapter {

    private int mCurrentColor;
    private LinkMap<String, Integer> mColorMap;
    private IOnColorPickListener mListener;

    public ColorAdapter(LinkMap<String, Integer> colorMap, int currentColor) {
        this.mCurrentColor = currentColor;
        this.mColorMap = colorMap;
    }

    @Override
    public int getCount() {
        return mColorMap.size();
    }

    @Override
    public Integer getItem(int position) {
        return mColorMap.getValue(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Integer color = getItem(position);
        ColorViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_color, parent, false);
            viewHolder = new ColorViewHolder(convertView);
            viewHolder.setListener(new IOnColorPickListener() {
                @Override
                public void onColorPick(int chooseColor) {
                    mListener.onColorPick(chooseColor);
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ColorViewHolder) convertView.getTag();
        }
        viewHolder.updateData(mColorMap.getKey(position), color, color == mCurrentColor);
        return convertView;
    }

    public void setListener(IOnColorPickListener listener) {
        this.mListener = listener;
    }

    public interface IOnColorPickListener {

        public void onColorPick(int chooseColor);
    }

    static class ColorViewHolder {

        private final TextDrawable.IBuilder mDrawableBuilder = TextDrawable.builder().round();
        private final int mTextColor, mTextHighlightColor;
        private final ImageView mColorIcon, mCover, mCheck;
        private final TextView mColorText;

        private int mColor;
        private IOnColorPickListener mListener;

        public ColorViewHolder(View itemView) {
            Context context = itemView.getContext();
            mTextColor = ColorUtils.getAttrColor(context, android.R.attr.textColorPrimary);
            mTextHighlightColor = context.getResources().getColor(R.color.lightBlueAccent);

            mCover = (ImageView) itemView.findViewById(R.id.cover);
            mColorIcon = (ImageView) itemView.findViewById(R.id.colorIcon);
            mColorText = (TextView) itemView.findViewById(R.id.colorText);
            mCheck = (ImageView) itemView.findViewById(R.id.check);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onColorPick(mColor);
                }
            });
        }

        public void updateData(String key, Integer value, boolean isSelected) {
            mColor = value;
            mColorIcon.setImageDrawable(mDrawableBuilder.build(" ", value));
            mColorText.setText(key);
            if (isSelected) {
                mCover.setVisibility(View.GONE);
                mColorText.setTextColor(mTextHighlightColor);
                mCheck.setVisibility(View.VISIBLE);
            } else {
                mCover.setVisibility(View.VISIBLE);
                mColorText.setTextColor(mTextColor);
                mCheck.setVisibility(View.GONE);
            }
        }

        public void setListener(IOnColorPickListener listener) {
            this.mListener = listener;
        }
    }
}
