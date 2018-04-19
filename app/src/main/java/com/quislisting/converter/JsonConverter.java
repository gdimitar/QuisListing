package com.quislisting.converter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.quislisting.util.CollectionUtils;
import com.quislisting.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JsonConverter<E> {

    private static final String TAG = JsonConverter.class.getSimpleName();

    public Collection<E> extractCollectionFromJson(final String jsonResponse, final Class<E> clazz,
                                                   final boolean modifyDates) {
        try {
            final Gson gson = new Gson();
            final JsonParser jsonParser = new JsonParser();
            final JsonArray jsonArray = jsonParser.parse(jsonResponse).getAsJsonArray();

            if (CollectionUtils.isNotEmpty(jsonArray)) {
                final List<E> jsonList = new ArrayList<>();
                for (final JsonElement jsonElement : jsonArray) {
                    if (modifyDates) {
                        final JsonObject jsonObject = jsonElement.getAsJsonObject();

                        final Long created = jsonObject.get("created").getAsLong();
                        final Long modified = jsonObject.get("modified").getAsLong();
                        jsonObject.addProperty("created",
                                StringUtils.convertLongTimestampToString(created));
                        jsonObject.addProperty("modified",
                                StringUtils.convertLongTimestampToString(modified));
                    }

                    final E entity = gson.fromJson(jsonElement, clazz);
                    jsonList.add(entity);
                }
                return jsonList;
            }
        } catch (final IllegalStateException e) {
            Log.e(TAG, "Problem parsing JSON.", e);
        }

        return null;
    }

    public E extractObjectFromJson(final String jsonResponse, final Class<E> clazz) {
        final Gson gson = new Gson();
        return gson.fromJson(jsonResponse, clazz);
    }

    public E extractObjectFromJson(final String jsonResponse, final Class<E> clazz,
                                   final String createdDateField, final String modifiedDateField,
                                   final boolean isStringField) {
        try {
            final JSONObject jsonObject = new JSONObject(jsonResponse);

            final Object createdDate = (Object) jsonObject.get(createdDateField);
            final Object modifiedDate = (Object) jsonObject.get(modifiedDateField);

            jsonObject.put(createdDateField, StringUtils.convertZonedDateTime(createdDate, isStringField));
            jsonObject.put(modifiedDateField, StringUtils.convertZonedDateTime(modifiedDate, isStringField));

            final Gson gson = new Gson();
            return gson.fromJson(jsonObject.toString(), clazz);
        } catch (final JSONException e) {
            Log.e(TAG, "Problem parsing JSON.", e);
        }

        return null;
    }
}
