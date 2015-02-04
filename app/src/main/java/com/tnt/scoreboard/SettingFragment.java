package com.tnt.scoreboard;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;


public class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.setting, false);
        addPreferencesFromResource(R.xml.setting);
    }
}
