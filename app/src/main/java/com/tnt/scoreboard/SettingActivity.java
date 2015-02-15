package com.tnt.scoreboard;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.tnt.scoreboard.dataAccess.SQLiteHelper;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_setting);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.layout, new SettingFragment()).commit();
        }
    }

    public static class SettingFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.setting);

            findPreference(getString(R.string.pref_key_developer))
                    .setSummary(String.format("%s <%s>",
                            getString(R.string.developer),
                            getString(R.string.developer_email)));
            findPreference(getString(R.string.pref_key_version))
                    .setSummary(String.format("v%s.%s",
                            BuildConfig.VERSION_NAME,
                            SQLiteHelper.DATABASE_VERSION));
        }
    }
}
