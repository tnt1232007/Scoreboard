package com.tnt.scoreboard.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.tnt.scoreboard.R;
import com.tnt.scoreboard.utils.ColorUtils;

public class ColorViewHolder {

    private final int mTextColor;
    private final int mTextHighlightColor;
    private ImageView cover;
    private ImageView colorIcon;
    private TextView colorText;
    private ImageView check;

    private int mColor;
    private IOnColorPickListener listener;
    private TextDrawable.IBuilder drawableBuilder = TextDrawable.builder().round();

    public ColorViewHolder(View itemView) {
        Context context = itemView.getContext();
        mTextColor = ColorUtils.GetAttrColor(context, android.R.attr.textColorPrimary);
        mTextHighlightColor = context.getResources().getColor(R.color.lightBlueAccent);

        cover = (ImageView) itemView.findViewById(R.id.cover);
        colorIcon = (ImageView) itemView.findViewById(R.id.colorIcon);
        colorText = (TextView) itemView.findViewById(R.id.colorText);
        check = (ImageView) itemView.findViewById(R.id.check);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onColorPick(mColor);
            }
        });
    }

    public void updateData(String key, Integer value, boolean isSelected) {
        mColor = value;
        colorIcon.setImageDrawable(drawableBuilder.build(" ", value));
        colorText.setText(key);
        if (isSelected) {
            cover.setVisibility(View.GONE);
            colorText.setTextColor(mTextHighlightColor);
            check.setVisibility(View.VISIBLE);
        } else {
            cover.setVisibility(View.VISIBLE);
            colorText.setTextColor(mTextColor);
            check.setVisibility(View.GONE);
        }
    }

    public void setListener(IOnColorPickListener listener) {
        this.listener = listener;
    }

    public interface IOnColorPickListener {

        public void onColorPick(int chooseColor);
    }
}
