package com.externalalarmclock.externalalarmclock;

import android.os.Bundle;
import android.preference.PreferenceFragment;

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
