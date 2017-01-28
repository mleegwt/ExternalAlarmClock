package com.externalalarmclock.externalalarmclock;

import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

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
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String host = sharedPref.getString(getResources().getString(R.string.host_pref),
                getResources().getString(R.string.host_pref_default));

        String hostKey = getResources().getString(R.string.host_key);
        adapter.add(new Tuple(hostKey, host));

        adapter.notifyDataSetChanged();
        getCapabilities();
    }

    public void getCapabilities() {
        RestClient.get("capabilities", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject capabilities) {
                boolean vibrate = capabilities.optBoolean("vibrate");
                adapter.add(new Tuple("Vibrate", vibrate ? "Yes" : "No"));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                adapter.add(new Tuple("Data error", throwable.toString()));
                adapter.notifyDataSetChanged();
            }
        });
    }
}
