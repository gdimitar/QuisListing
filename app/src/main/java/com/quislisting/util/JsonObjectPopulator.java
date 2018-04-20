package com.quislisting.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonObjectPopulator {

    private static final String TAG = JsonObjectPopulator.class.getSimpleName();

    public JsonObjectPopulator() {

    }

    public static JSONObject prepareSearchResultJson(final String searchText,
                                                     final String searchCategoryText,
                                                     final String searchCountryText,
                                                     final String searchStateText,
                                                     final String searchCityText) {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("text", searchText);
            jsonObject.put("categoryId", searchCategoryText);
            jsonObject.put("cityId", searchCountryText);
            jsonObject.put("stateId", searchStateText);
            jsonObject.put("countryId", searchCityText);

            return jsonObject;
        } catch (final JSONException e) {
            Log.e(TAG, "Problem preparing search result JSON object.", e);
        }

        return null;
    }
}
