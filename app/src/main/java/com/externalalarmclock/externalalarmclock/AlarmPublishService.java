package com.externalalarmclock.externalalarmclock;

import android.app.AlarmManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

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

    public AlarmPublishService() {
        super("AlarmPublishService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AlarmManager man = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final String tag_alarm = "tag_alarm";
        AlarmManager.AlarmClockInfo nextAlarmClock = man.getNextAlarmClock();
        Log.i(tag_alarm, "Next alarm is " + (nextAlarmClock == null ? "not set " :
                new Date(nextAlarmClock.getTriggerTime())));

        if (intent != null) {
            final String action = intent.getAction();
            if (AlarmClock.ACTION_SET_ALARM.equals(action)) {
                try {
                    handleAlarmClockChanged(intent);
                } catch (JSONException | IOException e) {
                    Log.d("TAG", "External set alarm failed", e);
                }
            }
        }
    }

    private boolean handleAlarmClockChanged(Intent intent) throws JSONException, IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("message", intent.getStringExtra(AlarmClock.EXTRA_MESSAGE));
        jsonObject.accumulate("days", intent.getIntegerArrayListExtra(AlarmClock.EXTRA_DAYS));
        jsonObject.accumulate("hour", intent.getIntExtra(AlarmClock.EXTRA_HOUR, -1));
        jsonObject.accumulate("minutes", intent.getIntExtra(AlarmClock.EXTRA_MINUTES, -1));

        // EXTRA_Ringtone is the one used by Android, ignored while sending externally, using external variant instead
        jsonObject.accumulate("ringtone", intent.getStringExtra(EXTRA_RINGTONE_EXTERNAL));

        // EXTRA_Vibrate is the one used by Android, ignored while sending externally, using external variant instead
        jsonObject.accumulate("vibrate", intent.getBooleanExtra(EXTRA_VIBRATE_EXTERNAL, true));

        // A wakeup light may be enabled
        jsonObject.accumulate("wakeuplight", intent.getBooleanExtra(EXTRA_WAKEUP_LIGHT_EXTERNAL, true));

        // Smart alarm clocks adapt to the best moment to wake. When this is enabled the external
        // alarm clock may enable it's smart alarm features when supported.
        jsonObject.accumulate("smartalarm", intent.getBooleanExtra(EXTRA_SMART_ALARM, true));

        HttpPost httpPost = new HttpPost(URL);

        HttpClient httpclient = HttpClientBuilder.create().build();
        httpPost.setEntity(new StringEntity(jsonObject.toString()));

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        HttpResponse httpResponse = httpclient.execute(httpPost);


        return httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
    }
}
