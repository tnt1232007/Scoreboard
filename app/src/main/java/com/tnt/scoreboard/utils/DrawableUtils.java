package com.tnt.scoreboard.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

public final class DrawableUtils {

    public static final String APP_NAME = "Scoreboard";
    public static final String PNG = ".png";
    public static final String DIVIDER = "/";

    public static Bitmap takeScreenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static File saveBitmap(Bitmap bitmap, String filename) {
        if (bitmap == null) return null;
        File file = getAppFile(filename);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            Log.e(APP_NAME, e.getMessage(), e);
        }
        return file;
    }

    public static Bitmap loadBitmap(String filename) {
        if (filename == null) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(getAppFile(filename).getPath(), options);
    }

    public static File getAppFile(String filename) {
        if (filename == null) return null;
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root, DIVIDER + APP_NAME);
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();
        return new File(dir, filename + PNG);
    }
}
