package com.externalalarmclock.app;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.externalalarmclock.app.activity.MainActivity;
import com.externalalarmclock.app.util.AlarmClockHelper;
import com.externalalarmclock.app.util.GsonRequest;
import com.externalalarmclock.app.util.RestHelper;
import com.externalalarmclock.app.util.VolleyHelper;
import com.externalalarmclock.pojo.ExternalAlarm;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests to update the external
 * alarms.
 */
public class AlarmPublishService extends IntentService {
    private static final String EXTERNAL = "_EXTERNAL";

    // Just for now.
    private static final String URL = "http://localhost";
    public static final String EXTRA_RINGTONE_EXTERNAL = AlarmClock.EXTRA_RINGTONE + EXTERNAL;
    public static final String EXTRA_VIBRATE_EXTERNAL = AlarmClock.EXTRA_VIBRATE + EXTERNAL;
    public static final String EXTRA_WAKEUP_LIGHT_EXTERNAL =
            "android.intent.extra.alarm.WAKEUP_LIGHT" + EXTERNAL;
    public static final String EXTRA_SMART_ALARM = "android.intent.extra.alarm.SMART_ALARM";
    private static final int MESSAGE_ID = 1;

    public AlarmPublishService() {
        super("AlarmPublishService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ExternalAlarm nextAlarm = getExternalAlarm();
        publishNotification(nextAlarm.getAlarmTime() == null ? R.drawable.ic_alarm_off
                        : R.drawable.ic_alarm_add, "Publishing alarm",
                "Alarm is being published, please wait...");
        GsonRequest<ExternalAlarm> request = new GsonRequest<>(
                new RestHelper(getApplicationContext()).getSetNextAlarmUrl(), ExternalAlarm.class,
                null, nextAlarm, new Response.Listener<ExternalAlarm>() {
            @Override
            public void onResponse(ExternalAlarm response) {
                publishNotification(nextAlarm.getAlarmTime() == null ? R.drawable.ic_alarm_off
                                : R.drawable.ic_alarm_on, "Alarm published",
                        "External alarm is " + nextAlarm.getAlarmTime() == null ? "disabled." : "set.");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                publishNotification(R.drawable.ic_error_outline, "Failed to publish alarm",
                        "Communication failed. Your recent alarm changes are not applied to the external alarm.");
            }
        });
        VolleyHelper.getInstance(this).addToRequestQueue(request);
//        if (intent != null) {
//            final String action = intent.getAction();
//            if (AlarmClock.ACTION_SET_ALARM.equals(action)) {
//                try {
//                    handleAlarmClockChanged(intent);
//                } catch (JSONException | IOException e) {
//                    Log.d("TAG", "External set alarm failed", e);
//                }
//            }
//        }
    }

    private ExternalAlarm getExternalAlarm() {
        ExternalAlarm alarm = new ExternalAlarm();
        alarm.setAlarmTime(AlarmClockHelper.getNextAlarmClockDate(getApplicationContext()));
        alarm.setMessage("TODO Message");

        if (alarm.getAlarmTime() == null) {
            return alarm;
        }

        alarm.setWakeupLight(true);

        // TODO MLE add other alarm options.

        return alarm;
    }

    private void publishNotification(@DrawableRes int smallIcon, String title, String content) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(smallIcon)
                        .setContentTitle(title)
                        .setContentText(content);
        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_ID, mBuilder.build());
    }

//    private boolean handleAlarmClockChanged(Intent intent) throws JSONException, IOException {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.accumulate("message", intent.getStringExtra(AlarmClock.EXTRA_MESSAGE));
//        jsonObject.accumulate("days", intent.getIntegerArrayListExtra(AlarmClock.EXTRA_DAYS));
//        jsonObject.accumulate("hour", intent.getIntExtra(AlarmClock.EXTRA_HOUR, -1));
//        jsonObject.accumulate("minutes", intent.getIntExtra(AlarmClock.EXTRA_MINUTES, -1));
//
//        // EXTRA_Ringtone is the one used by Android, ignored while sending externally, using external variant instead
//        jsonObject.accumulate("ringtone", intent.getStringExtra(EXTRA_RINGTONE_EXTERNAL));
//
//        // EXTRA_Vibrate is the one used by Android, ignored while sending externally, using external variant instead
//        jsonObject.accumulate("vibrate", intent.getBooleanExtra(EXTRA_VIBRATE_EXTERNAL, true));
//
//        // A wakeup light may be enabled
//        jsonObject.accumulate("wakeuplight", intent.getBooleanExtra(EXTRA_WAKEUP_LIGHT_EXTERNAL, true));
//
//        // Smart alarm clocks adapt to the best moment to wake. When this is enabled the external
//        // alarm clock may enable it's smart alarm features when supported.
//        jsonObject.accumulate("smartalarm", intent.getBooleanExtra(EXTRA_SMART_ALARM, true));
//
//        HttpPost httpPost = new HttpPost(URL);
//
//        HttpClient httpclient = HttpClientBuilder.create().build();
//        httpPost.setEntity(new StringEntity(jsonObject.toString()));
//
//        httpPost.setHeader("Accept", "application/json");
//        httpPost.setHeader("Content-type", "application/json");
//
//        HttpResponse httpResponse = httpclient.execute(httpPost);
//
//
//        return httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
//    }
}
