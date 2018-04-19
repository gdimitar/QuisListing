package com.quislisting.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonObjectPopulator {

    private static final String TAG = JsonObjectPopulator.class.getSimpleName();

    public JsonObjectPopulator() {

    }

    public static JSONObject prepareUserJson(final String email, final String firstName,
                                             final String lastName, final String language,
                                             final boolean receiveUpdates,
                                             final boolean isUserRegistration, final String password) {
        try {
            final JSONObject userJson = new JSONObject();
            userJson.put("login", email);
            userJson.put("firstName", firstName);
            userJson.put("lastName", lastName);
            userJson.put("email", email);
            userJson.put("updates", receiveUpdates);
            switch (language) {
                case "English":
                    userJson.put("langKey", "en");
                    break;
                case "Bulgarian":
                    userJson.put("langKey", "bg");
                    break;
                case "Romanian":
                    userJson.put("langKey", "ro");
                    break;
                default:
                    userJson.put("langKey", "en");
                    break;
            }
            if (isUserRegistration) {
                userJson.put("password", password);
            }

            return userJson;
        } catch (final JSONException e) {
            Log.e(TAG, "Problem preparing user JSON object.", e);
        }

        return null;
    }

    public static JSONObject prepareContactJson(final String email, final String name,
                                                final String subject, final String message,
                                                final String language) {
        try {
            final JSONObject contactJson = new JSONObject();
            contactJson.put("email", email);
            contactJson.put("name", name);
            contactJson.put("subject", subject);
            contactJson.put("message", message);
            contactJson.put("language", language);

            return contactJson;
        } catch (final JSONException e) {
            Log.e(TAG, "Problem preparing contact JSON object.", e);
        }

        return null;
    }

    public static JSONObject prepareMessageJson(final String text) {
        try {
            final JSONObject messageJson = new JSONObject();
            messageJson.put("text", text);

            return messageJson;
        } catch (final JSONException e) {
            Log.e(TAG, "Problem preparing message JSON object.", e);
        }

        return null;
    }

    public static JSONObject prepareUserAuthenticationJson(final String username,
                                                           final String password) {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("password", password);
            jsonObject.put("rememberMe", false);
            jsonObject.put("username", username);

            return jsonObject;
        } catch (final JSONException e) {
            Log.e(TAG, "Problem preparing user authentication JSON object.", e);
        }

        return null;
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
