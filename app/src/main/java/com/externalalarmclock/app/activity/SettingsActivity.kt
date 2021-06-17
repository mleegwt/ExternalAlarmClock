package com.externalalarmclock.app.activity

import android.R
import android.app.Activity
import android.os.Bundle
import com.externalalarmclock.app.fragment.SettingsFragment

/**
 * Displays the settings.
 */
class SettingsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Display the fragment as the main content.
        fragmentManager.beginTransaction()
                .replace(R.id.content, SettingsFragment())
                .commit()
    }
}