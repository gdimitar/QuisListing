package com.quislisting.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.quislisting.R;

import java.util.List;

public abstract class CustomArrayAdapter<T> extends ArrayAdapter<T> {

    private final Context context;
    private final List<T> inputList;
    private CustomArrayAdapter.ViewHolder viewHolder;

    public CustomArrayAdapter(final Context context, final @NonNull List<T> inputList) {
        super(context, R.layout.simple_spinner_dropdown_item, inputList);
        this.context = context;
        this.inputList = inputList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView,
                        final @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.simple_spinner_dropdown_item, parent, false);

            viewHolder = new CustomArrayAdapter.ViewHolder();
            viewHolder.itemView = (TextView) convertView.findViewById(R.id.checkedTextView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomArrayAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.itemView.setText(setViewValue(inputList, position));
        return convertView;
    }

    @Nullable
    @Override
    public T getItem(final int position) {
        return inputList.get(position);
    }

    @Override
    public View getDropDownView(final int position, final @Nullable View convertView,
                                final @NonNull ViewGroup parent) {
        final TextView itemView = (TextView) super.getView(position, convertView, parent);

        itemView.setText(setDropDownViewValue(inputList, position));
        return itemView;
    }

    private static class ViewHolder {
        private TextView itemView;
    }

    protected abstract String setViewValue(List<T> inputList, int position);

    protected abstract String setDropDownViewValue(List<T> inputList, int position);
}
