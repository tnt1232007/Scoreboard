package com.tnt.scoreboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tnt.scoreboard.utils.DrawableUtils;

public class HelpFeedbackActivity extends BaseActivity {

    public static final String SCREENSHOT = "screenshot";
    public static final String EMAIL_TYPE = "message/rfc822";
    private TextView mEmailText;
    private CheckBox mFeedbackCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_help_feedback);
        String mEmailFrom = "albert.einstein@gmail.com";
        ((TextView) findViewById(R.id.emailFrom)).setText(mEmailFrom);

        mEmailText = ((TextView) findViewById(R.id.emailText));
        mFeedbackCheck = ((CheckBox) findViewById(R.id.feedbackCheck));

        Bitmap bitmap = DrawableUtils.loadBitmap(SCREENSHOT);
        ImageView screenshot = (ImageView) findViewById(R.id.screenshot);
        ImageView preview = (ImageView) findViewById(R.id.preview);
        final View previewLayout = findViewById(R.id.previewLayout);

        screenshot.setImageBitmap(bitmap);
        preview.setImageBitmap(bitmap);
        screenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewLayout.animate().alpha(1f).withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        previewLayout.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewLayout.animate().alpha(0f).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        previewLayout.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
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
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType(EMAIL_TYPE);
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email_to)});
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
                i.putExtra(Intent.EXTRA_TEXT, mEmailText.getText());

                if (mFeedbackCheck.isChecked()) {
                    Uri uri = Uri.fromFile(DrawableUtils.getAppFile(SCREENSHOT));
                    i.putExtra(Intent.EXTRA_STREAM, uri);
                }

                try {
                    startActivity(Intent.createChooser(i, "Send Mail"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "There are no email clients installed.",
                            Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
