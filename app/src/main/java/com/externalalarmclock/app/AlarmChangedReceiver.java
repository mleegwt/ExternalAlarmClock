package com.externalalarmclock.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Forwards the broadcast to our publish service.
 */
public class AlarmChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent newIntent = new Intent(context, AlarmPublishService.class);
        newIntent.putExtras(intent);
        newIntent.setAction(intent.getAction());
        context.startService(newIntent);
    }
}
