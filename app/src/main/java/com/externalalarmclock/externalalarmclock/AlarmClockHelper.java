package com.externalalarmclock.externalalarmclock;

import android.app.AlarmManager;
import android.content.Context;
import android.support.annotation.Nullable;

import java.util.Date;

/**
 * Created by michiel on 28-1-17.
 */
public class AlarmClockHelper {
    @Nullable
    public static Date getNextAlarmClockDate(Context context) {
        AlarmManager man = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        AlarmManager.AlarmClockInfo nextAlarmClock = man.getNextAlarmClock();
        return nextAlarmClock == null ? null : new Date(nextAlarmClock.getTriggerTime());
    }

}
