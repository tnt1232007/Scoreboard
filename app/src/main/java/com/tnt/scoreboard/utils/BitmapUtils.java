package com.tnt.scoreboard.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

public final class BitmapUtils {

    public static Bitmap takeScreenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}
