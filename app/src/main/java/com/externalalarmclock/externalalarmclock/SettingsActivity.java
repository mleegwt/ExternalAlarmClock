package com.externalalarmclock.externalalarmclock;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.security.Timestamp;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        final EditText editText = (EditText) findViewById(R.id.edit_message);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    // TODO MLE Implement
                    handled = true;
                }
                return handled;
            }
        });

//        SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.switchForActionBar);
//        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                editText.setEnabled(isChecked);
//            }
//        });

        AlarmManager man = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final String tag_alarm = "tag_alarm";
        AlarmManager.AlarmClockInfo nextAlarmClock = man.getNextAlarmClock();
        Log.i(tag_alarm, "Next alarm is " + (nextAlarmClock == null ? "not set " :
                new Date(nextAlarmClock.getTriggerTime())));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        MenuItem item = menu.findItem(R.id.action_enable);
        item.setActionView(R.layout.switch_layout);
        return true;
    }
}
