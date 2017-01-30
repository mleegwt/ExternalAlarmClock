package com.externalalarmclock.app.activity;

import android.app.Activity;
import android.os.Bundle;

import com.externalalarmclock.app.fragment.SettingsFragment;

/**
 * Displays the settings.
 */
public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
