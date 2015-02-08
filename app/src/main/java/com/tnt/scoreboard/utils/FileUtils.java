package com.tnt.scoreboard.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public final class FileUtils {

    public static final String APP_NAME = "Scoreboard";
    public static final String DIVIDER = "/";

    public static File saveBitmap(Bitmap bitmap, String filename) {
        if (bitmap == null) return null;
        File file = getFile(filename);
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
        return BitmapFactory.decodeFile(getFile(filename).getPath(), options);
    }

    public static File getFile(String filename) {
        if (filename == null) return null;
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root, DIVIDER + APP_NAME);
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();
        return new File(dir, filename);
    }

    public static void saveLog(String filename) {
        try {
            FileOutputStream fOut = new FileOutputStream(getFile(filename));
            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            osw.write(readLog());
            osw.flush();
            osw.close();
        } catch (IOException e) {
            Log.e(APP_NAME, e.getMessage(), e);
        }
    }

    private static String readLog() {
        try {
            Process process = Runtime.getRuntime().exec(
                    new String[]{"logcat", "-d", "-t", APP_NAME + ":V", "*:S"});
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
                log.append("\r\n");
            }

            Runtime.getRuntime().exec(new String[]{"logcat", "-c"});
            return log.toString();
        } catch (IOException e) {
            Log.e(APP_NAME, e.getMessage(), e);
            return null;
        }
    }
}
