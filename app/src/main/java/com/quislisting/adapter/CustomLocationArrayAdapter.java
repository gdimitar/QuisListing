package com.quislisting.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.List;

public class CustomLocationArrayAdapter<Location> extends CustomArrayAdapter<Location> {

    private static final String TAG = CustomLocationArrayAdapter.class.getSimpleName();

    public CustomLocationArrayAdapter(final Context context,
                                      final @NonNull List<Location> inputList) {
        super(context, inputList);
    }

    @Override
    protected String setViewValue(final List<Location> inputList, final int position) {
        try {
            final Field field = inputList.get(position).getClass().getDeclaredField("name");
            field.setAccessible(true);
            return (String) field.get(inputList.get(position));
        } catch (final NoSuchFieldException e) {
            Log.e(TAG, "NoSuchFieldException during setting view value.", e);
        } catch (final IllegalAccessException e) {
            Log.e(TAG, "IllegalAccessException during setting view value.", e);
        }

        return null;
    }

    @Override
    protected String setDropDownViewValue(final List<Location> inputList, final int position) {
        try {
            final Field field = inputList.get(position).getClass().getDeclaredField("name");
            field.setAccessible(true);
            return (String) field.get(inputList.get(position));
        } catch (final NoSuchFieldException e) {
            Log.e(TAG, "NoSuchFieldException during setting drop down view value.", e);
        } catch (final IllegalAccessException e) {
            Log.e(TAG, "IllegalAccessException during setting drop down view value.", e);
        }

        return null;
    }
}
