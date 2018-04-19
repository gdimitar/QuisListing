package com.quislisting.util;

import com.google.gson.JsonArray;

import java.util.Collection;
import java.util.Map;

public class CollectionUtils {

    private CollectionUtils() {

    }

    public static boolean isEmpty(final Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    public static boolean isNotEmpty(final Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(final Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    public static boolean isNotEmpty(final Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean isNotEmpty(final JsonArray jsonArray) {
        return jsonArray != null && jsonArray.size() > 0;
    }
}
