package com.tnt.scoreboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;


public class SettingFragment extends PreferenceFragment {

    //TODO: Need specific summary for each preferences
    private static Preference.OnPreferenceChangeListener onPrefChangeListener
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

    private static void setDefaultPref(Preference preference, Class<?> cls) {
        preference.setOnPreferenceChangeListener(onPrefChangeListener);
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(preference.getContext());

        Object value;
        if (cls == Boolean.class) {
            value = sharedPreferences.getBoolean(preference.getKey(), false);
        } else if (cls == Integer.class) {
            value = sharedPreferences.getInt(preference.getKey(), -1);
        } else {
            value = sharedPreferences.getString(preference.getKey(), "");
        }
        onPrefChangeListener.onPreferenceChange(preference, value);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.setting, false);
        addPreferencesFromResource(R.xml.setting);

        setDefaultPref(findPreference(getString(R.string.pref_key_user_name)), String.class);
        setDefaultPref(findPreference(getString(R.string.pref_key_name_format)), Boolean.class);
        setDefaultPref(findPreference(getString(R.string.pref_key_theme)), String.class);
        setDefaultPref(findPreference(getString(R.string.pref_key_orientation)), String.class);
        setDefaultPref(findPreference(getString(R.string.pref_key_starting_score)), Integer.class);
        setDefaultPref(findPreference(getString(R.string.pref_key_update_delay)), Integer.class);
    }
}
