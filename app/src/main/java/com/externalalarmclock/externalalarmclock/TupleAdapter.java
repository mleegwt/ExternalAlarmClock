package com.externalalarmclock.externalalarmclock;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.externalalarmclock.app.R;

import java.util.List;

/**
 * Allows displaying of key value pairs.
 */

public class TupleAdapter extends ArrayAdapter<Tuple> {
    public TupleAdapter(@NonNull Context context, @LayoutRes int resource, List<Tuple> data) {
        super(context, resource, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tuple tuple = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tuple_item, parent, false);
        }
        TextView key = (TextView) convertView.findViewById(R.id.key);
        TextView value = (TextView) convertView.findViewById(R.id.value);
        key.setText(tuple.getKey());
        value.setText(tuple.getValue());
        return convertView;
    }
}