package com.tnt.scoreboard.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.tnt.scoreboard.R;


public class ColorViewHolder {

    private ImageView background;
    private ImageView colorIcon;
    private TextView colorText;
    private ImageView check;

    private int mColor;
    private IOnColorPickListener listener;
    private TextDrawable.IBuilder drawableBuilder = TextDrawable.builder().round();

    public ColorViewHolder(View itemView) {
        background = (ImageView) itemView.findViewById(R.id.background);
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
            background.setVisibility(View.GONE);
            colorText.setTextColor(0xff33b5e5);
            check.setVisibility(View.VISIBLE);
        } else {
            background.setVisibility(View.VISIBLE);
            colorText.setTextColor(0xff000000);
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
