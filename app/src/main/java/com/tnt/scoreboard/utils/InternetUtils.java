package com.tnt.scoreboard.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class InternetUtils {

    public static class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        private ImageView mImageView;
        private boolean mIsCropped;

        public DownloadImage(ImageView mImageView, boolean isCropped) {
            this.mImageView = mImageView;
            this.mIsCropped = isCropped;
        }

        protected Bitmap doInBackground(String... urls) {
            try {
                InputStream in = new java.net.URL(urls[0]).openStream();
                return BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e(Constants.APP_NAME, e.getMessage());
            }
            return null;
        }

        protected void onPostExecute(Bitmap result) {
            if (result == null) return;
            mImageView.setImageBitmap(mIsCropped ? DrawableUtils.croppedBitmap(result) : result);
        }
    }
}
