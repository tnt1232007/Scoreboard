package com.tnt.scoreboard.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tnt.scoreboard.R;
import com.tnt.scoreboard.utils.LinkMap;


public class ColorAdapter extends BaseAdapter {

    private int currentColor;
    private LinkMap<String, Integer> colorMap;
    private ColorViewHolder.IOnColorPickListener listener;

    public ColorAdapter(LinkMap<String, Integer> colorMap, int currentColor) {
        this.currentColor = currentColor;
        this.colorMap = colorMap;
    }

    @Override
    public int getCount() {
        return colorMap.size();
    }

    @Override
    public Integer getItem(int position) {
        return colorMap.getValue(position);
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
            viewHolder.setListener(new ColorViewHolder.IOnColorPickListener() {
                @Override
                public void onColorPick(int chooseColor) {
                    listener.onColorPick(chooseColor);
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ColorViewHolder) convertView.getTag();
        }
        viewHolder.updateData(colorMap.getKey(position), color, color == currentColor);
        return convertView;
    }

    //<editor-fold desc="Getter Setter">
    public void setListener(ColorViewHolder.IOnColorPickListener listener) {
        this.listener = listener;
    }
    //</editor-fold>
}
