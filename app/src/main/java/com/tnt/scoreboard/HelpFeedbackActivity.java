package com.tnt.scoreboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tnt.scoreboard.utils.DrawableUtils;

public class HelpFeedbackActivity extends BaseActivity {

    public static final String SCREENSHOT = "screenshot";
    private TextView mEmailText;
    private ImageView mScreenshot;
    private Bitmap mBitmap;
    private ImageView mPreview;
    private View mPreviewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_help_feedback);
        mEmailText = ((TextView) findViewById(R.id.emailText));
        mScreenshot = (ImageView) findViewById(R.id.screenshot);
        mPreview = (ImageView) findViewById(R.id.preview);
        mPreviewLayout = findViewById(R.id.previewLayout);
        ((TextView) findViewById(R.id.emailFrom)).setText("From: albert.einstein@gmail.com");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            byte[] byteArray = bundle.getByteArray(SCREENSHOT);
            mBitmap = DrawableUtils.byteArrayToBitmap(byteArray);
            mScreenshot.setImageBitmap(mBitmap);
            mScreenshot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPreview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPreviewLayout.animate().alpha(0f).withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    mPreviewLayout.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    });
                    mPreviewLayout.animate().alpha(1f).withStartAction(new Runnable() {
                        @Override
                        public void run() {
                            mPreviewLayout.setVisibility(View.VISIBLE);
                            mPreview.setImageBitmap(mBitmap);
                        }
                    });

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu, R.menu.menu_help_feedback);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_send:
                String content = mEmailText.getText().toString().trim();
                if (content.isEmpty()) {
                    new AlertDialog.Builder(this)
                            .setMessage("Write your feedback before sending.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email_to)});
                    i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
                    i.putExtra(Intent.EXTRA_TEXT, content);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    try {
                        startActivity(i);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
