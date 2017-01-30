package com.externalalarmclock.externalalarmclock;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays the alarm status.
 */
public class AlarmStatusFragment extends ListFragment {
    private List<String> items = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private DateTimeFormatter formatter = ISODateTimeFormat.dateTime();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_activated_1, items);
        setListAdapter(adapter);

        updateItems();
    }

    @Override
    public void onResume() {
        super.onResume();

        updateItems();
    }

    private void updateItems() {
        if (adapter == null) {
            return;
        }
        adapter.clear();

        DateTime date = AlarmClockHelper.getNextAlarmClockDate(getActivity());
        String nextAlarm = "Next phone alarm: " + (date == null ? "not set " :
                date.toString(formatter));
        final String tag_alarm = "tag_alarm";
        Log.i(tag_alarm, nextAlarm);

        adapter.add(nextAlarm);
        adapter.notifyDataSetChanged();
    }
}
