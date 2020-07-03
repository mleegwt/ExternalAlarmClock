package com.externalalarmclock.externalalarmclock;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.externalalarmclock.app.R;
import com.externalalarmclock.app.util.RestHelper;
import com.externalalarmclock.pojo.AlarmClockCapabilities;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Settings for the alarm clock.
 */
public class AlarmCapabilitiesFragment extends ListFragment {
    private List<Tuple> items = new ArrayList<>();
    private ArrayAdapter<Tuple> adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new TupleAdapter(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_activated_1, items);
        setListAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        updateItems();
    }

    private void updateItems() {
        adapter.notifyDataSetInvalidated();
        adapter.clear();

        adapter.add(new Tuple(getResources().getString(R.string.host_key),
                URI.create(new RestHelper(getActivity()).getHostUrl()).getHost()));

        adapter.notifyDataSetChanged();
        getCapabilities();
    }

    public void getCapabilities() {
        VolleyHelper.getInstance(getActivity().getApplicationContext())
                .addToRequestQueue(new GsonRequest<>(new RestHelper(getActivity()).getCapabiltiesUrl(), AlarmClockCapabilities.class, null, new Response.Listener<AlarmClockCapabilities>() {
                    @Override
                    public void onResponse(AlarmClockCapabilities response) {
                        if (response.isStreamAudio()) {
                            addTuple("Stream audio", "Yes");
                        }
                        if (response.isVibrate()) {
                            addTuple("Vibrate", "Yes");
                        }
                        if (response.isWakeupLight()) {
                            addTuple("Wakeup light", "Yes");
                        }
                        if (!response.getRingTone().isEmpty()) {
                            addTuple("Ring tones", Arrays.toString(response.getRingTone().toArray(new String[0])));
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        adapter.add(new Tuple("Data error", error.toString()));
                        adapter.notifyDataSetChanged();

                    }
                }));
    }

    private void addTuple(String key, String value) {
        adapter.add(new Tuple(key, value));
    }
}
