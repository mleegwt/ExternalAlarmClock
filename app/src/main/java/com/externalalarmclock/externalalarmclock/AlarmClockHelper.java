package com.externalalarmclock.externalalarmclock;

import android.app.AlarmManager;
import android.content.Context;
import androidx.annotation.Nullable;

import org.joda.time.DateTime;

/**
 * Created by michiel on 28-1-17.
 */
public class AlarmClockHelper {
    @Nullable
    public static DateTime getNextAlarmClockDate(Context context) {
        AlarmManager man = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        AlarmManager.AlarmClockInfo nextAlarmClock = man.getNextAlarmClock();
        return nextAlarmClock == null ? null : new DateTime(nextAlarmClock.getTriggerTime());
    }

}
