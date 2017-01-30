package com.externalalarmclock.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.externalalarmclock.app.R;

/**
 * Created by michiel on 28-1-17.
 */
public class RestHelper {
    private final Context context;

    public RestHelper(Context context) {
        this.context = context;
    }

    @NonNull
    public String getHostUrl() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(context.getResources().getString(R.string.pref_host_uri_key),
                context.getResources().getString(R.string.pref_host_uri_default));
    }

    @NonNull
    public String getCapabiltiesUrl() {
        return getHostUrl() + context.getResources().getString(R.string.get_capabilties);
    }

    @NonNull
    public String getSetNextAlarmUrl() {
        return getHostUrl() + context.getResources().getString(R.string.set_next_alarm);
    }
}
