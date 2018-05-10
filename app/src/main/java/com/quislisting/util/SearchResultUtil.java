package com.quislisting.util;

import android.util.Log;

import java.lang.reflect.Field;

public class SearchResultUtil<T> {

    private static final String TAG = SearchResultUtil.class.getSimpleName();

    private final Class<T> typeParameterClass;

    public SearchResultUtil(final Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }

    public String getSelectedData(final T selectedItem) {
        try {
            final Field field = typeParameterClass.getDeclaredField("id");
            field.setAccessible(true);
            if (selectedItem != null) {
                return field.get(selectedItem) != null
                        ? String.valueOf(field.get(selectedItem)) : StringUtils.EMPTY_STRING;
            }
        } catch (final NoSuchFieldException e) {
            Log.e(TAG, "NoSuchFieldException during retrieving data.", e);
        } catch (final IllegalAccessException e) {
            Log.e(TAG, "IllegalAccessException during retrieving data.", e);
        }

        return null;
    }
}
