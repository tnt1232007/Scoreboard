package com.tnt.scoreboard.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public final class DrawableUtils {

    public static Bitmap takeScreenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static Drawable getDrawable(Context context, String name) {
        int resourceId = context.getResources().getIdentifier(
                name, "drawable", context.getPackageName());
        return context.getResources().getDrawable(resourceId);
    }
}
