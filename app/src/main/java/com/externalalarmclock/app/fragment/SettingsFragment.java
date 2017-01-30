package com.externalalarmclock.app.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.externalalarmclock.app.R;

/**
 * The settings fragment.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
