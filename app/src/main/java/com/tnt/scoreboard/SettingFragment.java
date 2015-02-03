package com.tnt.scoreboard;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.tnt.scoreboard.utils.PrefUtils;


public class SettingFragment extends PreferenceFragment {

    private static Preference.OnPreferenceChangeListener sSetSummaryOnPrefChangeListener
            = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    private static void setDefaultPref(Preference preference) {
        preference.setOnPreferenceChangeListener(sSetSummaryOnPrefChangeListener);
        sSetSummaryOnPrefChangeListener.onPreferenceChange(preference, PreferenceManager
                .getDefaultSharedPreferences(preference.getContext())
                .getString(preference.getKey(), ""));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        setDefaultPref(findPreference(PrefUtils.DEFAULT_NAME));
        setDefaultPref(findPreference(PrefUtils.THEME));
    }
}
