package com.tnt.scoreboard.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.TypedValue;

import com.tnt.scoreboard.R;

public final class ColorUtils {

    private static LinkMap<String, Integer> colorMap;

    public static LinkMap<String, Integer> ColorMap(Context context) {
        if (colorMap == null) {
            Resources r = context.getResources();
            colorMap = new LinkMap<>();

            colorMap.put("Red", r.getColor(R.color.red));
            colorMap.put("Blue", r.getColor(R.color.blue));
            colorMap.put("Green", r.getColor(R.color.green));
            colorMap.put("Purple", r.getColor(R.color.purple));
            colorMap.put("Yellow", r.getColor(R.color.yellow));
            colorMap.put("Teal", r.getColor(R.color.teal));
            colorMap.put("Orange", r.getColor(R.color.orange));
            colorMap.put("Brown", r.getColor(R.color.brown));

            colorMap.put("Pink", r.getColor(R.color.pink));
            colorMap.put("Indigo", r.getColor(R.color.indigo));
            colorMap.put("Cyan", r.getColor(R.color.cyan));
            colorMap.put("Light Green", r.getColor(R.color.lightGreen));
            colorMap.put("Amber", r.getColor(R.color.amber));
            colorMap.put("Gray", r.getColor(R.color.gray));

            colorMap.put("Deep Purple", r.getColor(R.color.deepPurple));
            colorMap.put("Light Blue", r.getColor(R.color.lightBlue));
            colorMap.put("Lime", r.getColor(R.color.lime));
            colorMap.put("Deep Orange", r.getColor(R.color.deepOrange));
            colorMap.put("Brown", r.getColor(R.color.brown));
            colorMap.put("Blue Gray", r.getColor(R.color.blueGray));
        }
        return colorMap;
    }

    public static int getAttrColor(Context context, int colorAttrId) {
        TypedArray typedArray = context.obtainStyledAttributes(
                new TypedValue().resourceId, new int[]{colorAttrId});
        int color = typedArray.getColor(0, -1);
        typedArray.recycle();
        return color;
    }
}

